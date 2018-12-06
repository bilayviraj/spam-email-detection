package Methods;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTree {
    public enum DecisionTreeNodeType {
		FEATURE_NODE, OUTPUT_LEAF_NODE
	}
    
    File ent=new File("config/entropy.txt");
    File gain=new File("config/gain.txt");
  
    int fp=0,tp=0,fn=0,tn=0;
    
    public DTNode buildDecisionTree(Features features, Instances trainExamples,String[] outputLabels) throws FileNotFoundException {

        if(trainExamples.getExamples().size() ==0 || features.getFeatures().size() ==0)
            return null;
        
        Map<String, Integer> outputLabelCt = getOutputLabelDistribution(features.getFeatures(), trainExamples.getExamples(), outputLabels);
	int posCt = outputLabelCt.get(outputLabels[0]);
	int negCt = outputLabelCt.get(outputLabels[1]);
                
	String outputLabel = outputLabels[1];
	if(posCt > negCt)
            outputLabel = outputLabels[0];
		
        if(posCt == 0) {
			DTNode node = new DTNode();
			node.setNodeType(DecisionTreeNodeType.OUTPUT_LEAF_NODE);
			node.setOutputLabel(outputLabel);
			return node;
		}
        
        if(negCt == 0) {
			DTNode node = new DTNode();
			node.setNodeType(DecisionTreeNodeType.OUTPUT_LEAF_NODE);
			node.setOutputLabel(outputLabel);
			return node;
		}
        
        Feature bestFeature = getBestFeature(features.getFeatures(), trainExamples.getExamples(), outputLabels, outputLabelCt);
	Features remainingFtrs = getRemainingFtrs(features.getFeatures(), bestFeature);
        
        // build a node based on the best feature
	DTNode root = new DTNode();
	root.setNodeType(DecisionTreeNodeType.FEATURE_NODE);
	root.setFeature(bestFeature);

	// traverse left side and construct
	Instances filteredExOnBestFtr = getFilteredExamplesOnBestFtr(trainExamples.getExamples(), bestFeature, bestFeature.getfOptions()[0]);
	root.setLeftNode(buildDecisionTree(remainingFtrs, filteredExOnBestFtr, outputLabels));

	// traverse right side and construct
	filteredExOnBestFtr = getFilteredExamplesOnBestFtr(trainExamples.getExamples(), bestFeature, bestFeature.getfOptions()[1]);
	root.setRightNode(buildDecisionTree(remainingFtrs, filteredExOnBestFtr, outputLabels));

	// return root of the tree
	return root;
    }

    private Instances getFilteredExamplesOnBestFtr(List<Instance> examples,Feature bestFeature, String ftrValue) {
		Instances ins = new Instances();
		List<Instance> newInstances = new ArrayList<Instance>();

		for(Instance i: examples) {
			String[] ftrValues = i.getUserFeatures();

			if(ftrValues[bestFeature.getIdx()].equalsIgnoreCase(ftrValue)) {
				newInstances.add(i);
			}

		}
		ins.setExamples(newInstances);
		return ins;
	}
    
    private Features getRemainingFtrs(List<Feature> features,Feature bestFeature) {
		List<Feature> remFtrs = new ArrayList<Feature>();

		for(Feature f: features) {
			if(!f.getfName().equalsIgnoreCase(bestFeature.getfName())) {
				remFtrs.add(f);
			}
		}
		Features fs = new Features();
		fs.setFeatures(remFtrs);
		return fs;
	}

    private Feature getBestFeature(List<Feature> features,List<Instance> examples, String[] outputLabels,Map<String, Integer> outputLabelCt) throws FileNotFoundException {
		
                PrintWriter print_ent=new PrintWriter(ent);
                PrintWriter print_gain=new PrintWriter(gain);
		double outputLabelEntropy = Entropy.getEntropy(outputLabelCt.get(outputLabels[0]), outputLabelCt.get(outputLabels[1]));
		
		int totOutputCt = outputLabelCt.get(outputLabels[0])+outputLabelCt.get(outputLabels[1]);
                double maxInfoGain = 0;
		Feature bestFeature = null;

                for(Feature currentFeature: features) {
			Map<String, Integer> featureOption1ValueDist = getFeatureValueDistribution(currentFeature,currentFeature.getfOptions()[0], currentFeature.getIdx(), examples, outputLabels);

			double featureOption1Entropy = Entropy.getOutputEntropyOverFeature(totOutputCt, featureOption1ValueDist.get(outputLabels[0]),featureOption1ValueDist.get(outputLabels[1]));
			
			Map<String, Integer> featureOption2ValueDist = getFeatureValueDistribution(currentFeature,currentFeature.getfOptions()[1], currentFeature.getIdx(), examples, outputLabels);
			double featureOption2Entropy = Entropy.getOutputEntropyOverFeature(totOutputCt, featureOption2ValueDist.get(outputLabels[0]),
					featureOption2ValueDist.get(outputLabels[1]));

			double featureToOutputEntropy = featureOption1Entropy + featureOption2Entropy;
                        
			print_ent.write("\nFeature: "+currentFeature+" , Entropy= "+featureToOutputEntropy);
                        
                        //CHANGES TO BE MADE HERE
			//CHANGES TO BE MADE HERE
                        //CHANGES TO BE MADE HERE
                        double infoGain = outputLabelEntropy-featureToOutputEntropy;
			print_gain.write("\n"+infoGain);

			if(infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				bestFeature = currentFeature;
			}
		}
		return bestFeature;
	}
    
    private Map<String, Integer> getFeatureValueDistribution(Feature feature, String featureOptionValue, int ftrIdx, List<Instance> examples, String[] outputLabels) {
		Map<String, Integer> outputLabelCt = new HashMap<String, Integer>();
		outputLabelCt.put(outputLabels[0], 0);
		outputLabelCt.put(outputLabels[1], 0);

		for(Instance example: examples) {
			String[] userFeaturesArr = example.getUserFeatures();
			String fatureValue = userFeaturesArr[ftrIdx];
			if(featureOptionValue.equalsIgnoreCase(fatureValue)) {
				if(example.getOutputIndicator().equalsIgnoreCase(outputLabels[0])) {
					int ct = outputLabelCt.get(outputLabels[0])+1;
					outputLabelCt.put(outputLabels[0], ct);     
				} else {
					int ct = outputLabelCt.get(outputLabels[1])+1;
					outputLabelCt.put(outputLabels[1], ct);
                                }
			}
		}
		return outputLabelCt;
	}
    
    
    public String validateExample(Instance example, DTNode root) {
		String dtOutputLabel = "";
		while(root != null) {
			String[] exFtrValues = example.getUserFeatures();
			Feature dtFtr = root.getFeature();
			
			if(dtFtr != null) {
				String[] dtFtrValues = dtFtr.getfOptions();
				String exFtrValue = exFtrValues[dtFtr.getIdx()];
				
				if(exFtrValue.equals(dtFtrValues[0])) {
					root = root.getLeftNode();
				} else {
					root = root.getRightNode();
				}
				
				if(root != null) {
					dtOutputLabel = root.getOutputLabel();
				}
			} else {
				root = root.getLeftNode();
			}
		}
		return dtOutputLabel;
	}
    

	public Map<String, Integer> getOutputLabelDistribution(List<Feature> features, List<Instance> instances, String[] outputLabels) {

		Map<String, Integer> outputLabelCt = new HashMap<String, Integer>();
		outputLabelCt.put(outputLabels[0], 0);
		outputLabelCt.put(outputLabels[1], 0);

		for(Instance inst: instances) {
			if(inst.getOutputIndicator().equalsIgnoreCase(outputLabels[0])) {
				int ct = outputLabelCt.get(outputLabels[0])+1;
				outputLabelCt.put(outputLabels[0], ct);
			} else {
				int ct = outputLabelCt.get(outputLabels[1])+1;
				outputLabelCt.put(outputLabels[1], ct);
			}
		}
		return outputLabelCt;
	}

    public Instances testDecisionTree(DTNode root, Instances testData) {
		List<Instance> testExamples = testData.getExamples();
		List<Instance> failedExamples = new ArrayList<Instance>();
		for(Instance example: testExamples) {
			String currentOutputLabel = example.getOutputIndicator();
			String dtOutputLabel = validateExample(example, root);
			
                        if(currentOutputLabel.equalsIgnoreCase("spam") && dtOutputLabel.equalsIgnoreCase("spam"))
                            tp++;
                        
                        if(currentOutputLabel.equalsIgnoreCase("spam") && dtOutputLabel.equalsIgnoreCase("ham"))
                            fp++;
                        
                        if(currentOutputLabel.equalsIgnoreCase("ham") && dtOutputLabel.equalsIgnoreCase("ham"))
                            tn++;
                        
                        if(currentOutputLabel.equalsIgnoreCase("ham") && dtOutputLabel.equalsIgnoreCase("spam"))
                            fn++;
                        
			if(!dtOutputLabel.equalsIgnoreCase(currentOutputLabel)) {
				failedExamples.add(example);
			}
		}
                
                Instances is = new Instances();
		is.setExamples(failedExamples);
		
		return is;
	}
    
    public void calculateValues(){
                double recall=(double)tp/(double)(tp+fn);
                double precision=(double)tp/(double)(tp+fp);
                System.out.println("");
		System.out.println("Recall= "+recall);
                System.out.println("FP Rate= "+(double)(fp/(double)(fp+tn)));
                System.out.println("Precision= "+precision);
		System.out.println("F-measure= "+(double)((2*precision*recall)/(precision+recall)));
                System.out.println("Accuracy= "+((double)(tp+tn)/(double)(tp+tn+fp+fn)));
    }
}
