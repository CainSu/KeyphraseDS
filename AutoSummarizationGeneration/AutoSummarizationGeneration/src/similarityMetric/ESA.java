package similarityMetric;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

public class ESA {

	private int wikiDocs = 187239;
	private String wikiIndexDir = "D:\\Index\\wikiComputerTextIndex\\";

	private double[] shortText2vec(String str, int size) throws Exception {
		String s = str;

		double[] wordList = new double[size];
		for (int i = 0; i < wordList.length; i++)
			wordList[i] = 0;

		String[] sList = s.split(" ");
		for (int i = 0; i < sList.length; i++) {
			double[] tList = TfIdfindex(sList[i], size);
			for (int j = 0; j < size; j++) {
				wordList[j] += tList[j];
			}
		}
		return wordList;
	}

	private double[] TfIdfindex(String keyword, int size) throws IOException {
		String indexDir = this.wikiIndexDir;

		double[] wordvec = new double[size];

		IndexSearcher searcher = new IndexSearcher(indexDir);

		Term term = new Term("contents", keyword);
		TermQuery luceneQuery = new TermQuery(term);

		TopDocs results = searcher.search(luceneQuery, 400000);
		int numTotalHits = results.totalHits;

		ScoreDoc[] hits = results.scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document document = searcher.doc(hits[i].doc);
			String fileName = document.get("filename");
			int index = Integer.valueOf(fileName.substring(24).replace(".txt",
					""));
			int count = Integer.valueOf(document.get("count"));
			wordvec[index] = (double) count * Math.log(wikiDocs / numTotalHits);
		}
		return wordvec;
	}

	private Directory featureVectorIndex(ArrayList<double[]> conceptList)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {
		// LuceneIndex Writer.
		Analyzer simple = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriter indexWriter = new IndexWriter(directory, simple, true,
				IndexWriter.MaxFieldLength.UNLIMITED);
		int rows = conceptList.size();
		int columns = conceptList.get(0).length;
		for (int j = 0; j < columns; j++) {
			String termName = String.valueOf(j);
			String itemList = "";
			for (int i = 0; i < rows; i++) {
				if (conceptList.get(i)[j] != 0) {
					itemList += String.valueOf(i) + " ";
				}
			}
			if (itemList.equals(""))
				continue;
			else {
				Document doc = new Document();
				doc.add(new Field("term", termName, Field.Store.YES,
						Index.NOT_ANALYZED));
				doc.add(new Field("itemList", itemList.trim(), Field.Store.YES,
						Field.Index.ANALYZED));
				indexWriter.addDocument(doc);
			}
		}
		indexWriter.close();
		return directory;
	}

	private ArrayList<double[]> getTermFeatureVector(String[] phraseArray) throws Exception {

		int size = this.wikiDocs;// Because there are 187239
									// wikipedia-documents.
		/* Get each phrase's concept feature vector. */
		ArrayList<double[]> conceptList = new ArrayList<double[]>();
		for (String phrase : phraseArray) {
			double[] instance = shortText2vec(phrase, size);
			conceptList.add(instance);
		}
		
		return conceptList;
	}

	private double cosine_similarityOptimized(IndexSearcher iSearcher,
			int itemA, int itemB, int queryMaximize,
			ArrayList<double[]> conceptList) throws IOException {
		Query query1 = null;
		Query query2 = null;
		BooleanQuery query = null;
		query1 = new TermQuery(new Term("itemList", String.valueOf(itemA)));
		query2 = new TermQuery(new Term("itemList", String.valueOf(itemB)));
		query = new BooleanQuery();
		query.add(query1, BooleanClause.Occur.MUST);
		query.add(query2, BooleanClause.Occur.MUST);
		TopDocs topDocs = iSearcher.search(query, queryMaximize);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		double sum = 0;
		double len1 = 0;
		double len2 = 0;
		for (int i = 0; i < topDocs.totalHits; i++) {
			Document targetDoc = iSearcher.doc(scoreDocs[i].doc);
			int termID = Integer.valueOf(targetDoc.get("term"));
			double scoreA = conceptList.get(itemA)[termID];
			double scoreB = conceptList.get(itemB)[termID];
			sum += scoreA * scoreB;
			len1 += scoreA * scoreA;
			len2 += scoreB * scoreB;
		}
		return sum / (Math.sqrt(len1) * Math.sqrt(len2));
	}

	/*
	 * Initialzie the ESA wiki parameters.
	 */
	public void initializeParameter(int wikiDocs, String wikiIndexDir){
		this.wikiDocs = wikiDocs;
		this.wikiIndexDir = wikiIndexDir;
	}
	
	/*
	 * Calculate phrase's similarity matrix.
	 */
	public double[][] calculateSimilarityMatrix(String[] termArray) throws Exception {
		double[][] similarityMatrix = new double[termArray.length][termArray.length];
		ArrayList<double[]> termFeatureVectorList = getTermFeatureVector(termArray);
		
		/* Lucene index. */
		Directory directory = featureVectorIndex(termFeatureVectorList);
		IndexReader ireader = IndexReader.open(directory);
		IndexSearcher iSearcher = new IndexSearcher(ireader);
		int queryMaximize = wikiDocs;
		
		for (int i = 0; i < termFeatureVectorList.size(); i++) {
			for (int j = i; j < termFeatureVectorList.size(); j++) {
				if (i == j)
					similarityMatrix[i][j] = 1;
				else {
					similarityMatrix[i][j] = similarityMatrix[j][i] = cosine_similarityOptimized(
							iSearcher, i, j, queryMaximize, termFeatureVectorList);
				}
			}
		}
		directory.close();
		
		return similarityMatrix;
	}

}
