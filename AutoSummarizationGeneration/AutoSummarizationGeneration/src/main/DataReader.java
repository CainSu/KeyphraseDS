package main;

import java.util.ArrayList;
import util.FileDataAccesser;
import util.FileUtil;

public class DataReader {
	
	public int[][] featureVectorMatrix;
	public ArrayList<int[]> indexTermArray;
	public String[] sentenceArray;
	public String[] vocabularyArray;
	public String[] phraseArray;
	public int[][] sentence2phraseOccurrenceMatrix;
	
	public int[][] getFeatureVectorMatrix() {
		return featureVectorMatrix;
	}
	
	public String[] getSentenceArray() {
		return sentenceArray;
	}
	
	public String[] getVocabularyArray() {
		return vocabularyArray;
	}
	
	public String[] getPhraseArray() {
		return phraseArray;
	}
	
	public int[][] getSentence2phraseOccurrenceMatrix() {
		return sentence2phraseOccurrenceMatrix;
	}
	
	
	public ArrayList<int[]> getIndexTermArray() {
		return indexTermArray;
	}
	
	/*
	 * Read basic data.
	 * Initialzie:
	 * 		this.sentenceArray
	 * 		this.vocabularyArray
	 * 		this.featureVectorMatrix
	 * 		this.indexTermArray
	 */
	public void readSentenceAndFeatures(String dataFilePath, String vocabularyFilePath, String featureVectorMatrixFilePath, String indexTermArrayFilePath){
		// Read original sentences as String array.
		this.sentenceArray = FileUtil.getLines(dataFilePath);
		this.vocabularyArray = FileUtil.getLines(vocabularyFilePath);
		this.featureVectorMatrix = FileDataAccesser.getIntMatrixFromFile(featureVectorMatrixFilePath);
		this.indexTermArray = FileDataAccesser.getIndexTermArrayFromFile(indexTermArrayFilePath);
	}
	
	/*
	 * Read the phrase information.
	 * This method will call the trained CRF model to extract keyphrase.
	 * Initialize:
	 * 		this.phraseArray
	 * 		this.sentence2phraseOccurrenceMatrix
	 */
	public void readPhraseFromSentences(String phrasePath){
		this.phraseArray = FileUtil.getLines(phrasePath);
		this.sentence2phraseOccurrenceMatrix = new int[this.sentenceArray.length][this.phraseArray.length];
		for(int i = 0; i < this.sentenceArray.length; i++){
			String sentence = this.sentenceArray[i];
			for(int j = 0; j < this.phraseArray.length; j++){
				if(sentence.contains(this.phraseArray[j])){
					this.sentence2phraseOccurrenceMatrix[i][j] = 1;
				}else{
					this.sentence2phraseOccurrenceMatrix[i][j] = 0;
				}
			}
		}
	}
	
}
