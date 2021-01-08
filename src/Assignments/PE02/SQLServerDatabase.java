package Assignments.PE02;

import java.sql.*;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerDatabase {
     String dbname="jdbc:sqlserver://theodore.ist.rit.edu\\Jobs;user=330User;password=330Password";
     public static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    Connection connection=null;
    public boolean connect()
    {
        boolean conn=false;
        try
        {
            Class.forName(driver);
            connection= DriverManager.getConnection(dbname);
            if(connection!=null)
            {
                conn=true;
                System.out.println("Success");
            } // if
            else
            {
                System.out.print("here");
            }

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
