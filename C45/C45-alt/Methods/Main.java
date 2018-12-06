package Methods;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        DataGenerator generate=new DataGenerator();
        
        
        String[] train_ham=new String[4];
        String[] train_spam=new String[4];
        String[] test_ham=new String[4];
        String[] test_spam=new String[4];
        
        String[] training_set=new String[4];
        String[] testing_set=new String[4];
        
        File[] training_set_files=new File[4];
        File[] testing_set_files=new File[4];

        for(int i=0;i<4;i++){
            
           train_ham[i]="datasets/training/training"+""+(i+1)+""+"/ham"; 
           train_spam[i]="datasets/training/training"+""+(i+1)+""+"/spam";
           test_ham[i]="datasets/testing/testing"+""+(i+1)+""+"/ham";
           test_spam[i]="datasets/testing/testing"+""+(i+1)+""+"/spam";      
           training_set[i]="config/training_set"+""+(i+1)+"";
           testing_set[i]="config/testing_set"+""+(i+1)+"";
           
           generate.GetHamList(train_ham[i]);
           generate.GetSpamList(train_spam[i]);
           
           training_set_files[i]=new File(training_set[i]);
           testing_set_files[i]=new File(testing_set[i]);
        }
  
        
        generate.PrintHamList();
        generate.PrintSpamList();
        
        generate.GetAllWords();
        generate.PrintFeatures();
        
        
        for(int i=0;i<4;i++){
            generate.Print_training_set(train_ham[i], train_spam[i],training_set[i]);
            generate.Print_testing_set(test_ham[i], test_spam[i], testing_set[i]);
        }
        
        
        File featuresFile = new File("config/features");

        System.out.println("");
        System.out.println("Loading Input....");
        
        DataLoader dl = new DataLoader();
        DecisionTree dt = new DecisionTree();
        Lists_Values lv=new Lists_Values();
        
        Features features = dl.loadFeatures(featuresFile);
        String[] outputLabels = dl.loadOutputLabels(featuresFile);
      
        for(int i=0;i<4;i++){
        
            Instances trainExamples = dl.loadTrainData(training_set_files[i], features.getFeatures());
            Instances testExamples = dl.loadTestData(testing_set_files[i], features.getFeatures());
            DTNode root = dt.buildDecisionTree(features, trainExamples, outputLabels);
            dt.testDecisionTree(root, testExamples);
        }
        System.out.println();
        System.out.println("Successfully loaded training dataset of size="+lv.train_email_count);
        System.out.println("Successfully loaded testing dataset of size="+lv.test_email_count);
        System.out.println("Total= "+(lv.test_email_count+lv.train_email_count)+" emails");
        System.out.println("\n");
        System.out.println("Constructing the decision tree....");
        
        lv.getResults();
        
    }
}
