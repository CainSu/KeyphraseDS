package main;

import com.mathworks.toolbox.javabuilder.*; 
import SpectraClustering.Class1;

public class SpectraClustering {
	/*
	 * Call the matlab program: SpectralClustering.
	 */
	public void run(String affinityMatrixPath, int clusterNum, String savePath) throws MWException{
		Class1 sp = new Class1();
		sp.SpectralClusteringILP(affinityMatrixPath, clusterNum, savePath);
	}
}
