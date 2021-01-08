package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 *Subjects - List of Subject objects
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
class Subjects{
   // List of Subject objects
   private ArrayList<Subject> subjects;
          
   @JsonIgnore
   private MySQLDatabase dbObj = new MySQLDatabase();
   @JsonIgnore
   private User currentUser;
   //Constructors
   public Subjects(){
	   subjects = new ArrayList<Subject>();
   }
   
   public Subjects(User user) {
	   subjects = new ArrayList<Subject>();
	   this.currentUser=user;
   }
   
   public Subjects(ArrayList<Subject> subjects){
      this.subjects = subjects;
   }
   
   //GETTERS AND SETTERS
   public ArrayList<Subject> getSubjects(){
	   return subjects;
   }
   
   public void setSubjects(ArrayList<Subject> subjects) {
	   this.subjects = subjects;
   }
   
   /**
	 *GET all subjects		
	 *@return Subjects object
	 */
   public Subjects getAllSubjects() throws DLException {	   
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * from _Subjects";		   
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null) {
				   subjects = new ArrayList<Subject>();
				   for(int i=1,len=result.size(); i<len; i++) {
					   Subject subj = new Subject(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
					   subjects.add(subj);
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
	 *GET Subjects by Ids
	 *@param array of subject ids
	 *@return Subjects object
	 */
   public Subjects getSubjectsById(int[] subjectIds) throws DLException {	   
	   ArrayList<ArrayList<String>> result=null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr = "SELECT * from _Subjects WHERE subjectId IN (";
			   for(int i=0, len=subjectIds.length; i<len; i++) {
				   if(i==0) {
					   sqlStr+="?";
				   }else {
					   sqlStr+=",?";
				   }
				   args.add(String.valueOf(subjectIds[i]));
			   }
			   sqlStr+=");";
			   result = dbObj.getData(sqlStr,args);
			   // populate the subjects DAO, traverse through the 2D structure
			   if(result!=null) {
				   subjects = new ArrayList<Subject>();
				   for(int i=1, len=result.size(); i<len; i++) {				   
					   Subject subj = new Subject(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
					   subjects.add(subj);
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
	 *GET subjects related to a paper
	 *@param paperId
	 *@return Subjects object
	 */
   public Subjects getSubjectsByPaper(int paperId) throws DLException {	     
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT s.subjectId, s.subjectName FROM PaperSubjects ps INNER JOIN _Subjects s USING(subjectId) WHERE ps.paperId=?";
			   args.add(String.valueOf(paperId));
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   subjects = new ArrayList<Subject>();
				   for(int i=1,len=result.size(); i<len; i++) {
					   Subject subj = new Subject(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
					   subjects.add(subj);
				   }
				   this.setSubjects(subjects);			  	   			
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
	 *GET subjects by name
	 *@param array of subject names 	
	 *@return Subjects object
	 */
   public Subjects getSubjectsByName(String[] subjectArr) throws DLException {	   
	   ArrayList<ArrayList<String>> result=null;
	   try {
		   MySQLDatabase db = new MySQLDatabase();
		   boolean isConnected = db.connect();
		   if(isConnected) {
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr = "SELECT * from _Subjects WHERE subjectName IN (";
			   for(int i=0, len=subjectArr.length; i<len; i++) {
				   if(i==0) {
					   sqlStr+="?";
				   }else {
					   sqlStr+=",?";
				   }
				   args.add(subjectArr[i]);
			   }
			   sqlStr+=");";
			   result = db.getData(sqlStr,args);
			   // populate the subjects DAO, traverse through the 2D structure
			   if(result!=null) {
				   subjects = new ArrayList<Subject>();
				   for(int i=1, len=result.size(); i<len; i++) {				   
					   Subject subj = new Subject(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1));
					   subjects.add(subj);
				   }			   
			   }
			   db.close();
		   }
	   }
	   catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	   }	   
	   return this;
   }
   
   /**
	 *set subjects for a paper
	 *@param paperId
	 *@param array of subject names 	
	 *@return boolean(true/false)
	 */
   public boolean setSubjectsByPaper(int paperId, String[] subjectName) throws DLException {
	   boolean isSet=false;
	   //get the existing subjects and check if the subjects are already in there, see what needs to be added in
	   try {
		   Paper p = new Paper(currentUser);
		   p.getPaper(paperId);
		   if(currentUser.getIsAdmin()==1 || currentUser.getUserId()==p.getSubmitterId()) {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {		   
				   subjects = new ArrayList<Subject>();
				   getSubjectsByName(subjectName);			   
				   if(subjects!=null && subjects.size()>0) {				   
					   isSet = this.updatePaperSubjects(paperId);
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
	 *update paper subjects for a paper
	 *@param paperId	 
	 *@return boolean(true/false)
	 */
   public boolean updatePaperSubjects(int paperId) throws DLException {
	   boolean isUpdated=false;
	   try {
		   Paper p = new Paper(currentUser);
		   p.getPaper(paperId);
		   if(currentUser.getIsAdmin()==1 || currentUser.getUserId()==p.getSubmitterId()) {
			   MySQLDatabase db = new MySQLDatabase();		   
			   boolean isConnected = db.connect();
			   if(isConnected) {
				   if(subjects!=null ) {				   				   
					   ArrayList<String> args = new ArrayList<String>();
					   String sqlStr = "INSERT INTO PaperSubjects(paperId, subjectId) VALUES";		   
					   for(int i=0,len=subjects.size(); i<len; i++) {
						   Subject sub = subjects.get(i);
						   if(i==0) {
							   sqlStr+="(?,?)";
						   }else {
							   sqlStr+=", (?,?)";
						   }
						   args.add(String.valueOf(paperId));
						   args.add(String.valueOf(sub.getSubjectId()));
					   }
					   sqlStr+=" ON DUPLICATE KEY UPDATE subjectId=VALUES(subjectId)";				  
					   int rc = db.setData(sqlStr, args);
					   if(rc>0) {
						   isUpdated = true;
					   }				   				   
				   }
				   db.close();
			   }		
		   }
	   }
	   catch(Exception ex) {		   
		   throw new DLException(ex, ex.getMessage());
	   }	   
	   return isUpdated;
   }
     
   /**
	 *add new subjects for a paper
	 *@param paperId	 
	 *@return boolean(true/false)
	 */
   public boolean addPaperSubjects(int paperId, String[] newSubjects) throws DLException {
	   boolean isAdded=false;
	   //add new subjects to the Subjects table
	   //add the relationship for the given paper
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {		   
				   dbObj.startTrans();		   
				   //adding new subjects to subject table
				   if(addSubjects(newSubjects)) {
					   //adding paper-subjects relation in paper subjects table
					   isAdded = updatePaperSubjects(paperId);   
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
	   return isAdded;
   }
   
   
   /**
	 *delete subjects relation for a paper
	 *@param list of paperIds	 
	 *@return boolean(true/false)
	 */
   public boolean deleteSubjectsByPaper(ArrayList<Integer> paperIds) throws DLException {
	   // deleted the relation from PaperSubjects for the given paper ID, 
	   boolean isDeleted=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {			   
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr="";			    	   
				   //delete from the PaperSubjects for that paperId attribute
				   if(paperIds!=null && !paperIds.isEmpty()) {				   
					   sqlStr = "DELETE FROM PaperSubjects WHERE paperId IN (";
					   args.clear();
					   for(int i=0,len=paperIds.size(); i<len; i++) {
						   if(i!=0) {
							   sqlStr+=",?";   
						   }else {
							   sqlStr+="?";
						   }
						   args.add(String.valueOf(paperIds.get(i)));
					   }
					   sqlStr+=")";
					   int rc = dbObj.setData(sqlStr, args);
					   if(rc>0) {
						   isDeleted=true;
					   }
				   }		   
				   dbObj.close();
			   }
		   }
	   }
	   catch(Exception ex) {		   
		   throw new DLException(ex, ex.getMessage());
	   }	   
	   return isDeleted;
   }

 
   
   /**
	 *add new subjects into _subjects
	 *@param array of subject names
	 *@return boolean(true/false)
	 */
   public boolean addSubjects(String[] newSubjects) throws DLException {
	   boolean isAdded=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   MySQLDatabase db = new MySQLDatabase();
			   boolean isConnected = db.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();		   
				   //get the max subjectID 
				   String sqlStr = "SELECT max(subjectId) FROM _Subjects";
				   ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
				   int maxSubjId = Integer.parseInt(result.get(1).get(0));
				   sqlStr = "INSERT INTO _Subjects(subjectId, subjectName) VALUES";
				   for(int i=0,len=newSubjects.length; i<len; i++) {
					   Subject sub= new Subject(maxSubjId+1, newSubjects[i]);
					   if(i==0) {
						   sqlStr+="(?,?)";   
					   } else {
						   sqlStr+=", (?,?)";
					   }
					   args.add(String.valueOf(++maxSubjId));
					   args.add(newSubjects[i]);
					   subjects.add(sub);
				   }
				   int rc = db.setData(sqlStr, args);
				   if(rc==newSubjects.length) {
					   isAdded=true;
				   }
				   db.close();
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
	   ObjectToJson jsonObj =  new ObjectToJson(this.subjects);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("Couldn't convert to JSON."+ex.getMessage());		   
	   }
	   return resultJson;
   }

}