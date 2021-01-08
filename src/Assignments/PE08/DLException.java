package Assignments.PE08;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 5- Exceptions
 *  10/11/2019
 */

import java.io.*;
import java.sql.Timestamp;


public class DLException extends Exception {
    Exception ex;

    public DLException(Exception ex, String userMessage)  {
        super(userMessage);

        writeLog(ex, userMessage);

        System.exit(0);
    }

    public DLException(Exception ex, String userMessage, String... values)  {
        super(userMessage);

            writeLog(ex, userMessage, values);
        System.exit(0);


    }

    private void writeLog(Exception ex, String userMessage, String... values)  {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
                File logFile = new File("mySQLLog.txt");
                if (!logFile.exists()) {

                    logFile.createNewFile();
                }  // if
                FileWriter fw= new FileWriter("mySQLLog.txt",true);
                BufferedWriter bw=new BufferedWriter(fw);
                PrintWriter pw=new PrintWriter(bw);
                pw.println("");
                pw.println("");
            pw.write(timestamp.toString());
            pw.print(" ");
                ex.printStackTrace(pw);
            for (int i=0;i<values.length;i++)
            {
                pw.print(" ");
                pw.write(values[i]);
            }

                //pw.write(ex.toString());


                System.out.println(userMessage);
                pw.close();
            } //try
            catch (IOException io) {
                System.out.println(ex.getMessage());
            } //catch

        }//writeLog
    }

