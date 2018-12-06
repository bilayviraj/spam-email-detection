package Methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

public class DataGenerator {
    static HashSet<String> hamlist = new HashSet<String>();
    static HashSet<String> spamlist= new HashSet<String>();
    static HashSet<String> alllist = new HashSet<String>();
    static HashSet<String> temp= new HashSet<String>();
    static HashSet<String> dictionary=new HashSet<String>();
    
    static int ham_file_count=0,spam_file_count=0,i=1;
    
    //Getting Ham Emails
    static public void GetHamList() throws FileNotFoundException{
        File dir = new File("training/ham");
        File[] listoffiles = dir.listFiles();

        for(File file:listoffiles) {
            Scanner s = new Scanner(file);
            while(s.hasNext()){
                hamlist.add(s.next());
            }
            ham_file_count++;
        }
    }
    
    //Getting Spam Emails
    static public void GetSpamList() throws FileNotFoundException{

        File dir = new File("training/spam");
        File[] listoffiles = dir.listFiles();

        for(File file:listoffiles) {
            Scanner s = new Scanner(file);
            while(s.hasNext()){
                spamlist.add(s.next());
            }
            spam_file_count++;
        }
    }
    
    static public void PrintHamList(){
        System.out.println("-----------HAM-------------");
        System.out.println(hamlist);
        System.out.println("Total Emails :"+ham_file_count);
        System.out.println("Total Words :"+hamlist.size());
    }

    static public void PrintSpamList(){
        System.out.println("++++++++++SPAM+++++++++++++");
        System.out.println(spamlist);
        System.out.println("Total Emails :"+spam_file_count);
        System.out.println("Total Words :"+spamlist.size());
    }
    
    
    static public void GetAllWords() throws FileNotFoundException, IOException{
    
        FileReader dict=new FileReader("config/words.txt");
        BufferedReader br=new BufferedReader(dict);
        String strLine;
        while ((strLine = br.readLine()) != null)   
              dictionary.add(strLine);
    
      for(String s1:spamlist)
          if(dictionary.contains(s1))
            alllist.add(s1);
           
    for(String s1:hamlist)
        if(!alllist.contains(s1))
            if(dictionary.contains(s1))
                alllist.add(s1);
    
        System.out.println("********ALL WORDS**********");
        System.out.println(alllist);
        System.out.println("Total number of words(attributes): "+ alllist.size());
        
    }
    
    
    
        
    
    public static void PrintDataset()throws FileNotFoundException{
        File file0 = new File("config/features");
        //Printing Features
         PrintWriter writer0 = new PrintWriter(file0);
         writer0.print("// A listing of the features and their possible values. All features are binary.");
         writer0.println();
        for(String p:alllist) {
                writer0.printf("%s",p);
                writer0.print(" ");
                writer0.print("0 1");
                writer0.println();
        }
        writer0.println();
        writer0.println("// space separated output labels");
        writer0.print("output_labels=spam ham");
        writer0.close();
        
 
        
        File dir1 = new File("training/spam");
        File[] listoffiles1 = dir1.listFiles();
        File file1=new File("config/training_set");
        PrintWriter writer1 = new PrintWriter(file1);
        for(File file_iterator:listoffiles1) {
          
            Scanner s = new Scanner(file_iterator);
            writer1.print("Train_"+i+" "+"spam"+" ");
            while(s.hasNext())
            temp.add(s.next());
            for(String p:alllist){
                String check=p;
                if(temp.contains(check))
                        writer1.print("1");
                else
                        writer1.print("0");
                
                writer1.print(" ");
                } 
            
            temp.clear();
            writer1.println();    
            i++;
        }
        
        
        
        File dir2 = new File("training/ham");
        File[] listoffiles2 = dir2.listFiles();
        for(File file_iterator:listoffiles2) {
          
            Scanner s = new Scanner(file_iterator);
            writer1.print("Train_"+i+" "+"ham"+" ");
            while(s.hasNext())
            temp.add(s.next());
            for(String p:alllist){
                String check=p;
                if(temp.contains(check))
                        writer1.print("1");
                else
                        writer1.print("0");
                
                writer1.print(" ");
                } 
            
            temp.clear();
            writer1.println();    
            i++;
        }
        writer1.close();
        i=1;
        
        File dir3 = new File("testing/spam");
        File[] listoffiles3 = dir3.listFiles();
        File file2=new File("config/testing_set");
        PrintWriter writer2 = new PrintWriter(file2);
        for(File file_iterator:listoffiles3) {
          
            Scanner s = new Scanner(file_iterator);
            writer2.print("Test_"+i+" "+"spam"+" ");
            while(s.hasNext())
            temp.add(s.next());
            for(String p:alllist){
                String check=p;
                if(temp.contains(check))
                        writer2.print("1");
                else
                        writer2.print("0");
                writer2.print(" ");
                } 
            
            temp.clear();
            writer2.println();    
            i++;
        }
        
        
        
       File dir4 = new File("testing/ham");
        File[] listoffiles4 = dir4.listFiles();
        for(File file_iterator:listoffiles4) {
          
            Scanner s = new Scanner(file_iterator);
            writer2.print("Test_"+i+" "+"ham"+" ");
            while(s.hasNext())
            temp.add(s.next());
            for(String p:alllist){
                String check=p;
                if(temp.contains(check))
                        writer2.print("1");
                else
                        writer2.print("0");
                
                writer2.print(" ");
                } 
            
            temp.clear();
            writer2.println();    
            i++;
        }
        writer2.close();
        }
}
