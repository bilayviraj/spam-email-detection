package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException,IOException{

         double[][] tfeatures = new double[60][36];
        double[][] tsfeatures = new double[50][36];

        int trh,trs,tsh,tss;

        File dir1 = new File("training/ham");
        File dir2 = new File("training/spam");
        File dir3 = new File("testing/ham");
        File dir4 = new File("testing/spam");
        File[] listoffiles1 = dir1.listFiles();
        File[] listoffiles2 = dir2.listFiles();
        File[] listoffiles3 = dir3.listFiles();
        File[] listoffiles4 = dir4.listFiles();
        trh = listoffiles1.length;
        trs = listoffiles2.length;
        tsh = listoffiles3.length;
        tss = listoffiles4.length;

        double[][] x = new double[trh+trs][50];
        double[][] xt = new double[tsh+tss][50];

        //Training Phase--------
//        GetFeatures.TrainSpam(tfeatures);
        GetFeatures.GetHamList();
        GetFeatures.PrintHamList();
        GetFeatures.GetSpamList();
        GetFeatures.PrintSpamList();
        GetFeatures.FilterWords();
        GetFeatures.TrainSpam(x);
        GetFeatures.TrainHam(x,trs);
        GetFeatures.PercentageValues(x);
        GetFeatures.PrintNewDataset(x);
//        GetFeatures.PercentageValues(tfeatures);
//        GetFeatures.PrintDataset();
//        GetFeatures.CreateTrainingDataset(tfeatures);
        Perceptron.TestValues(x);
        //Testing Phase------
        GetFeatures.TestSpam(xt);
        GetFeatures.TestHam(xt,tss);
        GetFeatures.PercentageValues(xt);
//        GetFeatures.shuffleDataset(xt);
//        Perceptron.TestValues(xt);
        Perceptron.TestingPerceptron(xt,tsh+tss);
//        GetFeatures.Err();
//        GetFeatures.ResetFeature();
//        GetFeatures.TestSpam(tsfeatures);
//        GetFeatures.TestHam(tsfeatures);
//        GetFeatures.PercentageValues(tsfeatures);
//        Perceptron.TestingPerceptron(tsfeatures);
//        GetFeatures.CreateTestingDataset(tsfeatures);
    }
}
