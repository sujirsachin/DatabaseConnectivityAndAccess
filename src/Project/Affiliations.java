package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Affiliations - contains list of Affilation objects
 * @author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Affiliations {
	   // list of affiliation objects	
	   private ArrayList<Affiliation> affiliations;	   	   
	   
	   @JsonIgnore
	   private MySQLDatabase dbObj = new MySQLDatabase();
	   @JsonIgnore
	   private User currentUser;
	   
	   public Affiliations() {
		   affiliations = new ArrayList<Affiliation>();
	   }
	   
	   public Affiliations(User user) {
		   affiliations = new ArrayList<Affiliation>();
		   currentUser=user;
	   }
	   
	   public ArrayList<Affiliation> getAffiliations(){
		   return affiliations;
	   }
	   
	   public void setAffiliations(ArrayList<Affiliation> affiliations) {
		   this.affiliations = affiliations;
	   }
	   
	   /**
		*GET all affiliations		
		*@return Affiliations object
		*/
	   public Affiliations getAllAffiliations() throws DLException {
		  try {
			  ArrayList<ArrayList<String>> result = null;
		      boolean isConnected = dbObj.connect();
		      if(isConnected) {			
		         ArrayList<String> args = new ArrayList<String>();
		         String sqlStr="SELECT * from _affiliations";		   
		         result = dbObj.getData(sqlStr,args);
		         if(result!=null) {
		        	 affiliations = new ArrayList<Affiliation>();
		            for(int i=1,len=result.size(); i<len; i++) {
		               Affiliation aff = new Affiliation(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
		               affiliations.add(aff);
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
		 *GET affiliations by Ids
		 *@param affiliationIds 
		 *@return Affiliations object
		 */
	   public Affiliations getAffiliationsById(int[] affiliationIds) throws DLException {		
		   ArrayList<ArrayList<String>> result=null;
		   try {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "SELECT * from _affiliations WHERE affiliationID IN (";
				   for(int i=0, len=affiliationIds.length; i<len; i++) {
					   if(i==0) {
						   sqlStr+="?";
					   }else {
						   sqlStr+=",?";
					   }
					   args.add(String.valueOf(affiliationIds[i]));
				   }
				   sqlStr+=");";
				   result = dbObj.getData(sqlStr,args);
				   // populate the affiliations DAO, traverse through the 2D structure
				   if(result!=null) {
					   affiliations = new ArrayList<Affiliation>();
					   for(int i=1, len=result.size(); i<len; i++) {				   
						   Affiliation aff = new Affiliation(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
						   affiliations.add(aff);
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
		 *GET affiliations by name
		 *@param affiliationNames  	
		 *@return Affiliations object
		 */
	   public Affiliations getAffiliationsByName(String[] affiliationNames) throws DLException {	   
		   ArrayList<ArrayList<String>> result=null;
		   try {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "SELECT * from _affiliations WHERE affiliationName IN (";
				   for(int i=0, len=affiliationNames.length; i<len; i++) {
					   if(i==0) {
						   sqlStr+="?";
					   }else {
						   sqlStr+=",?";
					   }
					   args.add(affiliationNames[i]);
				   }
				   sqlStr+=");";
				   result = dbObj.getData(sqlStr,args);
				   // populate the affiliatiomns DAO, traverse through the 2D structure
				   if(result!=null) {
					   affiliations = new ArrayList<Affiliation>();
					   for(int i=1, len=result.size(); i<len; i++) {				   
						   Affiliation aff = new Affiliation(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
						   affiliations.add(aff);
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
		 *ADD new affiliations
		 *@param newAffiliations
		 *@return boolean(true/false)
		 */
	   public boolean addAffiliations(String[] newAffiliations) throws DLException {		   
		   boolean isAdded=false;
		   try {
			   if(currentUser.getIsAdmin()==1) {
				   boolean isConnected = dbObj.connect();		   
				   if(isConnected) {
					   int maxTypeId=0;
					   ArrayList<String> args = new ArrayList<String>();
					   //get the max type ID and then add the new types
					   String sqlStr = "SELECT max(affiliationID) FROM _affiliations";
					   ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
					   if(result!=null) {
						   if(result.size()>1) {
							   maxTypeId = Integer.parseInt(result.get(1).get(0));
						   }
						   sqlStr = "INSERT INTO _affiliations(affiliationId,affiliationName) VALUES";
						   for(int i=0, len=newAffiliations.length; i<len; i++) {
							   String typeName = newAffiliations[i];
							   if(i==0) {
								   sqlStr+="(?,?)";
							   }else {
								   sqlStr+=", (?,?)";   
							   }						   
							   args.add(String.valueOf(++maxTypeId));
							   args.add(typeName);				   
						   }
						   int rc = dbObj.setData(sqlStr, args);
						   if(rc==newAffiliations.length) {
							   isAdded=true;
						   }
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
	     *Convert to string
	     *@return JSON String representation
	     */
	   public String toString() {
		   String resultJson="";	   
		   ObjectToJson jsonObj =  new ObjectToJson(this.affiliations);
		   try {
			 resultJson = jsonObj.convertToJson();  
		   }
		   catch(Exception ex) {
			   System.out.println("Couldn't convert to JSON."+ex.getMessage());		   
		   }
		   return resultJson;
	   }
}
