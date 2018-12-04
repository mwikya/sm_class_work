
/**
 * @author mwikya
 */
public class Dice {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
       int a1=0;
        int a2=0;
        int a3=0;
        int a4=0;
        int a5=0;
        int a6=0;
        int k=0;
        double x=0;
        while(k<1000){
        x=Math.random();
        if(x<(1.0/6.0)){
        a1++;
                }
        else if(x>(1.0/6.0) && x<=(2.0/6.0)){
        a2++;
                }
        else if(x>(2.0/6.0)&& x<=(3.0/6.0)){
        a3++;
                }
        else if(x>(3.0/6.0) && x<=(4.0/6.0)){
        a4++;
                }
        else if(x>(4.0/6.0) && x<=(5.0/6.0)){
        a5++;}
        else {
          a6++;
        }
       k++;
       
    }
        int faceno[]={a1,a2,a3,a4,a5,a6};
        
        double percent[]={(double) a1/10,(double) a2/10,(double) a3/10,(double) a4/10,(double) a5/10,(double) a6/10};
         System.out.println("face\tfrequency\tpercentage");
        for(int m=1;m<=faceno.length;m++){
            System.out.println(m +"\t"+ faceno[m-1] +"\t\t"+percent[m-1]);
        }
    }
    
}
