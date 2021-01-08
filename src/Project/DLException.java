package Project;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * DLException - Custom exception class
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class DLException extends Exception {
   private Exception ex;
   String[] msgInfo;

   public DLException(Exception ex){
      this.ex = ex;
      writeLog();
      System.exit(0);
   }
   public DLException(Exception ex, String... values){
      this.ex = ex;
      this.msgInfo = values;
      writeLog();
      //System.exit(0);
   
   }
   public void writeLog(){
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String date = sdf.format(new Date());

      try {         
         //print writer
         PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("serverError.log",true)));
     
         out.println("Date: " + date +"\n");
         out.println("Cause:" + ex.getCause() +"\n");
                   
         String message="";
         for(String msg : msgInfo) {
            message+= msg+" ";
         }         
         out.println("Message:" + message+"\n"+ex.getMessage()+ "\n ");
         out.println("Class Name: " + ex.getStackTrace()[0].getClassName() + "\n");
         out.println("Method Name: " + ex.getStackTrace()[0].getMethodName() + "\n");
         out.println("Line number: " + ex.getStackTrace()[0].getLineNumber() + "\n");
         ex.printStackTrace(out);
         out.flush();
         out.close();
         //line number which method
      }
      catch(IOException io){
         io.getMessage();
      }
      catch(Exception ex){
         ex.getMessage();
      }
   }
}