package Assignments.PE02;

import java.sql.*;
public class MySQLDatabase {
     String driver="com.mysql.jdbc.Driver";
     String userid="330User";
     String password="330Password";
     String dbname="jdbc:mysql://simon.ist.rit.edu/networkx_ser";
    public static Connection connection;


    public boolean connect()
    {
         connection= null;


        boolean conn=false;
        try
        {
            Class.forName(driver);
         connection= DriverManager.getConnection(dbname,userid,password);

         if(connection!=null)
         {
             conn=true;
             System.out.print("Success");
         } // if

        } //try block

catch (SQLException sqlexp)
{
    System.out.print(sqlexp.getMessage());
} //catch sqlexp
        catch (Exception exp)
        {
          System.out.print(exp.getMessage());
        } //catch exp

        return conn;
    } //boolean method
public boolean closeConn()
{
    boolean closeConn=false;
    if(connection!=null)
    {
        try {
            connection.close();
            closeConn = true;
        } //try
        catch (SQLException sqlexp)
        {
           System.out.println(sqlexp.getMessage());
        } //catch
    } //if
    return closeConn;
} //closeConn method
}
