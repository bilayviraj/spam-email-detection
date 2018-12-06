package Methods;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        DataGenerator generate=new DataGenerator();
        generate.GetHamList();
        generate.GetSpamList();
        generate.PrintHamList();
        generate.PrintSpamList();
        generate.GetAllWords();
        generate.PrintDataset();
        
        File featuresFile = new File("config/features");
        File trainingSetFile = new File("config/training_set");
        File testingSetFile = new File("config/testing_set");
        
        System.out.println("");
        System.out.println("Loading Input....");
        
        DataLoader dl = new DataLoader();
	Features features = dl.loadFeatures(featuresFile);
	Instances trainExamples = dl.loadTrainData(trainingSetFile, features.getFeatures());
	Instances testExamples = dl.loadTestData(testingSetFile, features.getFeatures());
	String[] outputLabels = dl.loadOutputLabels(featuresFile);
        
        DecisionTree dt = new DecisionTree();
        System.out.println("");
        System.out.println("Constructing the decision tree....");
	DTNode root = dt.buildDecisionTree(features, trainExamples, outputLabels);
	dt.testDecisionTree(root, testExamples);
        dt.calculateValues();
    }
}
