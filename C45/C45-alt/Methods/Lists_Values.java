
package Methods;

import java.util.HashSet;

public class Lists_Values {
    
    static HashSet<String> hamlist = new HashSet<String>();
    static HashSet<String> spamlist= new HashSet<String>();
    static HashSet<String> alllist = new HashSet<String>();
    static HashSet<String> temp= new HashSet<String>();
    static HashSet<String> dictionary=new HashSet<String>();
    
    static int i1=1,i2=1; 
    static int train_email_count=0,test_email_count=0;
    static int tp=0,fp=0,tn=0,fn=0;
    
    
    
    public void getResults(){
                double recall=(double)tp/(double)(tp+fn);
                double precision=(double)tp/(double)(tp+fp);
                System.out.println("");
		System.out.println("Recall= "+(recall*100)+"%");
                System.out.println("FP Rate= "+(double)(fp/(double)(fp+tn))*100+"%");
                System.out.println("Precision= "+precision*100+"%");
		System.out.println("F-measure= "+(double)((2*precision*recall)/(precision+recall))*100+"%");
                System.out.println("Accuracy= "+((double)(tp+tn)/(double)(tp+tn+fp+fn))*100+"%");
    }
}
