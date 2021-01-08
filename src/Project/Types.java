package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *Types - List of Type objects
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Types {
	   // List of Type objects
	   private ArrayList<Type> types;	   	   
	    
	   @JsonIgnore
	   private MySQLDatabase dbObj = new MySQLDatabase();
	   @JsonIgnore
	   private User currentUser;
	   
	   public Types() {
		   types = new ArrayList<Type>();
	   }
	   
	   public Types(User user) {
		   types = new ArrayList<Type>();
		   this.currentUser=user;
	   }
	   
	   public ArrayList<Type> getTypes(){
		   return types;
	   }
	   
	   public void setTypes(ArrayList<Type> types) {
		   this.types = types;
	   }
	   
	  /**
	 	*GET all submission types		
	 	*@return Types Object
	 	*/	   
	   public Types getAllTypes() throws DLException {	       
	      ArrayList<ArrayList<String>> result = null;
	      try {
	    	  boolean isConnected = dbObj.connect();
		      if(isConnected) {			
		         ArrayList<String> args = new ArrayList<String>();
		         String sqlStr="SELECT * from _Types";		   
		         result = dbObj.getData(sqlStr,args);
		         if(result!=null && result.size()>1) {
		            types = new ArrayList<Type>();
		            for(int i=1,len=result.size(); i<len; i++) {
		               Type type = new Type(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
		               types.add(type);
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
		*GET types by Ids
		*@param An array of typeIds
		*@return boolean(true/false)
		*/
	   public Types getTypesById(int[] typeIds) throws DLException {		   
		   ArrayList<ArrayList<String>> result=null;
		   try {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "SELECT * from _Types WHERE typeId IN (";
				   for(int i=0, len=typeIds.length; i<len; i++) {
					   if(i==0) {
						   sqlStr+="?";
					   }else {
						   sqlStr+=",?";
					   }
					   args.add(String.valueOf(typeIds[i]));
				   }
				   sqlStr+=");";
				   result = dbObj.getData(sqlStr,args);
				   // populate the types DAO, traverse through the 2D structure
				   if(result!=null) {
					   types = new ArrayList<Type>();
					   for(int i=1, len=result.size(); i<len; i++) {				   
						   Type type = new Type(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
						   types.add(type);
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
		*Add new paper types
		*@param String array of new types	
		*@return boolean(true/false)
		*/
	   public boolean addTypes(String[] newTypes) throws DLException {		   
		   boolean isAdded=false;
		   try {
			   if(currentUser.getIsAdmin()==1) {
				   boolean isConnected = dbObj.connect();
				   if(isConnected) {	
					   int maxTypeId=0;
					   ArrayList<String> args = new ArrayList<String>();
					   //get the max type ID and then add the new types
					   String sqlStr = "SELECT max(typeId) FROM _Types";
					   ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
					   if(result!=null) {
						   if(result.size()>1) {
							   maxTypeId = Integer.parseInt(result.get(1).get(0));
						   }					   
						   sqlStr = "INSERT INTO _Types(typeId,typeName) VALUES";
						   for(int i=0, len=newTypes.length; i<len; i++) {
							   String typeName = newTypes[i];
							   if(i==0) {
								   sqlStr+="(?,?)";
							   }else {
								   sqlStr+=", (?,?)";   
							   }
							   
							   args.add(String.valueOf(++maxTypeId));
							   args.add(typeName);		   
						   }					   
						   int rc = dbObj.setData(sqlStr, args);							   
						   if(rc==newTypes.length) {
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
		 * Converts to JSON string
		 * @return JSON representation of the object
		 */
	   public String toString() {
		   String resultJson="";		   
		   ObjectToJson jsonObj =  new ObjectToJson(this.types);
		   try {
			 resultJson = jsonObj.convertToJson();  
		   }
		   catch(Exception ex) {
			   System.out.println("Couldn't convert to JSON."+ex.getMessage());		   
		   }
		   return resultJson;
	   }

}
