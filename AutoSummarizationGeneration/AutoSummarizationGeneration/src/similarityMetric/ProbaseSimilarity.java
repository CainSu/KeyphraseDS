package similarityMetric;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

public class ProbaseSimilarity {

	public double[][] computePhraseSimilarityMatrix(ProbaseAccess pA,
			String[] phraseArray) throws SQLException, IOException {
		Connection conn = pA.getConnectionMySql();
		/* Get each phrase's concept list, and the concept vocabulary. */
		ArrayList<String> conceptVoc = new ArrayList<String>();
		ArrayList<ArrayList<String[]>> conceptList = new ArrayList<ArrayList<String[]>>();
		for (String phrase : phraseArray) {
			ArrayList<String[]> instance = pA.getConceptByEntity(conn, phrase);
			conceptList.add(instance);

			for (int i = 0; i < instance.size(); i++) {
				String[] rs = instance.get(i);
				if (!conceptVoc.contains(rs[0]))
					conceptVoc.add(rs[0]);
			}
		}
		conn.close();

		System.out.println("Begin to get concept vector...");
		/* Get each phrase's concept vector. */
		ArrayList<double[]> conceptVectors = new ArrayList<double[]>();
		for (int i = 0; i < conceptList.size(); i++) {
			double[] vector = new double[conceptVoc.size()];
			for (int j = 0; j < vector.length; j++)
				vector[j] = 0;

			ArrayList<String[]> instance = conceptList.get(i);
			if (instance.size() == 0) {
				conceptVectors.add(vector);
				continue;
			}
			double sum = 0;
			for (String[] rs : instance) {
				String concept = rs[0];
				String frequency = rs[2];
				vector[conceptVoc.indexOf(concept)] = Double.valueOf(frequency);
				sum += Double.valueOf(frequency);
			}
			/* Normalize the vector. */
			for (int j = 0; j < vector.length; j++)
				vector[j] /= sum;
			conceptVectors.add(vector);
		}

		System.out.println("Begin to get phrase-phrase similarity matrix...");
		/* Lucene index. */
		Directory directory = featureVectorIndex(conceptVectors);
		IndexReader ireader = IndexReader.open(directory);
		IndexSearcher iSearcher = new IndexSearcher(ireader);
		int queryMaximize = conceptVoc.size();
		/* Compute the phrase-phrase similarity matrix. */
		double[][] similarityMatrix = new double[phraseArray.length][phraseArray.length];
		for (int i = 0; i < conceptVectors.size(); i++) {
			for (int j = 0; j <= i; j++) {
				if (i == j)
					similarityMatrix[i][j] = 1;
				else {
					double simialrity = cosine_similarityOptimized(iSearcher,
							i, j, queryMaximize, conceptVectors);
					similarityMatrix[i][j] = similarityMatrix[j][i] = simialrity;
				}
			}
		}
		return similarityMatrix;
	}

	private Directory featureVectorIndex(
			ArrayList<double[]> termFeatureVectorList)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {
		// LuceneIndex Writer.
		Analyzer simple = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriter indexWriter = new IndexWriter(directory, simple, true,
				IndexWriter.MaxFieldLength.UNLIMITED);
		int rows = termFeatureVectorList.size();
		int columns = termFeatureVectorList.get(0).length;
		for (int j = 0; j < columns; j++) {
			String termName = String.valueOf(j);
			String itemList = "";
			for (int i = 0; i < rows; i++) {
				if (termFeatureVectorList.get(i)[j] != 0) {
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

	private double cosine_similarityOptimized(IndexSearcher iSearcher,
			int itemA, int itemB, int queryMaximize,
			ArrayList<double[]> termFeatureVectorList) throws IOException {
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
		if (topDocs.totalHits <= 0)
			return 0;
		double sum = 0;
		double len1 = 0;
		double len2 = 0;
		for (int i = 0; i < topDocs.totalHits; i++) {
			Document targetDoc = iSearcher.doc(scoreDocs[i].doc);
			int termID = Integer.valueOf(targetDoc.get("term"));
			double scoreA = termFeatureVectorList.get(itemA)[termID];
			double scoreB = termFeatureVectorList.get(itemB)[termID];
			sum += scoreA * scoreB;
			len1 += scoreA * scoreA;
			len2 += scoreB * scoreB;
		}
		return sum / (Math.sqrt(len1) * Math.sqrt(len2));
	}

}
