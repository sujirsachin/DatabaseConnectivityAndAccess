package Project;

import java.util.*;
import Project.dependencies.jackson.annotation.*;

/**
 *Affiliation - contains information about each affiliation
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri  
 */
public class Affiliation {
	
   private int affiliationId;
   private String affiliationName;
   
   @JsonIgnore
   private Users users;   
   @JsonIgnore
   private MySQLDatabase dbObj = new MySQLDatabase();   
   @JsonIgnore
   private User currentUser;
      
   public Affiliation() {	   
   }
   
   public Affiliation(User user) {
	   this.currentUser=user;
   }
      
   public Affiliation(int affiliationId) {
	  this.affiliationId = affiliationId; 
   }
   
   public Affiliation(int affiliationId, String affiliationName){
      this.affiliationId = affiliationId;
      this.affiliationName = affiliationName;
   }
   
   public int getAffiliationId(){
      return this.affiliationId;
   }
   
   public void setAffiliationId(int affiliationId){
      this.affiliationId = affiliationId;
   }
   
   public String getAffiliationName(){
      return this.affiliationName;
   }
   
   public void setAffiliationName(String affiliationName){
      this.affiliationName = affiliationName;   
   }
   
   /**
	 *GET affiliation by ID
	 *@param affiliationId	
	 *@return Affiliation object
	 */
   public Affiliation getAffiliation(int affiliationId) throws DLException {
	   ArrayList<ArrayList<String>> result = null;
	   try {
		  this.setAffiliationId(affiliationId);      
	      boolean isConnected = dbObj.connect();
	      if(isConnected) {			
	         ArrayList<String> args = new ArrayList<String>();
	         String sqlStr="SELECT * FROM _affiliations WHERE affiliationID=?";
	         args.add(String.valueOf(affiliationId));
	         result = dbObj.getData(sqlStr,args);
	         if(result!=null && result.size()>1) {
	            this.setAffiliationId(Integer.parseInt(result.get(1).get(0)));
	            this.setAffiliationName(result.get(1).get(1));	           
	            dbObj.close();			
	         }
	      }		   
	   }
	   catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	   }      
      return this;   
   }
   
   /**
	 *GET affiliation by name
	 *@param affiliationName	
	 *@return Affiliation object
	 */
   public Affiliation getAffiliationByName(String affiliation) throws DLException {	    
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * FROM _affiliations WHERE affiliationName=?";
			   args.add(affiliation);
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   this.setAffiliationId(Integer.parseInt(result.get(1).get(0)));
				   this.setAffiliationName(result.get(1).get(1));			   
				   dbObj.close();			
			   }
		   }
	   }
	   catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	   }	      
	   return this;
   }
   
   /**
	 *ADD new affiliation
	 *@param affiliationName
	 *@return boolean(true/false)
	 */
   public boolean addAffiliation(String affiliationName) throws DLException {
      boolean isAdded=false;   
      try {
    	  if(currentUser.getIsAdmin()==1) {
	    	  this.setAffiliationName(affiliationName);
	          boolean isConnected = dbObj.connect();
	          if(isConnected) {        	  
	             // get maximum id from the affiliations
	             ArrayList<String> args = new ArrayList<String>();
	             int maxAffiliationId=0;
	             String sqlStr = "SELECT max(affiliationID) from _affiliations;";
	             ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
	             if(result!=null && result.size()>1) {
	                maxAffiliationId = Integer.parseInt(result.get(1).get(0));
	             } 
	             sqlStr = "INSERT INTO _affiliations VALUES(?,?)";
	             this.setAffiliationId(maxAffiliationId+1);
	             args.add(String.valueOf(maxAffiliationId+1));
	             args.add(affiliationName);
	             int rc = dbObj.setData(sqlStr,args);             
	             if(rc==1) {
	            	 isAdded = true;
	             }
	             dbObj.close();
	          } 	  
    	  }
      }
      catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	  }      
      return isAdded; 
   }
   
   /**
	 *Set affiliation: if exists, update else add new affiliation
	 *@param affiliationId
	 *@param affiliationName
	 *@return boolean(true/false)
	 */ 
   public boolean setAffiliation(int affiliationId, String affiliationName) throws DLException {      
      boolean isSet=false;
      try {
    	  if(currentUser.getIsAdmin()==1) {
	    	  boolean isConnected = dbObj.connect();
	          if(isConnected) {
	             getAffiliation(affiliationId);
	             if(this.getAffiliationName()==null) {
	                isSet = addAffiliation(affiliationName);
	             } else {
	                isSet = updateAffiliation(affiliationId, affiliationName);
	             }	   		  			  
	             dbObj.close();
	          }
    	  }
      }
      catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	  }      
      return isSet; 
   }
   
   /**
  	 *Update existing affiliation
  	 *@param affiliationId
  	 *@param affiliationName
  	 *@return boolean(true/false)
  	 */    
   public boolean updateAffiliation(int affiliationId, String affiliationName) throws DLException {
      boolean isUpdated=false;
      try {
    	  if(currentUser.getIsAdmin()==1) {
	    	  this.setAffiliationId(affiliationId);
	          this.setAffiliationName(affiliationName);
	          boolean isConnected = dbObj.connect();
	          if(isConnected) {
	             ArrayList<String> args = new ArrayList<String>();
	             String sqlStr = "UPDATE _affiliations SET affiliationName = ? WHERE affiliationID = ?";
	             args.add(affiliationName);
	             args.add(String.valueOf(affiliationId));		   
	             int rc = dbObj.setData(sqlStr,args);
	             if(rc==1) {
	            	 isUpdated = true;
	             }
	             dbObj.close();
	          }
    	  }
      }
      catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	  }      
      return isUpdated; 
   }
            
   /**
 	 *Delete affiliation
 	 *@param affiliationId 	
 	 *@return boolean(true/false)
 	 */
   public boolean deleteAffiliation(int affiliationId) throws DLException {
      boolean isDeleted=false;
      try {
    	  if(currentUser.getIsAdmin()==1) {
	    	  this.setAffiliationId(affiliationId);	   
	          boolean isConnected = dbObj.connect();
	          if(isConnected) {
	        	  dbObj.startTrans();
	        	 // delete from users
	        	 users = new Users(currentUser);
	        	 users.deleteUsersByAffiliation(affiliationId);
	             ArrayList<String> args = new ArrayList<String>();
	             String sqlStr = "DELETE FROM _affiliations WHERE affiliationID = ?";		   
	             args.add(String.valueOf(affiliationId));		   
	             int rc = dbObj.setData(sqlStr,args);
	             if(rc==1) {
	            	 isDeleted = true;
	             }
	             dbObj.endTrans();
	             dbObj.close();
	          }
    	  }
      }
      catch(Exception ex) {
    	  dbObj.rollbackTrans();
		  throw new DLException(ex, ex.getMessage());
	  }
      return isDeleted;
   }
   
   /**
	 * Converts to JSON string
	 * @return JSON representation of the object
	 */
   public String toString() {
	   String resultJson="";	   
	   ObjectToJson jsonObj =  new ObjectToJson(this);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("Couldn't convert to JSON."+ex.getMessage());		   
	   }
	   return resultJson;
   }
   
}