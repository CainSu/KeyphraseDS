package util;

import org.apache.commons.math3.stat.inference.TTest;

public class MathUtil {
	/*
	 * KL divergence.
	 */
	public static double computeKLdistance(double[] probabilityA,
			double[] probabilityB) {
		double klScore = 0.0;
		double pA = 0.0;
		double pB = 0.0;
		for (int i = 0; i < probabilityA.length; i++) {
			pA += probabilityA[i] * Math.log(probabilityA[i] / probabilityB[i]);
			pB += probabilityB[i] * Math.log(probabilityB[i] / probabilityA[i]);
		}
		klScore = (pA + pB) / 2;

		return klScore;
	}
	
	public static void normalizeMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			double SUM = 0;
			for(int j = 0; j < matrix[i].length; j++){
				SUM += matrix[i][j];
			}
			for(int j = 0; j < matrix[i].length; j++){
				if(SUM == 0)
					matrix[i][j] = 0;
				else
					matrix[i][j] = matrix[i][j] / SUM;
			}
		}
	}
	
	public static void normalizeVector(double[] vector){
		double SUM = 0;
		for(int i = 0; i < vector.length; i++){
			SUM += vector[i];
		}
		for(int i = 0; i < vector.length; i++){
			if(SUM == 0)
				vector[i] = 0;
			else
				vector[i] = vector[i] / SUM;
		}
	}
	
	public static void TTestStat(){
		double[] sample1 = { 1  , 2  , 3   ,4 , 3, 5, 6.1, 3.4, 2.9, 4.4};
		double[] sample2 = { 5.2, 4.2, 7.24,4 , 5, 6, 4.1, 5.9, 7.0, 8.0};
		double t_statistic;
		
		TTest ttest = new TTest();
		t_statistic = ttest.t(sample1, sample2);
		System.out.println("T_statistic: " + t_statistic);
	}
}
