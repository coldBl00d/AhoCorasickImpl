
import java.io.*;
	import java.math.*;
	import java.security.*;
	import java.text.*;
	import java.util.*;
	import java.util.concurrent.*;
	import java.util.regex.*;


public class KangarooJump {
	
	    /*
	     * https://www.hackerrank.com/challenges/kangaroo/problem
	     */
	    static String kangaroo(int x1, int v1, int x2, int v2) {
	            int temp;
	            double y, n1,n2;
	            
	            if(x1>x2){
	                temp = x1;
	                x1=x2;
	                x2=temp;
	                temp=v1;
	                v1=v2; 
	                v2=temp;
	            }
	        
	            if(v1<v2)
	                return "NO";
	            else if(v1==v2){
	                if(x1==x2)
	                    return "YES";
	                else 
	                    return "NO";
	            }
	            else {
	            	//point at which they meet
	                y = ((v1*x2)-(v2*x1))/(v1-v2);
	                //number of hops
	                n2 = (y-x2)/v2;
	                n1 = (y-x1)/v1;
	                //check if n is integral and if equal they meet else no. 
	                if(n1%1==0 && n2%1==0 && n1==n2)
	                    return "YES";
	                else return "NO";
	            }
	    }

	    private static final Scanner scanner = new Scanner(System.in);
    
	    public static void main(String[] args) throws IOException {
	        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

	        String[] x1V1X2V2 = scanner.nextLine().split(" ");

	        int x1 = Integer.parseInt(x1V1X2V2[0]);

	        int v1 = Integer.parseInt(x1V1X2V2[1]);

	        int x2 = Integer.parseInt(x1V1X2V2[2]);

	        int v2 = Integer.parseInt(x1V1X2V2[3]);

	        String result = kangaroo(x1, v1, x2, v2);

	        bufferedWriter.write(result);
	        bufferedWriter.newLine();

	        bufferedWriter.close();

	        scanner.close();
	    }
}

	


