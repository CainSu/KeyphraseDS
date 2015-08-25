package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataAccesser {

	public static double[][] getDoulbeMatrixFromFile(String path) {
		String[] sentences = FileUtil.getLines(path);
		double[][] matrix = new double[sentences.length][];
		for (int i = 0; i < sentences.length; i++) {
			String[] terms = sentences[i].trim().split(" ");
			matrix[i] = new double[terms.length];
			for (int j = 0; j < terms.length; j++) {
				matrix[i][j] = Double.valueOf(terms[j].trim());
			}
		}
		return matrix;
	}

	public static int[][] getIntMatrixFromFile(String path) {
		String[] sentences = FileUtil.getLines(path);
		int[][] matrix = new int[sentences.length][];
		for (int i = 0; i < sentences.length; i++) {
			String[] terms = sentences[i].split(" ");
			matrix[i] = new int[terms.length];
			for (int j = 0; j < terms.length; j++) {
				matrix[i][j] = Integer.valueOf(terms[j]);
			}
		}
		return matrix;
	}

	public static double[] getDoubleVectorFromFile(String path) {
		String[] sentences = FileUtil.getLines(path);
		double[] vector = new double[sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			if(sentences[i].equals("Inf"))
				vector[i] = 0.5;
			else
				vector[i] = Double.valueOf(sentences[i]);
		}
		return vector;
	}

	public static int[] getIntVectorFromFile(String path) {
		String[] sentences = FileUtil.getLines(path);
		int[] vector = new int[sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			if(sentences[i].equals("NaN"))
				vector[i] = 1;
			else
				vector[i] = Integer.valueOf(sentences[i]);
		}
		return vector;
	}
	
	public static ArrayList<int[]> getIndexTermArrayFromFile(String indexTermArrayFilePath){
		ArrayList<int[]> indexTermArray = new ArrayList<int[]>();
		String[] lines = FileUtil.getLines(indexTermArrayFilePath);
		for(String line : lines){
			String[] terms = line.trim().split("\t");
			int[] termIntegers = new int[terms.length];
			for(int i = 0; i < terms.length; i++){
				termIntegers[i] = Integer.valueOf(terms[i]);
			}
			indexTermArray.add(termIntegers);
		}
		return indexTermArray;
	}

	public static void saveMatrix2File(double[][] matrix, String filePath)
			throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < matrix.length; i++) {
			String writeLine = String.valueOf(matrix[i][0]);
			for (int j = 1; j < matrix[i].length; j++) {
				writeLine += " " + matrix[i][j];
			}
			bWriter.write(writeLine);
			bWriter.newLine();
			bWriter.flush();
		}
		bWriter.flush();
		bWriter.close();
	}

	public static void saveMatrix2File(int[][] matrix, String filePath)
			throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < matrix.length; i++) {
			String writeLine = String.valueOf(matrix[i][0]);
			for (int j = 1; j < matrix[i].length; j++) {
				writeLine += " " + matrix[i][j];
			}
			bWriter.write(writeLine);
			bWriter.newLine();
			bWriter.flush();
		}
		bWriter.flush();
		bWriter.close();
	}

	public static void saveVector2File(double[] vector, String filePath)
			throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < vector.length; i++) {
			bWriter.write(String.valueOf(vector[i]));
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();
	}

	public static void saveVector2File(int[] vector, String filePath)
			throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < vector.length; i++) {
			bWriter.write(String.valueOf(vector[i]));
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();
	}

	public static void saveVocabularyList2File(List<String> vocabularyList,
			String filePath) throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for(int i = 0; i < vocabularyList.size(); i++){
			bWriter.write(vocabularyList.get(i));
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();
	}
	
	public static void saveIndexTermArray2File(ArrayList<ArrayList<Integer>> indexTermArray, String filePath) throws IOException{
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		for(int i = 0; i < indexTermArray.size(); i++){
			String line = "";
			for(int j = 0; j < indexTermArray.get(i).size(); j++){
				line += indexTermArray.get(i).get(j) + "\t";
			}
			bWriter.write(line.trim());
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();
	}

}
