package similarityMetric;

import java.util.ArrayList;
import java.util.List;

public class TopicSimilarity {
	private double[][] topic_word_distribution;
	private List<String> vocList;
	private int topicNumber;

	/*
	 * Compute two phrases's similarity based on the learned topics.???
	 */
	private double computeTopicSimilarity(String phraseA, String phraseB) {
		double similarity = 0.0;
		String[] termsA = phraseA.split(" ");

		ArrayList<Integer> ListA = new ArrayList<Integer>();
		for (String term : termsA) {
			if (vocList.contains(term))
				ListA.add(vocList.indexOf(term));
		}

		String[] termsB = phraseB.split(" ");
		ArrayList<Integer> ListB = new ArrayList<Integer>();
		for (String term : termsB) {
			if (vocList.contains(term))
				ListB.add(vocList.indexOf(term));
		}

		similarity = relevance(ListA, ListB);
		return similarity;
	}

	/*
	 * Relevance Compute.
	 */
	private double relevance(ArrayList<Integer> ListA, ArrayList<Integer> ListB) {
		double score = 0.0;
		/*
		 * terms = ListB + ListB.
		 */
		int[] terms = new int[ListA.size() + ListB.size()];
		int index = 0;
		for (int m = 0; m < ListA.size(); m++) {
			terms[index] = ListA.get(m);
			index++;
		}
		for (int n = 0; n < ListB.size(); n++) {
			terms[index] = ListB.get(n);
			index++;
		}
		// The likelihood of jointly generating ListA and ListB.
		score = computeLikelihood(terms);
		return score;
	}

	/*
	 * Data's likelihood.
	 */
	private double computeLikelihood(int[] terms) {
		double likelihood = 0.0;
		for (int i = 0; i < topicNumber; i++) {
			double tmp = 1.0;
			for (int j = 0; j < terms.length; j++) {
				tmp *= topic_word_distribution[i][terms[j]];
			}
			likelihood += tmp;
		}
		return likelihood;
	}

	public void initializeParameter(double[][] topic_word_distribution, List<String> vocList, int topicNumber){
		this.topic_word_distribution = new double[topic_word_distribution.length][];
		for(int i = 0; i < topic_word_distribution.length; i++){
			this.topic_word_distribution[i] = new double[topic_word_distribution[i].length];
			for(int j = 0; j < topic_word_distribution[i].length; j++){
				this.topic_word_distribution[i][j] = topic_word_distribution[i][j];
			}
		}
		this.vocList = new ArrayList<String>();
		for(int i = 0; i < vocList.size(); i++){
			this.vocList.add(vocList.get(i));
		}
		this.topicNumber = topicNumber;
	}
	public double[][] computeTopicSimialrityMatrix(String[] termArray) {
		double[][] similarityMatrix = new double[termArray.length][termArray.length];

		/* Compute the phrase-phrase similarity matrix. */
		for (int i = 0; i < termArray.length; i++) {
			for (int j = 0; j < termArray.length; j++) {
				if (i == j)
					similarityMatrix[i][j] = 1;
				else {
					double similarity = computeTopicSimilarity(termArray[i],
							termArray[j]);
					similarityMatrix[i][j] = similarity;
				}
			}
		}
		return similarityMatrix;
	}

}
