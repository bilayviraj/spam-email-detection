package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by VIRAJ on 11-10-2016.
 */
public class Perceptron extends GetFeatures{

    static double[] w = new double[50];
    static double[] yt;
    static double b=0,alpha=0.01,yin,y;
    static int match,iter=0;
    static boolean IsBreak = true;

    public static void TestValues(double[][] x){
        while(IsBreak){
//            match=0;
//            iter++;
//            System.out.println("Iterations: "+iter);
            for(double p[]:x)
                CalOutput(p);
        }
//        System.out.println("\nIterations: "+iter);
        System.out.println("Final bias value: "+b);
        System.out.println("Final weights: ");
        for(double p :w)
            System.out.print(p+"\t");
//        System.out.println("\nMatch: "+match);
    }

    public static void CalOutput(double[] x){
             yin=0.0;
            for(int i=1;i<49;i++)
                yin += x[i] * w[i];
            yin+=b;
//        System.out.print("------------------Yin = "+yin);
            y= (yin>=1.9) ? 1:0;                              //Activation Function
            if(y==x[0]) {
                match++;
                IsBreak=false;
            }
            else{
                ChangeWeight(x);
                ChangeBias(x[0]);
            }
//        System.out.printf("\nYin = %.3f ",yin);
//        System.out.println("\nY = "+ y);
//        System.out.println("T = "+x[0]);
//        System.out.println("Total Match = "+match);
    }


    public static void ChangeWeight(double[] x){
        for(int i=1;i<49;i++) {
            w[i] += alpha * x[0] * x[i];
        }
    }

    public static void ChangeBias(double t){
        b+=alpha*t;
    }

    public static void TestingPerceptron(double[][] x,int ts) throws FileNotFoundException{
//        System.out.println("\n---------+++++++TESTING+++++++----------------");
        yt = new double[ts];
        int temp=0,accu=0;
        double tp=0,tn=0,fp=0,fn=0;
        for(double p[]:x){
            yin=0.0;
            for(int i=1;i<49;i++) {
                yin += p[i] * w[i];
            }
            yin+=b;
//            System.out.println("Yin: "+ yin);
            y= (yin>=1.9) ? 1:0;                                             //Activation Function
//            System.out.println("Y: "+ y);
            yt[temp]=y;
            temp++;
        }
//        System.out.println("Temp is: "+temp);
        File file= new File("StoppingCondition.txt");
        PrintWriter writer=new PrintWriter(file);
        writer.println("Calculated output is: ");
        for(double p:yt)
            writer.print(p+"\t");
        writer.println();
        writer.println("Target output is: ");
        for(double p[]:x)
            writer.print(p[0]+"\t");
        writer.close();
        System.out.println("Total number of emails for testing: "+c);

        int co=0;
            for(double p[]:x){
            if(p[0] == 1 && yt[co] == 0){
                fp++;
            }
            else  if(p[0] == 0 &&  yt[co] == 1){
                fn++;
            }
            else  if(p[0] == 1 && yt[co] == 1){
                tp++;
            }
            else if(p[0] == 0 && yt[co] == 0){
                tn++;
            }
            co++;
            }

        System.out.println("\nResults:-");
        double sum = (tp+tn)/(tp+tn+fp+fn);
        System.out.println("Accuracy: "+ sum);
        double recall = tp/(tp+fn);
        System.out.println("Recall: "+ recall);
        System.out.println("FP Rate: "+ (fp/(fp+tn)));
        double prec = tp/(tp+fp);
        System.out.println("Precision: "+ prec);
        System.out.println("F-Measure: "+ 2*(prec*recall)/(prec+recall));

//        System.out.println("tp = "+ tp);
//        System.out.println("tn = "+ tn);
//        System.out.println("fp = "+ fp);
//        System.out.println("fn = "+ fn);
    }
}
