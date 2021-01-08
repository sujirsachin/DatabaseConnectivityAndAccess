package Assignments.PE07;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Transactions
 *  10/31/2019
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

            if(connection!=null) {
                conn = true;
            }

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

            } //try
            catch (SQLException sqlexp)
            {
                throw new DLException(sqlexp,"Connection Problem");
            } //catch
        } //if

        return closeConn;
    } //closeConn method
private PreparedStatement prepare(String sql,String... values) throws DLException
{
    PreparedStatement prp=null;
    try {


        prp = connection.prepareStatement(sql);
        int i=1;

        for(String val:values)
        {
                prp.setString(i++,val);
            }


    }
    catch (SQLException sqlex)
    {
    throw new DLException(sqlex,"Problem with database");
    }
    catch (Exception ex)
    {
        throw new DLException(ex,"Couldn't complete process");
    }
    return prp;
}

public ArrayList<ArrayList<String>> getData(String sql,String... values) throws DLException
{
    ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
    try {

        PreparedStatement prp=null;
        prp=prepare(sql,values);
        ResultSet rs= prp.executeQuery();
        ResultSetMetaData rsmd=rs.getMetaData();
        ArrayList<String> columnName=new ArrayList<String>();
       int numOfFields=rsmd.getColumnCount();
       for(int i=1;i<=numOfFields;i++)
       {
           columnName.add(rsmd.getColumnName(i));
       }
       result.add(columnName);
        while(rs.next())
        {
            ArrayList<String> record=new ArrayList<String>();
            for (int j=1;j<=numOfFields;j++)
            {
                record.add(rs.getString(j));
            } //for
            result.add(record);

        } // while
        if(result.size()<2)  //checking if result has only column names and not data(Bad Query)
        {
            return null;  //null if only metadata is present
        }
        else
        {
            return result;
        }

    }
    catch (SQLException sqlex)
    {
        throw new DLException(sqlex,"Unable to complete process");
    }

} // getData method
    public int setData(String sql,String...values) throws DLException
    {
     int status=0;
     try {

          status= executeStmt(sql,values);

     }

     catch (Exception ex)
     {
         throw new DLException(ex,"Unable to process");
     }
        return status;
    }
   public int executeStmt(String statement, String... values) throws DLException
    {
        int status=0;
        try {
            PreparedStatement prp = prepare(statement, values);
            status=prp.executeUpdate();

        }

        catch (SQLException sqlex)
        {
            throw new DLException(sqlex,"Unable to complete request");
        }
        catch (Exception ex)
        {
            throw new DLException(ex,"Unable to process");
        }
        if(status==0)
        {
            return 0;
        }
        else
        {
            return status;
        }

    }
    /*
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
    } // getData method */


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
public void startTrans() throws DLException {
        try {
            connection.setAutoCommit(false);
        }
        catch (SQLException sqlex)
        {
            throw new DLException(sqlex,"Not able to start a transaction");
        }

}
public void endTrans() throws DLException
{
    try {
        connection.setAutoCommit(true);

    }
    catch (SQLException sqlex)
    {
        throw new DLException(sqlex,"Unable to commit transaction");
    }
}
public void rollbackTrans() throws DLException {
 try {
     connection.rollback();
 }
 catch (SQLException sqlex)
 {
     throw new DLException(sqlex,"Unable to roll back");
 }
}
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
