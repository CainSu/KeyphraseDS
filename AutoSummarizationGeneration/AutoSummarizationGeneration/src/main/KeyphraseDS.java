package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import similarityMetric.ESA;
import similarityMetric.ProbaseAccess;
import similarityMetric.ProbaseSimilarity;
import similarityMetric.TopicSimilarity;
import util.FileDataAccesser;
import util.MathUtil;

public class KeyphraseDS {
	// Project configures.
	public String basePath; // The directory of data files.
	public String queryPhrase;
	public String dataFileName;
	public String phraseFileName;
	public String vocabularyFileName;
	public String featureVectorMatrixFileName;
	public String indexTermArrayFileName;
	public int wikiDocs;
	public String wikiIndexDir;
	public String probaseUrl;
	public String probaseUser;
	public String probasePsw;
	public String probaseClassForName;

	// Data Structure.
	public ArrayList<int[]> indexTermArray; // The index of terms in sentence.
	public int[][] featureVectorMatrix; // The feature matrix of the document.
	public String[] sentenceArray;
	public String[] vocabularyArray;
	public String[] phraseArray;
	public int[][] sentence2phraseOccurrenceMatrix;

	// Algorithm parameters.
	public int clusterNum = 13;
	public double tradeoffTopic = 0.4;
	public double tradeoffProbase = 0.2;
	public double tradeoffESA = 0.4;
	public double candidatePenalty = 0.7;
	public double lamda1 = 0.4;
	public double lamda2 = 0.4;
	public double lamda3 = 0.2;
	public int summaryLength = 250;

	// Property class.
	private Properties properties;

	public static void main(String[] args) throws Exception{
		KeyphraseDS kDS = new KeyphraseDS();
		kDS.loadProjectConfigures();
		kDS.run();
	}
	
	public void run() throws Exception {
		// Initialize the DataReader class.
		DataReader dataReader = new DataReader();
		dataReader.readSentenceAndFeatures(basePath + "\\"
				+ dataFileName, basePath + "\\"
				+ vocabularyFileName, basePath
				+ "\\" + featureVectorMatrixFileName,
				basePath + "\\"
						+ indexTermArrayFileName);
		dataReader.readPhraseFromSentences(basePath + "\\"
				+ phraseFileName);
		// Load features.
		loadData(dataReader.getIndexTermArray(),
				dataReader.getFeatureVectorMatrix(),
				dataReader.getSentenceArray(), dataReader.getVocabularyArray(),
				dataReader.getPhraseArray(),
				dataReader.getSentence2phraseOccurrenceMatrix());

		// Process
		keyphraseDS_run();
	}

	/*
	 * Load Project configures.
	 */
	public void loadProjectConfigures() {
		this.properties = new Properties();
		try {
			properties
					.load(new FileInputStream(new File("./config.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Read the corresponding configures.
		this.basePath = ((String) properties.getProperty("basePath"));
		this.queryPhrase = ((String) properties.getProperty("queryPhrase"));
		this.dataFileName = ((String) properties.getProperty("dataFileName"));
		this.phraseFileName = ((String) properties
				.getProperty("phraseFileName"));
		this.vocabularyFileName = ((String) properties
				.getProperty("vocabularyFileName"));
		this.featureVectorMatrixFileName = ((String) properties
				.getProperty("featureVectorMatrixFileName"));
		this.indexTermArrayFileName = ((String) properties
				.getProperty("indexTermArrayFileName"));
		this.wikiDocs = Integer.valueOf((String) properties
				.getProperty("wikiDocs"));
		this.wikiIndexDir = ((String) properties.getProperty("wikiIndexDir"));
		this.probaseUrl = ((String) properties.getProperty("probaseUrl"));
		this.probaseUser = ((String) properties.getProperty("probaseUser"));
		this.probasePsw = ((String) properties.getProperty("probasePsw"));
		this.probaseClassForName = ((String) properties
				.getProperty("probaseClassForName"));
		System.out.println("KeyphraseDS.loadProjectConfigures()");
	}

	/*
	 * Load data.
	 */
	public void loadData(ArrayList<int[]> indexTermArray,
			int[][] featureVectorMatrix, String[] sentenceArray,
			String[] vocabularyArray, String[] phraseArray,
			int[][] sentence2phraseOccurrenceMatrix) {
		this.indexTermArray = indexTermArray;
		this.featureVectorMatrix = featureVectorMatrix;
		this.sentenceArray = sentenceArray;
		this.vocabularyArray = vocabularyArray;
		this.phraseArray = phraseArray;
		this.sentence2phraseOccurrenceMatrix = sentence2phraseOccurrenceMatrix;
	}

	/*
	 * Core process.
	 */
	public void keyphraseDS_run() throws Exception {
		System.out.println("Begin!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------
		// TopicModel - LDA Run.
		int topicNum = 30; //Topic Number is fixed.
		
		
		LdaGibbsSampler topicModel = new LdaGibbsSampler();
		topicModel.lda_run(this.indexTermArray, this.vocabularyArray.length, topicNum);
		double[][] phi = topicModel.getPhi();
		int[][] sentence2topicCount = topicModel.nd;
		System.out.println("LDA done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------
		
		// Knowledge-based similarity matrix.
		TopicSimilarity topicSimilarity = new TopicSimilarity();
		topicSimilarity.initializeParameter(phi,
				Arrays.asList(vocabularyArray), topicNum);
		double[][] phraseTopicSimilarityMatrix = topicSimilarity
				.computeTopicSimialrityMatrix(phraseArray);
		for(int i = 0; i < phraseTopicSimilarityMatrix.length; i++){
			for(int j = 0; j < phraseTopicSimilarityMatrix[i].length; j++){
				if(Double.isInfinite(phraseTopicSimilarityMatrix[i][j]) || Double.isNaN(phraseTopicSimilarityMatrix[i][j]))
					phraseTopicSimilarityMatrix[i][j] = 0.0;
			}
		}
		System.out.println("TopicSimilarity done!" + " @Time:" + (new Date()).toString());
		
		ProbaseAccess pA = new ProbaseAccess();
		pA.initializeParameter(this.probaseUrl, this.probaseUser,
				this.probasePsw, this.probaseClassForName);
		ProbaseSimilarity probaseSimilarity = new ProbaseSimilarity();
		double[][] phraseProbaseSimilarityMatrix = probaseSimilarity
				.computePhraseSimilarityMatrix(pA, phraseArray);
		for(int i = 0; i < phraseProbaseSimilarityMatrix.length; i++){
			for(int j = 0; j < phraseProbaseSimilarityMatrix[i].length; j++){
				if(Double.isInfinite(phraseProbaseSimilarityMatrix[i][j]) || Double.isNaN(phraseProbaseSimilarityMatrix[i][j]))
					phraseProbaseSimilarityMatrix[i][j] = 0.0;
			}
		}
		System.out.println("ProbaseSimilarity done!" + " @Time:" + (new Date()).toString());

		ESA esa = new ESA();
		esa.initializeParameter(wikiDocs, wikiIndexDir);
		double[][] phraseESASimilarityMatrix = esa
				.calculateSimilarityMatrix(phraseArray);
		for(int i = 0; i < phraseESASimilarityMatrix.length; i++){
			for(int j = 0; j < phraseESASimilarityMatrix[i].length; j++){
				if(Double.isInfinite(phraseESASimilarityMatrix[i][j]) || Double.isNaN(phraseESASimilarityMatrix[i][j]))
					phraseESASimilarityMatrix[i][j] = 0.0;
			}
		}
		System.out.println("ESASimilarity done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Phrase graph construct.
		double[][] phraseAffinityMatrix = computePhraseAffinity(tradeoffTopic,
				tradeoffProbase, tradeoffESA, phraseTopicSimilarityMatrix,
				phraseProbaseSimilarityMatrix, phraseESASimilarityMatrix);
		double[] phraseInitialScore = initializePhraseInitialScore(queryPhrase,
				tradeoffTopic, tradeoffProbase, tradeoffESA,
				phraseTopicSimilarityMatrix, phraseProbaseSimilarityMatrix,
				phraseESASimilarityMatrix);
		// Save Inter-Variables.
		FileDataAccesser.saveMatrix2File(phraseAffinityMatrix,
				"./phraseAffinityMatrix.txt");
		FileDataAccesser.saveVector2File(phraseInitialScore,
				"./phraseInitialScore.txt");
		System.out.println("PhraseGraphConstruct done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Phrase Ranking.
		String phraseRankingScorePath = "./phraseScore.txt";
		double tradeoffRanking = 0.5;
		RegularizationRanking rR = new RegularizationRanking();
		rR.run("./phraseAffinityMatrix.txt", "./phraseInitialScore.txt",
				phraseRankingScorePath, tradeoffRanking);
		
		double[] phraseRankingScore  = FileDataAccesser
				.getDoubleVectorFromFile(phraseRankingScorePath);
		System.out.println("PhraseRanking done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------
		
		
		// Phrase Clustering.
		String clusterPath = "./cluster.txt";
		int[] sentenceClusterLabel = null;
		SpectraClustering sC = new SpectraClustering();
		sC.run("./phraseAffinityMatrix.txt", this.clusterNum, clusterPath);
		sentenceClusterLabel = FileDataAccesser
				.getIntVectorFromFile(clusterPath);
		System.out.println("PhraseClustering done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Sentence Relevance.
		ArrayList<int[]> sectionFeatures = this.getSectionFeature(
				sentenceClusterLabel, phraseArray, vocabularyArray,
				this.clusterNum);
		double[][] section2SentenceRelevanceMatrix = this
				.computeSection2SentenceRelevance(indexTermArray,
						sectionFeatures, topicNum, phi);
		System.out.println("SenenceRelevance done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Sentence Diversity.
		double[][] topicDistanceMatrix = initializeTopicDistanceMatrix(phi, topicNum);
		double[] sentenceDiversityArray = sentenceDiversity(topicNum,
				sentence2topicCount, topicDistanceMatrix);
		System.out.println("SentenceDiversity done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Greedy Candidate Selection.
		double[][] sentenceSimilarityMatrix = this
				.computeSentence2SentenceSimilarityMatrix(sentence2phraseOccurrenceMatrix,
						phraseAffinityMatrix);
		ArrayList<int[]> sectionCandidateSentence = new ArrayList<int[]>();
		ArrayList<int[]> sectionCandidatePhrase = new ArrayList<int[]>();
		int candidateTopK = 15;
		this.candidateSentenceInitializeGreedy(sentenceArray,
				sectionCandidateSentence, sectionCandidatePhrase,
				section2SentenceRelevanceMatrix, sentence2phraseOccurrenceMatrix,
				sentenceSimilarityMatrix, candidateTopK, candidatePenalty);
		System.out.println("GreedyCandidateSelection done!" + " @Time:" + (new Date()).toString());
		// -----------------------------------------------------------------------------------------------------------

		// Optimization.
		String sectionResultPath = basePath + "\\sectionResult.txt";
		String globalResultPath = basePath + "\\sentenceLabelILP.txt";
		IBMCplex.sectionSpecificOptimizer(sectionCandidateSentence,
				sectionCandidatePhrase, section2SentenceRelevanceMatrix,
				sentenceDiversityArray, sentence2phraseOccurrenceMatrix,
				phraseRankingScore, sentenceArray, summaryLength, lamda1,
				lamda2, lamda3, sectionResultPath, globalResultPath);
		System.out.println("ILPOptimization done!" + " @Time:" + (new Date()).toString());
	}

	/*
	 * compute phrase-phrase similarity considering topic semantic, probase, and
	 * ESA.
	 */
	private double[][] computePhraseAffinity(double tradeoffTopic,
			double tradeoffProbase, double tradeoffESA, double[][] phraseTopic,
			double[][] phraseProbase, double[][] phraseESA) {
		double[][] phraseAffinityMatrix = new double[this.phraseArray.length][];
		for (int i = 0; i < this.phraseArray.length; i++) {
			phraseAffinityMatrix[i] = new double[this.phraseArray.length];
		}

		for (int i = 0; i < this.phraseArray.length; i++) {
			for (int j = i; j < this.phraseArray.length; j++) {
				if (i == j)
					phraseAffinityMatrix[i][j] = 1;
				else {
					double similarity = tradeoffTopic * phraseTopic[i][j]
							+ tradeoffProbase * phraseProbase[i][j]
							+ tradeoffESA * phraseESA[i][j];
					phraseAffinityMatrix[i][j] = phraseAffinityMatrix[j][i] = similarity;
				}
			}
		}
		return phraseAffinityMatrix;
	}

	/*
	 * initialize the ranking score of phrase with respect to the queryPhrase.
	 */
	private double[] initializePhraseInitialScore(String queryPhrase,
			double tradeoffTopic, double tradeoffProbase, double tradeoffESA,
			double[][] phraseTopic, double[][] phraseProbase,
			double[][] phraseESA) {
		double[] phraseInitialScore = new double[this.phraseArray.length];

		int indexQuery = -1;
		for (int i = 0; i < this.phraseArray.length; i++) {
			if (this.phraseArray[i].equals(queryPhrase))
				indexQuery = i;
		}

		if (indexQuery == -1) {// if query Phrase is not in the phrase list.
			for (int i = 0; i < this.phraseArray.length; i++) {
				phraseInitialScore[i] = (double) 1 / this.phraseArray.length;
			}
		} else {
			for (int i = 0; i < this.phraseArray.length; i++) {
				if (i == indexQuery)
					phraseInitialScore[i] = 1;
				else {
					double score = tradeoffTopic * phraseTopic[indexQuery][i]
							+ tradeoffProbase * phraseProbase[indexQuery][i]
							+ tradeoffESA * phraseESA[indexQuery][i];
					phraseInitialScore[i] = score;
				}
			}
		}
		return phraseInitialScore;
	}

	/*
	 * Compute topic diversity for each sentence;
	 */
	private double[] sentenceDiversity(int topicNum,
			int[][] sentence2topicCount, double[][] topicDistanceMatrix) {
		double[] sentenceDiversityArray = new double[this.sentenceArray.length];
		double SUM = 0;
		for (int index = 0; index < sentenceDiversityArray.length; index++) {
			double score = 0.0;
			for (int i = 0; i < topicNum; i++) {
				for (int j = 0; j < topicNum; j++) {
					score += sentence2topicCount[index][i]
							* sentence2topicCount[index][j]
							* topicDistanceMatrix[i][j];
				}
			}
			sentenceDiversityArray[index] = score;
			SUM += (score);
		}
		for (int index = 0; index < sentenceDiversityArray.length; index++) {
			if (SUM == 0)
				sentenceDiversityArray[index] = 0;
			else
				sentenceDiversityArray[index] = (sentenceDiversityArray[index])
						/ SUM;
		}
		return sentenceDiversityArray;
	}

	/*
	 * Initialize the topic-to-topic distant matrix.
	 */
	private double[][] initializeTopicDistanceMatrix(
			double[][] topic_word_distribution, int topicNum) {
		double[][] topicDistanceMatrix = new double[topicNum][topicNum];
		for (int i = 0; i < topicNum; i++) {
			for (int j = i; j < topicNum; j++) {
				topicDistanceMatrix[i][j] = topicDistanceMatrix[j][i] = MathUtil
						.computeKLdistance(topic_word_distribution[i],
								topic_word_distribution[j]);
			}
		}
		return topicDistanceMatrix;
	}

	/*
	 * compute sentence's relevance to section(cluster).
	 * 
	 * @param sentenceList: each sentence's feature list.
	 * 
	 * @param sectionList: each section's feature list.
	 * 
	 * @param smooth: the smooth parameter for normalize.
	 */
	private double[][] computeSection2SentenceRelevance(
			ArrayList<int[]> indexTermArray, ArrayList<int[]> sectionList,
			int topicNum, double[][] topic2wordDistribution) {
		double[][] section2SentenceRelevanceMatrix = new double[sectionList
				.size()][indexTermArray.size()];
		for (int i = 0; i < sectionList.size(); i++) {
			int[] sectionTerms = sectionList.get(i);
			for (int j = 0; j < indexTermArray.size(); j++) {
				int[] sentenceTerms = indexTermArray.get(j);
				/*
				 * terms = sectionTerms + sentenceTerms.
				 */
				int[] terms = new int[sectionTerms.length
						+ sentenceTerms.length];
				int index = 0;
				for (int m = 0; m < sectionTerms.length; m++) {
					terms[index] = sectionTerms[m];
					index++;
				}
				for (int n = 0; n < sentenceTerms.length; n++) {
					terms[index] = sentenceTerms[n];
					index++;
				}
				// The likelihood of jointly generating sentence and section.
				section2SentenceRelevanceMatrix[i][j] = computeLikelihood(
						terms, topicNum, topic2wordDistribution);
			}
		}
		/* Smooth normalize */
		for (int i = 0; i < sectionList.size(); i++) {
			double SUM = 0;
			for (int j = 0; j < indexTermArray.size(); j++) {
				SUM += (section2SentenceRelevanceMatrix[i][j]);
			}
			for (int j = 0; j < indexTermArray.size(); j++) {
				if (SUM == 0)
					section2SentenceRelevanceMatrix[i][j] = 0;
				else
					section2SentenceRelevanceMatrix[i][j] = (section2SentenceRelevanceMatrix[i][j])
							/ SUM;
			}
		}
		return section2SentenceRelevanceMatrix;
	}

	/*
	 * compute the data's likelihood
	 * 
	 * @param terms: the jointly generated terms.
	 */
	private double computeLikelihood(int[] terms, int topicNum,
			double[][] topic2wordDistribution) {
		double likelihood = 0.0;
		for (int i = 0; i < topicNum; i++) {
			double tmp = 1; // If the number of terms is very bigger, the tmp
							// should be defined as Double.MaxValue.
			for (int j = 0; j < terms.length; j++) {
				tmp *= topic2wordDistribution[i][terms[j]];
			}
			likelihood += tmp;
		}
		return likelihood;
	}

	/*
	 * 
	 * @param clusterNum: the initial number of clusters.
	 * 
	 * @return All cluster/section's feature list. Note: the size of the
	 * returned result is less than @param:clusterNum, for possible empty
	 * cluster.
	 */
	private ArrayList<int[]> getSectionFeature(int[] sentenceClusterLabel,
			String[] phraseList, String[] vocabulary, int clusterNum) {
		ArrayList<int[]> sectionFeatures = new ArrayList<int[]>();
		List<String> vocList = Arrays.asList(vocabulary);

		for (int i = 0; i < clusterNum; i++) {
			/* Collect the current cluster's phrase-terms. */
			ArrayList<String> terms = new ArrayList<String>();
			for (int j = 0; j < sentenceClusterLabel.length; j++) {
				if (sentenceClusterLabel[j] == (i + 1)) {// Judge if the j-th
															// phrase is
															// assigned to
															// cluster i.
					String[] phrases = phraseList[j].split(" ");
					for (String phrase : phrases)
						terms.add(phrase);
				}
			}
			/* Judge if the current cluster is empty. */
			if (terms.size() > 0) {
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				for (int j = 0; j < terms.size(); j++) {
					if (vocList.contains(terms.get(j)))
						tmp.add(vocList.indexOf(terms.get(j)));
				}
				int[] section = new int[tmp.size()];
				for (int m = 0; m < tmp.size(); m++)
					section[m] = tmp.get(m);
				sectionFeatures.add(section);
			}

		}
		return sectionFeatures;
	}

	/*
	 * compute sentence-sentence similarity matrix.
	 */
	private double[][] computeSentence2SentenceSimilarityMatrix(
			int[][] sentence2phraseOccurrenceMatrix,
			double[][] phraseAffinityMatrix) {
		int sentenceCount = sentence2phraseOccurrenceMatrix.length;
		double[][] sentenceSimilarityMatrix = new double[sentenceCount][sentenceCount];
		for (int i = 0; i < sentenceCount; i++) {
			for (int j = i; j < sentenceCount; j++) {
				if (i == j)
					sentenceSimilarityMatrix[i][j] = 1;
				else {
					double SUM = 0;
					int count = 0;
					for (int pA = 0; pA < sentence2phraseOccurrenceMatrix[i].length; pA++) {
						if (sentence2phraseOccurrenceMatrix[i][pA] != 0) {
							for (int pB = 0; pB < sentence2phraseOccurrenceMatrix[j].length; pB++) {
								if (sentence2phraseOccurrenceMatrix[j][pB] != 0) {
									SUM += phraseAffinityMatrix[pA][pB];
									count++;
								}
							}
						}
					}
					if (count == 0)
						sentenceSimilarityMatrix[i][j] = sentenceSimilarityMatrix[j][i] = 0;
					else
						sentenceSimilarityMatrix[i][j] = sentenceSimilarityMatrix[j][i] = SUM
								/ count;
				}
			}
		}
		return sentenceSimilarityMatrix;
	}

	/*
	 * compute each section's candidate sentences and phrases, i.e.,
	 * sectionCandidateSentence && sectionCandidatePhrase.
	 */
	private void candidateSentenceInitializeGreedy(String[] sentence,
			ArrayList<int[]> sectionCandidateSentence,
			ArrayList<int[]> sectionCandidatePhrase,
			double[][] section2SentenceRelevanceMatrix,
			int[][] sentence2phraseOccurrenceMatrix,
			double[][] sentenceSimilarityMatrix, int topK, double penalty) {
		double[][] section2SentenceRelevanceMatrixDuplicate = new double[section2SentenceRelevanceMatrix.length][section2SentenceRelevanceMatrix[0].length];
		int[] sentenceLength = new int[sentence.length];
		for (int i = 0; i < sentence.length; i++) {
			sentenceLength[i] = sentence[i].trim().split(" ").length;
		}

		int[] sentenceAssignment = new int[section2SentenceRelevanceMatrix[0].length];
		for (int j = 0; j < section2SentenceRelevanceMatrix[0].length; j++) {// Each
			// Sentence
			// Filter these sentences contained no phrases and length is small.
			int phraseCount = 0;
			for (int s = 0; s < sentence2phraseOccurrenceMatrix[j].length; s++)
				phraseCount += sentence2phraseOccurrenceMatrix[j][s];
			if (phraseCount == 0 && sentenceLength[j] < 15)
				continue;

			double[] sectionRelevance = new double[section2SentenceRelevanceMatrix.length];
			ArrayList<Double> sectionRelevanceList = new ArrayList<Double>();
			for (int i = 0; i < section2SentenceRelevanceMatrix.length; i++) {// Each
																				// Section
				sectionRelevance[i] = section2SentenceRelevanceMatrix[i][j];
				sectionRelevanceList.add(section2SentenceRelevanceMatrix[i][j]);
			}
			// Rank the relevance.
			Arrays.sort(sectionRelevance);
			double relevance = sectionRelevance[sectionRelevance.length - 1];
			/* The selected section. Section assignment */
			// The sentence j is assigned to section index.
			int index = sectionRelevanceList.indexOf(relevance);
			sentenceAssignment[j] = index;
			/* Influence to other sentence's relevance to this section. */
			for (int s = 0; s < section2SentenceRelevanceMatrix[index].length; s++) {
				section2SentenceRelevanceMatrix[index][s] = section2SentenceRelevanceMatrix[index][s]
						- penalty * sentenceSimilarityMatrix[s][j] * relevance;
			}
		}

		/* Re-organize the result. */
		for (int i = 0; i < section2SentenceRelevanceMatrix.length; i++) {// Each
			// section.
			ArrayList<Integer> selected = new ArrayList<Integer>();
			for (int j = 0; j < sentenceAssignment.length; j++) {
				if (sentenceAssignment[j] == i)
					selected.add(j);
			}

			int[] selectedSentence;
			if (topK < selected.size()) {
				selectedSentence = new int[topK];
				for (int m = 0; m < topK; m++) {
					double MAX = Double.MIN_VALUE;
					int index = 0;
					for (int n = 0; n < selected.size(); n++) {
						if (section2SentenceRelevanceMatrixDuplicate[i][selected
								.get(n)] > MAX) {
							MAX = section2SentenceRelevanceMatrixDuplicate[i][selected
									.get(n)];
							index = n;
						}
					}
					selectedSentence[m] = selected.get(index);
					section2SentenceRelevanceMatrixDuplicate[i][selected
							.get(index)] = Double.MIN_VALUE;
				}
			} else {
				selectedSentence = new int[selected.size()];
				for (int m = 0; m < selected.size(); m++) {
					selectedSentence[m] = selected.get(m);
				}
			}

			// Obtain the contained phrases.
			ArrayList<Integer> selectedPhrase = new ArrayList<Integer>();
			for (int p = 0; p < sentence2phraseOccurrenceMatrix.length; p++) {// Each
				// sentence.
				for (int j = 0; j < selectedSentence.length; j++) {
					if (selectedSentence[j] == p) {// Indicate current sentence
													// is selected.
						for (int m = 0; m < sentence2phraseOccurrenceMatrix[p].length; m++) {
							if (sentence2phraseOccurrenceMatrix[p][m] != 0)
								selectedPhrase.add(m);
						}
						break;
					}
				}
			}

			int[] selectedPhraseArray = new int[selectedPhrase.size()];
			for (int p = 0; p < selectedPhrase.size(); p++)
				selectedPhraseArray[p] = selectedPhrase.get(p);
			// Add the selected sentences and phrases to the List.
			sectionCandidateSentence.add(selectedSentence);
			sectionCandidatePhrase.add(selectedPhraseArray);
		}
	}

}
