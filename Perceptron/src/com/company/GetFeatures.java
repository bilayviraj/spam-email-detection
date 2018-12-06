package com.company;

import java.io.*;
import java.util.*;

/**
 * Created by VIRAJ on 11-10-2016.
 */
    class GetFeatures {
    static int c=0,d=0,in=1;

    static Set<String> hamlist = new HashSet<String>();
    static Set<String> spamlist = new HashSet<String>();
    static Set<String> filteredSpamlist = new HashSet<String>();

    static double[] e = new double[50];

    static void GetHamList() throws FileNotFoundException{
        File dir = new File("training/ham");
        File[] listoffiles = dir.listFiles();

        for(File file:listoffiles) {
            Scanner s = new Scanner(file);
            while(s.hasNext()){
                hamlist.add(s.next());
            }
            c++;
        }
    }

    static void GetSpamList() throws FileNotFoundException{

        File dir = new File("training/spam");
        File[] listoffiles = dir.listFiles();

        for(File file:listoffiles) {
            Scanner s = new Scanner(file);
            while(s.hasNext()){
                spamlist.add(s.next());
            }
            d++;
        }
    }

    static void TrainSpam(double[][] x) throws FileNotFoundException {
        File dir = new File("training/spam");
        File[] listoffiles = dir.listFiles();
        int fc=0,wc=0;
        for(File file:listoffiles) {
            x[fc][0]=1.0;
            Scanner s = new Scanner(file);
            String check;
            while(s.hasNext()){
                wc++;
                check=s.next();
                if(filteredSpamlist.contains(check)){
                    x[fc][in]++;
                    in++;
                    if(in==49)
                        in=1;
                }
                else{
                    in++;
                    if(in==49)
                        in=1;
                }
            }
            x[fc][49]=wc;
            if(wc==0)
                x[fc][49]=1.0;
            wc=0;
            fc++;
        }
    }

    static void TrainHam(double[][] x,int trs) throws FileNotFoundException{
        File dir = new File("training/ham");
        File[] listoffiles = dir.listFiles();
        int wc=0,fc=trs;
        for(File file:listoffiles) {
            x[fc][0]=0.0;
            Scanner s = new Scanner(file);
            String check;
            while(s.hasNext()){
                wc++;
                check=s.next();
                if(filteredSpamlist.contains(check)){
                    x[fc][in]+=1;
                    in++;
                    if(in==49)
                        in=1;
                }
                else{
                    in++;
                    if(in==49)
                        in=1;
                }

            }
            x[fc][49]=wc;
            if(wc==0)
                x[fc][49]=1.0;
            wc=0;
            fc++;
        }
    }

    static void PrintNewDataset(double[][] x)throws FileNotFoundException{
        File file = new File("NewDataset.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.println("Inputs are: ");
        for(double[] p:x) {
            for (double q : p)
                writer.printf(" %.3f  ",q);
            writer.println();
        }
        writer.close();
    }

    static void PrintDataset(double[][] features) {
        System.out.println("Total files are:" + c);
        System.out.println("Feature values: ");
        for (double p[] : features) {
            for (double q : p)
                System.out.printf(" %.3f",q);
            System.out.println();
        }
    }

    static void PercentageValues(double[][] x){
        for(double p[]:x){
            for(int i=1;i<p.length-2;i++)
                p[i] = p[i] * 100 / p[p.length - 1];
        }
    }

//    static void DefArrNeg(){
//        for(double p[]:features)
//            Arrays.fill(p,-1);
//    }

    static void CreateTrainingDataset(double[][] features) throws IOException{
        System.out.println("Creating Dataset...");
        File file = new File("TrainingDataset.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.println("Total files are:" + c);
        writer.println("Feature values: ");
        for (double p[] : features) {
            for (double q : p)
                writer.printf("    %.3f",q);
            writer.println();
        }
        writer.close();
        System.out.println("Total number of emails: "+c);
    }

    static void TestSpam(double[][] xt) throws FileNotFoundException {
        File dir = new File("testing/spam");
        File[] listoffiles = dir.listFiles();
        int fc=0,wc=0;
        for(File file:listoffiles) {
            xt[fc][0]=1.0;
            Scanner s = new Scanner(file);
            String check;
            while(s.hasNext()){
                wc++;
                check=s.next();
                if(filteredSpamlist.contains(check)){
                    xt[fc][in]++;
                    in++;
                    if(in==49)
                        in=1;
                }
                else{
                    in++;
                    if(in==49)
                        in=1;
                }

            }
            xt[fc][49]=wc;
            if(wc<=2)
                xt[fc][26]+=5;
            if(wc==0)
                xt[fc][49]=1.0;
            wc=0;
            fc++;
        }
    }

    static void TestHam(double[][] xt,int tss) throws FileNotFoundException{
        File dir = new File("testing/ham");
        File[] listoffiles = dir.listFiles();
        int wc=0,fc=tss;
        for(File file:listoffiles) {
            xt[fc][0]=0.0;
            Scanner s = new Scanner(file);
            String check;
            while(s.hasNext()){
                wc++;
                check=s.next();
                if(filteredSpamlist.contains(check)){
                    xt[fc][in]+=1;
                    in++;
                    if(in==49)
                        in=1;
                }
                else{
                    in++;
                    if(in==49)
                        in=1;
                }

            }
            xt[fc][49]=wc;
            if(wc==0)
                xt[fc][49]=1.0;
            wc=0;
            fc++;
        }
    }

    static void CreateTestingDataset(double[][] features) throws IOException{
        File file = new File("TestingDataset.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.println("Total files to test are:" + c);
        writer.println("Feature values: ");
        for (double p[] : features) {
            for (double q : p)
                writer.printf("    %.3f",q);
            writer.println();
        }
        writer.close();
    }

    static void PrintHamList(){
        System.out.println("-----------HAM-------------");
        System.out.println(hamlist);
        System.out.println("Total Emails :"+c);
        System.out.println("Total Words :"+hamlist.size());
    }

    static void PrintSpamList(){
        System.out.println("++++++++++SPAM+++++++++++++");
        System.out.println(spamlist);
        System.out.println("Total Emails :"+d);
        System.out.println("Total Words :"+spamlist.size());
    }

    static void FilterWords() throws FileNotFoundException{
        for(String s1:spamlist){
            if(!hamlist.contains(s1))
                filteredSpamlist.add(s1);
        }
        filteredSpamlist.add("$");
        filteredSpamlist.add("!");

        System.out.println("++++++++++-------Filtered Words-------++++++++");
        System.out.println(filteredSpamlist);
        System.out.println("Total Words: "+filteredSpamlist.size());

    }


    static void shuffleDataset(double[][] x){
        List <double[]> list = Arrays.asList(x);
        Collections.shuffle(list);
        x = list.toArray(new double[0][]);//convert back to a array
    }


    static void ResetFeature(double[][] x){
        for(double p[]:x)
             Arrays.fill(p,-1.0);
    }


    static void Err() throws FileNotFoundException,IOException{
        File dir = new File("error");
        File[] listoffiles = dir.listFiles();
        int fc=0,wc=0;
        System.out.println("TEST 1");
        for(File file:listoffiles) {
            System.out.println("TEST 2");
            e[0]=1.0;
            Scanner s = new Scanner(file);
            String check;
            System.out.println("TEST 3");
            while(s.hasNext()){
                System.out.println("TEST 4");
                wc++;
                System.out.print("WC----"+wc+"\t");
                check=s.next();
                if(filteredSpamlist.contains(check)){
                    e[in]++;
                    in++;
                    if(in==49)
                        in=1;
                }
                else{
                    in++;
                    if(in==49)
                        in=1;
                }

            }
            e[49]=wc;
            wc=0;
            fc++;
        }


        System.out.println("\nError array is: ");
        for(double p:e)
            System.out.print(p+"\t");
    }
}
