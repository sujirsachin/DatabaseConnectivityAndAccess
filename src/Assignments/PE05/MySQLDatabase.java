package Assignments.PE05;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 5- Exception
 *  10/11/2019
 */
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.lang.*;
import java.util.ArrayList;



public class MySQLDatabase {
    String driver="com.mysql.jdbc.Driver";
    String userid="root";
    String password="student";
    String dbname="jdbc:mysql://localhost:3306/travel";

    public static Connection connection;


    public boolean connect() throws DLException
    {
        connection= null;


        boolean conn=false;
        try
        {
            Class.forName(driver);
            connection= DriverManager.getConnection(dbname,userid,password);

            if(connection!=null)
                conn=true;
            {
                System.out.println("Connection Success");
            } // if

        } //try block

         catch (SQLException sqlexp)
        {
            throw new DLException(sqlexp,"Problem With Connection","test 1 successful");
        } //catch sqlexp
        catch (Exception exp)
        {
            throw new DLException(exp,"Unable to complete request");
        } //catch exp

        return conn;
    } //boolean method
    public boolean close() throws DLException
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
                throw new DLException(sqlexp,"Connection Problem");
            } //catch
        } //if

        return closeConn;
    } //closeConn method

    // getData method
    public String[][] getData(String stmt, int numOfFields) throws DLException {
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
        catch (SQLException sqlexp)
        {
            if(sqlexp.getClass().toString().contains("java.sql.SQLSyntaxErrorException"))
            {

                throw new DLException(sqlexp,"There is some problem with SQL expression","Error in: "+stmt," \n test2 successful");

            }
            else {
                throw new DLException(sqlexp, "Sorry problem with retrieving the data");
            }

        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }

        return result;
    } // getData method


    //setData method
    public boolean setData(String stmt) throws DLException
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
            if(sqlexp.getClass().toString().contains("java.sql.SQLSyntaxErrorException"))
            {

                throw new DLException(sqlexp,"There is some problem with SQL expression","Error in: "+stmt," \n test2 successful");

            }
            else {
                throw new DLException(sqlexp, "Sorry problem with updating the data");
            }

        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }

        return status;
    } //setData method

    public ArrayList<ArrayList<String>> getData(String sql, boolean colwdt) throws DLException {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {
            Statement stmt = connection.createStatement();
            ArrayList<String> columnWidth = new ArrayList<String>();
            ArrayList<String> columnName = new ArrayList<String>();
            ArrayList<Integer> columnWidMax=new ArrayList<Integer>();

            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int numOfFields = rsmd.getColumnCount();

            for(int i=1;i<=numOfFields;i++)
            {
                columnName.add(rsmd.getColumnName(i));
                columnWidth.add(String.valueOf(rsmd.getColumnDisplaySize(i)));
            }

            if(colwdt)
            {
                result.add(columnName);
                result.add(columnWidth);

            }
            while(rs.next())
            {
                ArrayList<String> record=new ArrayList<String>();
                for (int j=1;j<=numOfFields;j++)
                {
                    record.add(rs.getString(j));
                } //for
                result.add(record);

            } // while


        } //try

        catch (SQLException sqlexp)
            {
                if(sqlexp.getClass().toString().contains("java.sql.SQLSyntaxErrorException"))
               {

                    throw new DLException(sqlexp,"There is some problem with SQL expression","Error in: "+sql," \n test2 successful");

                }
                else {
                    throw new DLException(sqlexp, "Sorry problem with retrieving the data");
                }

            }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }


        return result;
    }
    } //class
