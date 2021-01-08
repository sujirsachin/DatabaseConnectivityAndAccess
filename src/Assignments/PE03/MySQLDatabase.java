package Assignments.PE03;/* Sachin Mohan Sujir
        *  ISTE 722 Database Connectivity and Access
        *  Practice Exercise 3- Data Retrieval
        *  09/27/2019
        */
import java.sql.*;
import java.lang.*;




public class MySQLDatabase {
    String driver="com.mysql.jdbc.Driver";
    String userid="root";
    String password="Sachin@96";
    String dbname="jdbc:mysql://localhost:3306/travel";
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
                System.out.println("Connection Success");
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
                System.out.println("Connection Closed");
            } //try
            catch (SQLException sqlexp)
            {
                System.out.println(sqlexp.getMessage());
            } //catch
        } //if

        return closeConn;
    } //closeConn method

    // getData method
    public String[][] getData(String stmt, int numOfFields) {
        String[][] result = new String[0][];
        try {
            Statement stmnt = connection.createStatement();
            result = new String[100][100];
            ResultSet rs = stmnt.executeQuery(stmt);
            int row = 0;
            while (rs.next()) {
                for (int i = 1; i <= numOfFields; i++) {

                    result[row][i - 1] = rs.getString(i);

                } //for
                row++;
            } //while
        } //try
        catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
        } //catch
        return result;
    } // getData method


    //setData method
    public boolean setData(String stmt)
    {
        boolean status=false;
        try {
            Statement stmnt=connection.createStatement();
            int result=stmnt.executeUpdate(stmt);

            if(result>=0)
            {
                status=true;
                System.out.println("Number of rows affected: "+result);
            }
        } //try
        catch (SQLException sqlexp)
        {
            System.out.println(sqlexp.getMessage());
        } //catch
        return status;
    } //setData method
} //class
