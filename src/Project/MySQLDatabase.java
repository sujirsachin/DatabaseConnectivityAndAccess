package Project;

import java.sql.*;
import java.util.*;

/**
 * MySQLDatabase - Database class with DML and transaction operations
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class MySQLDatabase {
	
   //attributes
   private String url;
   private String userName;
   private String password;
   private Connection conn;

   public MySQLDatabase(){
   //instanciating attributes
      this.url = "jdbc:mysql://localhost/CSM?useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
      this.userName = "root";
      this.password = "student";   
   }
   
   /**
   *This method attempts to connect to the database
   *@return If the connection is successful it will return true
   */
   public boolean connect() throws DLException{
      try {
         //make connection
         conn = DriverManager.getConnection(url,userName,password);
         if(conn != null){
            return true;
         }
      } //end try
      catch(SQLException sqlEx){          
         throw new DLException(sqlEx, "Couldn't connect to the MySQL database.");                 
      }// end catch      
      catch(Exception ex){         
         throw new DLException(ex, ex.getMessage());                 
      }               
      return false;     
   }//end connect method
   
 /**
   *This method attempts to close the database
   *@return If the connection closes successfully it will return true
   */
   public boolean close() throws DLException {      
      try {
         if(conn!=null){
            //closes connection
            conn.close();
            return true;
         }                  
      } //end try
      catch(SQLException sqlEx){
         throw new DLException(sqlEx, "Connection doesn't exist, nothing to release.");  
      }//end catch
      catch(Exception ex){
         throw new DLException(ex, ex.getMessage());
      }//end catch
      return false;   
   } //end close method
   
  /**
   *Prepares the statement, binds the values and returns a prepared statement
   *@param parameterized statement
   *@param values to bind
   *@return prepared statement
   */
   private PreparedStatement prepare(String sqlStmt, ArrayList<String> params) throws DLException {
      PreparedStatement ps = null;
      try{
         ps = conn.prepareStatement(sqlStmt);
         for(int i=0; i<params.size();i++){
            ps.setString(i+1,params.get(i));
         }
      }
      catch(SQLException sqlEx){
         throw new DLException(sqlEx, "Couldn't prepare the SQL statement.");
      }
      catch(Exception ex){
         throw new DLException(ex, ex.getMessage());
      }
      return ps;
   }

  /**
   *Calls the prepared method, executes the statement and returns 2d array structure
   *@param parameterized query
   *@param array list of values to bind
   *@return 2D array structure
   */
   public ArrayList<ArrayList<String>> getData(String sqlStmt, ArrayList<String> params) throws DLException {
      try {
         ArrayList<ArrayList<String>> result = null;
         PreparedStatement ps = this.prepare(sqlStmt,params);
         if(ps!=null) {
            ResultSet rs = ps.executeQuery();
            if(rs!=null) {
               ResultSetMetaData rsmd = rs.getMetaData();
               result = new ArrayList<ArrayList<String>>();
               ArrayList<String> colMeta = new ArrayList<String>();
               for(int i=1;i<=rsmd.getColumnCount();i++) {
                  colMeta.add(rsmd.getColumnName(i));
               }
               result.add(colMeta);
               while(rs.next()){
                  ArrayList<String> row = new ArrayList<String>();
                  for(int i=1; i<=rsmd.getColumnCount();i++){                
                     row.add(rs.getString(i));
                  } //end for loop
                  result.add(row);
               } //end while
            }
         }
         return (result!=null && result.size()>1)?result:null;
      }
      catch(SQLException sqlEx){
         throw new DLException(sqlEx, "Fetching from the database was unsuccessful.");
      }
      catch(Exception ex){
         throw new DLException(ex, ex.getMessage());
      }   
   }

   /**
	 * manipulate data in database
	 * @param sql query
	 * @param params
	 * @throws DLException
	 */	
   public int setData(String sqlStmt,ArrayList<String> params) throws DLException{
      int numRows=-1;
      try {
         PreparedStatement ps = this.prepare(sqlStmt,params);
         if(ps!=null){
            numRows = ps.executeUpdate();
         }
      }
      catch(SQLException sqlEx){
         throw new DLException(sqlEx, "Couldn't complete operation on the database.");
      }
      catch(Exception ex){
         throw new DLException(ex, ex.getMessage());
      }   
      return numRows;
   }
   
   /**
	 * Starts transaction
	 * @throws DLException
	 */	
   public void startTrans() throws DLException{
      try {
         if(conn!=null){
            conn.setAutoCommit(false);            
         }         
      }
      catch(SQLException sqlEx) {
         throw new DLException(sqlEx, "Problem in begining transaction.");
      }
      catch(Exception ex) {
         throw new DLException(ex, ex.getMessage());
      }      
   }
   
   /**
  	 * Ends transaction
  	 * @throws DLException
  	 */
   public void endTrans() throws DLException{
      try {
         if(conn!=null){
            conn.commit();            
            conn.setAutoCommit(true);
         }
      }
      catch(SQLException sqlEx) {    	  
    	  if(conn!=null) {
    		  rollbackTrans();
    	  }
         throw new DLException(sqlEx, "Problem in committing the transaction");
      }
      catch(Exception ex) {    	  
    	  if(conn!=null) {
    		  rollbackTrans();
    	  }
         throw new DLException(ex, ex.getMessage());
      }
   }
   
   /**
 	 * Rollback transaction
 	 * @throws DLException
 	 */
   public void rollbackTrans() throws DLException{
      try {
         if(conn!=null){
           conn.rollback();
         }
      }
      catch(SQLException sqlEx) {
         throw new DLException(sqlEx, "Couldn't rollback transaction.");
      }
      catch(Exception ex) {
         throw new DLException(ex, ex.getMessage());
      }
      
   }

}