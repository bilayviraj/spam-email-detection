/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods;

/**
 *
 * @author Utkarsh
 */
public class Entropy {
    public static double getEntropy(Integer positiveCt, Integer negativeCt) {
		
                // positive prob
		double totCt = positiveCt+negativeCt;
		double prob = (1.0 * positiveCt)/totCt;
		double entropy = -1 * (prob) * Math.log(prob)/Math.log(2);
		
		// negative prob
		prob = negativeCt/totCt;
		entropy += (-1 * (prob) * (Math.log(prob)/Math.log(2)));
		return entropy;
	}
	
	public static double getOutputEntropyOverFeature(int totOutputCt, int featurePosCt, int featureNegCt) {
		int totFeatureCt = featurePosCt+featureNegCt;
		double featureOption1Prob = 1.0 * totFeatureCt/totOutputCt;
		
		if(featureNegCt == 0 || featurePosCt == 0) {
			return 0;
		}
		
		double ftrOption1OutputLable1Prob = 1.0*featurePosCt/totFeatureCt;
		double ftrOption1OutputLable2Prob = 1.0*featureNegCt/totFeatureCt;
		double featureOption1Entropy = (-ftrOption1OutputLable1Prob * (Math.log(ftrOption1OutputLable1Prob)/Math.log(2)) 
                                		- (ftrOption1OutputLable2Prob * (Math.log(ftrOption1OutputLable2Prob)/Math.log(2))));
		
		return featureOption1Entropy * featureOption1Prob;
	}
}
