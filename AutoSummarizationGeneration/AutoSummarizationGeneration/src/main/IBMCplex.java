package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import ilog.concert.*;
import ilog.cplex.*;

public class IBMCplex {

	/**
	 * Optimize the objective function with respect to each section.
	 * 
	 * @param sectionCandidateSentence
	 * @param sectionCandidatePhrase
	 * @param section2SentenceRelevanceMatrix
	 * @param sentenceDiversityArray
	 * @param sentence2phraseOccurrenceMatrix
	 * @param phraseRankingScore
	 * @param sentence
	 * @param summaryLength
	 * @param lamda1
	 * @param lamda2
	 * @param lamda3
	 * @param sectionResultPath
	 * @param globalResultPath
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static int[] sectionSpecificOptimizer(
			ArrayList<int[]> sectionCandidateSentence,
			ArrayList<int[]> sectionCandidatePhrase,
			double[][] section2SentenceRelevanceMatrix,
			double[] sentenceDiversityArray,
			int[][] sentence2phraseOccurrenceMatrix,
			double[] phraseRankingScore, String[] sentence, int summaryLength,
			double lamda1, double lamda2, double lamda3,
			String sectionResultPath, String globalResultPath)
			throws NumberFormatException, IOException {

		int[] sentenceLength = new int[sentence.length];
		for (int i = 0; i < sentence.length; i++) {
			sentenceLength[i] = sentence[i].trim().split(" ").length;
		}

		/* the final sentence's label: selected or not. */
		int[] sentenceLabel = new int[sentence.length];
		for (int i = 0; i < sentence.length; i++)
			sentenceLabel[i] = 0;

		int sectionSize = section2SentenceRelevanceMatrix.length;// The number
																	// of
																	// sections.

		// Section-by-section optimization
		for (int i = 0; i < sectionSize; i++) {
			int sectionLength = summaryLength / sectionSize;// Each section's
															// constrained
															// length.

			int[] candidateSentence = sectionCandidateSentence.get(i);
			int[] candidatePhrase = sectionCandidatePhrase.get(i);

			int variableSize = candidateSentence.length
					+ candidatePhrase.length;

			double[] variableCoefficient = new double[variableSize];
			int index = 0;
			/* sentence part coefficient */
			double sumA = 0;
			double[] tmpA = new double[candidateSentence.length];
			double sumB = 0;
			double[] tmpB = new double[candidateSentence.length];
			// Normalize
			for (int j = 0; j < candidateSentence.length; j++) {
				tmpA[j] = section2SentenceRelevanceMatrix[i][candidateSentence[j]];
				sumA += tmpA[j];
				tmpB[j] = sentenceDiversityArray[candidateSentence[j]];
				sumB += tmpB[j];
			}
			for (int j = 0; j < candidateSentence.length; j++) {
				variableCoefficient[index] = lamda1 * (tmpA[j] / sumA) + lamda2
						* (tmpB[j] / sumB);
				index++;
			}

			/* phrase part coefficient */
			sumA = 0;
			tmpA = new double[candidatePhrase.length];
			// Normalize
			for (int p = 0; p < candidatePhrase.length; p++) {
				tmpA[p] = phraseRankingScore[candidatePhrase[p]];
				sumA += tmpA[p];
			}
			for (int p = 0; p < candidatePhrase.length; p++) {
				variableCoefficient[index] = lamda3 * (tmpA[p] / sumA);
				index++;
			}

			/*
			 * Constraint.
			 */
			ArrayList<double[]> constraint = new ArrayList<double[]>();
			/*
			 * ConstraintOne
			 */
			double[] constraintOne = new double[variableSize];
			for (index = 0; index < variableSize; index++)
				constraintOne[index] = 0;
			for (int j = 0; j < candidateSentence.length; j++) {
				constraintOne[j] = sentenceLength[candidateSentence[j]];
			}
			/*
			 * ConstraintTwo
			 */
			int nonzeroCount = 0;
			for (int j = 0; j < candidateSentence.length; j++) {
				for (int p = 0; p < candidatePhrase.length; p++) {
					if (sentence2phraseOccurrenceMatrix[candidateSentence[j]][candidatePhrase[p]] == 1)
						nonzeroCount++;
				}
			}
			double[][] constraintTwo = new double[nonzeroCount][variableSize];
			for (int indexA = 0; indexA < nonzeroCount; indexA++) {
				for (int indexB = 0; indexB < variableSize; indexB++)
					constraintTwo[indexA][indexB] = 0;
			}
			int tmp = 0;
			for (int j = 0; j < candidateSentence.length; j++) {
				for (int p = 0; p < candidatePhrase.length; p++) {
					if (sentence2phraseOccurrenceMatrix[candidateSentence[j]][candidatePhrase[p]] == 1) {
						constraintTwo[tmp][j] = 1;
						constraintTwo[tmp][candidateSentence.length + p] = -1;
						tmp++;
					}
				}
			}
			/*
			 * ConstraintThree
			 */
			double[][] constraintThree = new double[candidatePhrase.length][variableSize];
			for (int p = 0; p < candidatePhrase.length; p++) {
				for (index = 0; index < variableSize; index++)
					constraintThree[p][index] = 0;
			}
			for (int p = 0; p < candidatePhrase.length; p++) {
				for (int j = 0; j < candidateSentence.length; j++) {
					constraintThree[p][j] = sentence2phraseOccurrenceMatrix[candidateSentence[j]][candidatePhrase[p]];
				}
				constraintThree[p][candidateSentence.length + p] = -1;
			}
			// Put the constraint to the ConstaintList.
			constraint.add(constraintOne);
			for (int j = 0; j < constraintTwo.length; j++) {
				constraint.add(constraintTwo[j]);
			}
			for (int j = 0; j < constraintThree.length; j++) {
				constraint.add(constraintThree[j]);
			}

			/* execute the IBMCplex. */
			double[] result = execute(variableCoefficient, constraint,
					variableSize, sectionLength, nonzeroCount,
					candidatePhrase.length);
			if (result == null) {
				//Cannot obtain the optimal solution. Random Select.
				result = new double[candidateSentence.length];
				int count = 0;
				for(int s = 0; s < candidateSentence.length; s++){
					if((count + sentence[candidateSentence[s]].split(" ").length) < sectionSize){
						result[s] = 1;
						count += sentence[candidateSentence[s]].split(" ").length;
					}
				}
			} else {
				/* Save the results to files. */
				// sentence-selection result
				BufferedWriter bWriter = new BufferedWriter(new FileWriter(
						new File(sectionResultPath), true));
				for (int r = 0; r < candidateSentence.length; r++) {
					if (result[r] == 1) {
						bWriter.write(String.valueOf(candidateSentence[r])
								+ " ");
						sentenceLabel[candidateSentence[r]] = 1;
					}
				}
				bWriter.newLine();
				bWriter.flush();
				bWriter.close();
			}
		}
		//Re-check the global sentence label's size.
		ArrayList<Integer> selectedLabels = new ArrayList<Integer>();
		int hasSelectedSize = 0;
		for(int i = 0; i < sentence.length; i++){
			if(sentenceLabel[i] == 1){
				selectedLabels.add(i);
				hasSelectedSize += sentence[i].split(" ").length;
			}
		}
		int iterations = sentence.length;
		while(iterations > 0){
			int index = (int) (Math.random()*sentence.length);
			if(selectedLabels.contains(index) || sentence[index].split(" ").length <15 || sentence[index].split(" ").length > 50)
				continue;
			if((hasSelectedSize + sentence[index].split(" ").length) < summaryLength){
				sentenceLabel[index] = 1;
				hasSelectedSize += sentence[index].split(" ").length;
			}
			iterations--;
		}
		
		/* Save the global sentence label to files. */
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(
				globalResultPath)));
		for (int i = 0; i < sentence.length; i++) {
			bWriter.write(String.valueOf(sentenceLabel[i]));
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();

		return sentenceLabel;
	}

	/**
	 * 
	 * @param variableCoefficient
	 * @param constraint
	 * @param vairiableSize
	 * @param summarySize
	 * @param constraintTwoSize
	 * @param constraintThreeSize
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static double[] execute(double[] variableCoefficient,
			ArrayList<double[]> constraint, int vairiableSize, int summarySize,
			int constraintTwoSize, int constraintThreeSize)
			throws NumberFormatException, IOException {
		try {
			IloCplex cplex = new IloCplex();
			/* variable bound */
			int[] low = new int[vairiableSize];
			int[] upper = new int[vairiableSize];
			for (int i = 0; i < vairiableSize; i++) {
				low[i] = 0;
				upper[i] = 1;
			}
			// variable
			IloNumVar[] x = cplex.intVarArray(vairiableSize, low, upper);
			IloLinearNumExpr expr = cplex.scalProd(x, variableCoefficient);
			cplex.addMaximize(expr);

			// constraintOne
			cplex.addLe(cplex.scalProd(constraint.get(0), x), summarySize);
			// constraintTwo
			for (int i = 1; i < constraintTwoSize; i++) {
				cplex.addLe(cplex.scalProd(constraint.get(i), x), 0);
			}
			// constraintThree
			for (int i = constraintTwoSize; i < constraintThreeSize; i++) {
				cplex.addGe(cplex.scalProd(constraint.get(i), x), 0);
			}

			if (cplex.solve()) {
				double[] val = cplex.getValues(x);
				return val;
			}
			cplex.end();
		} catch (IloException e) {
			System.err.println("Concert exception '" + e + "' caught");
		}
		return null;
	}
}
