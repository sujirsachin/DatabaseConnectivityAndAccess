package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 *Users - List of User objects 
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Users {
   private ArrayList<User> users;
   
   @JsonIgnore
   private MySQLDatabase dbObj=new MySQLDatabase();
   @JsonIgnore
   User currentUser;
   //Constructor
   public Users(){
	   users = new ArrayList<User>();
   }
   public Users(User user) {
	   currentUser=user;
	   users = new ArrayList<User>();
   }

   public ArrayList<User> getUsers(){
      return users;
   }

   public void setUsers(ArrayList<User> users){
      this.users=users;
   }
   
   /**
    *GET all users    
    *@return Users object
    */
   public Users getAllUsers() throws DLException{      
      ArrayList<ArrayList<String>> result=null;
      try {
    	  boolean isConnected=dbObj.connect();
          if(isConnected){
             String sqlStr="SELECT * FROM Users;";
             ArrayList<String> args=new ArrayList<String>();
             result = dbObj.getData(sqlStr,args);
             ArrayList<String> row=new ArrayList<String>();
             users = new ArrayList<User>();
             for(int i=1;i<result.size();i++){
                row=result.get(i);
                User u = new User(Integer.parseInt(row.get(0)),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),row.get(6),Integer.parseInt(row.get(7)));
                users.add(u);
             }         
             dbObj.close();             
          }
      }
      catch(Exception ex) {
	   		throw new DLException(ex, ex.getMessage());
	  }      
      return this;
   }
   
   /**
    *GET users by ids
    *@param list of user IDs
    *@return Users object
    */
   public Users getUsersById(ArrayList<Integer> userIds) throws DLException {     
      ArrayList<ArrayList<String>> result=null;
      try {
    	  boolean isConnected=dbObj.connect();
          if(isConnected){
             String sqlStr="SELECT * FROM users WHERE userId IN (";
             ArrayList<String> args = new ArrayList<String>();
             for(int i=0;i<userIds.size();i++){
                if(i==0){
                   sqlStr+="?";
                }
                else{
                sqlStr+=",?";
                }
                args.add(String.valueOf(userIds.get(i)));
             }
             sqlStr+=");";
             result = dbObj.getData(sqlStr,args);
             users = new ArrayList<User>();
             ArrayList<String> row=new ArrayList<String>();         
             for(int i=1;i<result.size();i++){
                row=result.get(i);
                User u = new User(Integer.parseInt(row.get(0)),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),row.get(6),Integer.parseInt(row.get(7)));
                users.add(u);
             }
          }    	 
      }
      catch(Exception ex) {
    	  throw new DLException(ex, ex.getMessage());
	  }      
      return this;
   }
   
   /**
    *GET all user names   
    *@return Users object
    */
   public Users getUsersName() throws DLException {	   
	   ArrayList<ArrayList<String>> result=null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr = "SELECT userId, firstName, lastName from Users;";
			   result = dbObj.getData(sqlStr,args);
			   // populate the subjects DAO, traverse through the 2D structure
			   if(result!=null) {
				   users = new ArrayList<User>();
				   for(int i=1, len=result.size(); i<len; i++) {
					   User u = new User(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), result.get(i).get(2));
					   users.add(u);
				   }
			   }
			   dbObj.close();
		   }
	   }
	   catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	   }	   
	   return this;
   }
   
   
   /**
    *GET all users by affiliation
    *@param affiliationId    
    *@return Users object
    */
   public Users getUsersByAffiliation(int affiliationId) throws DLException {	  
      ArrayList<ArrayList<String>> result=null;
      try {
    	  boolean isConnected=dbObj.connect();
          if(isConnected){
             String sqlStr="SELECT * FROM users WHERE affiliationId=?";
             ArrayList<String> args = new ArrayList<String>();
             args.add(String.valueOf(affiliationId));
             result = dbObj.getData(sqlStr,args);
             if(result!=null) {
            	 users = new ArrayList<User>();
            	 ArrayList<String> row = new ArrayList<String>();         
                 for(int i=1;i<result.size();i++){
                    row=result.get(i);
                    User u = new User(Integer.parseInt(row.get(0)),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),row.get(6),Integer.parseInt(row.get(7)));
                    users.add(u);
                 }                              	 
             }
             dbObj.close();
          }    	  
      }
      catch(Exception ex) {
	   		throw new DLException(ex, ex.getMessage());
	  }      
      return this;	   
   }
   
   /**
    *GET all users by paper
    *@param paperId    
    *@return Users object
    */
   public Users getUsersbyPaper(int paperId) throws DLException {	       
      try {
    	  boolean isConnected=dbObj.connect();
          if(isConnected){
             PapersAuthors pa = new PapersAuthors(currentUser);
             pa.getPapersAuthorsByOrder(paperId);
             ArrayList<Integer> userIds = new ArrayList<Integer>();
             for(int i=0;i<pa.paperAuthorsList.size();i++){
            	 userIds.add(pa.paperAuthorsList.get(i).getUserId());
             }
             getUsersById(userIds);         
             dbObj.close();
          }
      }
      catch(Exception ex) {
	   		throw new DLException(ex, ex.getMessage());
	  }	  
      return this;      
   }
   
   /**
    *delete users by affiliation
    *@param affiliationId
    *@return Users object
    */
   public boolean deleteUsersByAffiliation(int affiliationId) throws DLException{
	   	boolean isDeleted=false;
	   	try {
	   		if(currentUser.getIsAdmin()==1) {
	   			boolean isConnected = dbObj.connect();
				if(isConnected) {
					dbObj.startTrans();
					String sqlStr="SELECT userId FROM Users WHERE affiliationId =?";
					ArrayList<String> args = new ArrayList<String>();
					args.add(String.valueOf(affiliationId));
					ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
					if(result!=null && result.size()>1) {
						ArrayList<Integer> userIds = new ArrayList<Integer>();
						for(int i=1,len=result.size(); i<len; i++) {
							userIds.add(Integer.parseInt(result.get(i).get(0)));
						}
						Papers papers = new Papers(currentUser);
						papers.deletePapersByUser(userIds);
						sqlStr = "DELETE FROM Users WHERE affiliationId=?";
						int rc = dbObj.setData(sqlStr, args);
						if(rc>0) {
							isDeleted=true;
						}
						dbObj.endTrans();
				        dbObj.close();
					}
		        }
	   		}
	   	}
	   	catch(Exception ex) {	   		
	   		dbObj.rollbackTrans();
	   		throw new DLException(ex, "Delete not allowed");
	   	}
		return isDeleted;
   }
      
   
   /**
    *Set authors for paper
    *@param paperId
    *@return Users object
    */
   public boolean setUsersByPaper(int paperId, String[] firstNames, String[] lastNames) throws DLException {
	   boolean isSet=false;
	   try {
		   boolean isAuthorized=false;
		   if(currentUser.getIsAdmin()==1 ) {
			   isAuthorized = true;
		   }
		   List<String> fNames = Arrays.asList(firstNames);
		   List<String> lNames = Arrays.asList(lastNames);
		   boolean isConnected = dbObj.connect();	  
		   if(isConnected) {		   
			   //get the userIds of the names
			   Users u1 = new Users();
			   u1.getUsersName();
			   HashMap<Integer, Integer> authorOrders = new HashMap<Integer, Integer>();
               if (u1.users != null) {
            	   Paper p = new Paper();
				   p.getPaper(paperId);
				   if(p.getSubmitterId()==currentUser.getUserId()) {
					   isAuthorized=true;
				   }
                   for (int i = 0; i < u1.users.size(); i++) {
                       String fName = u1.users.get(i).getFirstName();
                       String lName = u1.users.get(i).getLastName();
                       if (fNames.contains(fName) && fNames.indexOf(fName) == lNames.indexOf(lName)) {
                           authorOrders.put(fNames.indexOf(fName), u1.users.get(i).getUserId());
                           if(!isAuthorized && u1.users.get(i).getUserId()==currentUser.getUserId()) {
							   isAuthorized = true;
						   }
                       }
                   }
               }			   
			   if(isAuthorized) {				   
				   PapersAuthors pa = new PapersAuthors(currentUser);
				   isSet = pa.setPaperAuthors(paperId, authorOrders);
			   }	   
			   dbObj.close();
		   }
	   }
	   catch(Exception ex) {
		   throw new DLException(ex,"Updating PaperAuthors not successful");
	   }
	  return isSet;	  
   }
	
   /**
    *Convert to string
    *@return JSON String representation
    */   
   public String toString() {
	   String resultJson="";
	   ObjectToJson jsonObj =  new ObjectToJson(this.users);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("problem in conversion "+ex.getMessage());		   
	   }
	   return resultJson;
   }
}
