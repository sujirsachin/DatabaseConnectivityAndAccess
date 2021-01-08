package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 *Subject - Information about a subject
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
class Subject{
  
   private int subjectId;
   private String subjectName;
   
   @JsonIgnore
   private Papers papers;   
   @JsonIgnore
   private MySQLDatabase dbObj = new MySQLDatabase();
   @JsonIgnore
   private User currentUser;
   //Constructors
   public Subject(){	   
   }
   public Subject(User user) {
	   this.currentUser=user;
   }
   public Subject(int subjectId){
      this.subjectId = subjectId;
   }
   
   public Subject(int subjectId,String subjectName){
      this.subjectId = subjectId;
      this.subjectName = subjectName;
   }
   
   //GETTERS AND SETTERS
   public int getSubjectId(){
	   return subjectId;
   }      

   public void setSubjectId(int subjectId){
      this.subjectId = subjectId;
   }      
   
   public void setSubjectName(String subjectName){
      this.subjectName = subjectName;
   }
   
   public String getSubjectName(){
	   return subjectName;
   }
   
   public Papers getPapers(){
	   return papers;
   }
   
   public void setPapers(Papers papers){
	   this.papers = papers;
   } 
   
   /**
	 *GET Subject by ID
	 *@param subjectId
	 *@return Subject object
	 */
   public Subject getSubject(int subjectId) throws DLException {	   
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   this.setSubjectId(subjectId);
		   this.setSubjectName(null);
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * FROM _Subjects WHERE subjectId=?";
			   args.add(String.valueOf(subjectId));
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   this.setSubjectId(Integer.parseInt(result.get(1).get(0)));
				   this.setSubjectName(result.get(1).get(1));			  		   		
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
	*GET Subject by name
	*@param subjectName	
	*@return Subject object
	*/
   public Subject getSubjectByName(String subjectName) throws DLException {	    
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * FROM _Subjects WHERE subjectName=?";
			   args.add(subjectName);
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   this.setSubjectId(Integer.parseInt(result.get(1).get(0)));
				   this.setSubjectName(result.get(1).get(1));			   				   			
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
  	*Add new subject
  	*@param subjectName	
  	*@return boolean(true/false)
  	*/
   public boolean addSubject(String subjectName) throws DLException {
	   boolean isAdded=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   this.setSubjectName(subjectName);
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {		   
				   // get maximum id from the subjects
				   ArrayList<String> args = new ArrayList<String>();
				   int maxSubjId=0;
				   String sqlStr = "SELECT max(subjectId) from _Subjects;";
				   ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
				   if(result!=null) {
					   if(result.size()>1) {
						   maxSubjId = Integer.parseInt(result.get(1).get(0));   
					   }
					   sqlStr = "INSERT INTO _Subjects VALUES(?,?)";
					   this.setSubjectId(maxSubjId+1);
					   args.add(String.valueOf(maxSubjId+1));
					   args.add(subjectName);
					   int rc = dbObj.setData(sqlStr,args);
					   if(rc==1) {
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
	*Set subject, if exists, update else add new subject
	*@param typeId
	*@param new typeName	
	*@return boolean(true/false)
	*/
   public boolean setSubject(int subjectId, String subjectName) throws DLException {      
	  boolean isSet=false;
	  try {
		  if(currentUser.getIsAdmin()==1) {
			  boolean isConnected = dbObj.connect();
		   	  if(isConnected) {
		   		  getSubject(subjectId);
		   		  if(this.getSubjectName()==null) {
		   			  isSet = addSubject(subjectName);
		   		  } else {
		   			  isSet = updateSubject(subjectId, subjectName);
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
 	*Update existing subject name
 	*@param subjectId
 	*@param new subjectName	
 	*@return boolean(true/false)
 	*/
   public boolean updateSubject(int subjectId, String subjectName) throws DLException {
	   boolean isUpdated = false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   this.setSubjectId(subjectId);
			   this.setSubjectName(subjectName);
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "UPDATE _Subjects SET subjectName = ? WHERE subjectId = ?";
				   args.add(subjectName);
				   args.add(String.valueOf(subjectId));		   
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
	*delete existing subject
	*@param subjectId 		
	*@return boolean(true/false)
	*/
   public boolean deleteSubject(int subjectId) throws DLException {
	   boolean isDeleted=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   this.setSubjectId(subjectId);	   
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   dbObj.startTrans();
				  // delete from paperSubjects
				   papers = new Papers(currentUser);
				   papers.deletePapersBySubject(subjectId); // deleting relation from PaperSubjects
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "DELETE FROM _Subjects WHERE subjectId = ?";		   
				   args.add(String.valueOf(subjectId));		   
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
  	 *Converts to JSON string
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