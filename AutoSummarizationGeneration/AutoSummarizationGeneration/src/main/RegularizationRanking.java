package main;

import com.mathworks.toolbox.javabuilder.*;
import RankingPhrase.Regularization;

public class RegularizationRanking {
	/*
	 * Call the matlab program: RegularizationRankingOnKeyPhraseGraph.
	 */
	public void run(String edge_weight_path, String initialScorePath, String savePath, double u) throws MWException{
		Regularization rP = new Regularization();
		rP.RegularizationRankingOnKeyPhraseGraph(edge_weight_path,initialScorePath, savePath,u);
	}
}
