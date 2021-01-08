package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Paper - Information about a paper submitted in a conference
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Paper {
	
	private int paperId, submitterId, submissionType;
	private String title, submissionAbstract, track, status, tentativeStatus, fileId;
	
	@JsonIgnore	
	private Subjects subjects; //many-many(Paper->Subjects)
	@JsonIgnore	
	private Type type; // many-one(Paper->type)
	@JsonIgnore	
	private Users authors; 	//many-many(Paper->Users)
	@JsonIgnore	
	private PapersAuthors pa; //PaperAuthors	
	@JsonIgnore	
	private MySQLDatabase dbObj = new MySQLDatabase();
	@JsonIgnore
	private User currentUser; //currently logged-in user
	
	public Paper() {	
	}
	
	public Paper(User user) {
		this.currentUser=user;
	}
		
	public Paper(int submissionId) {
		this.paperId = submissionId;
	}
	
	public Paper(int submissionId, String submissionTitle, String paperAbstract, String track, String status, int submissionType, int submitterId, String fileId, String tentativeStatus) {
		this.paperId = submissionId;
		this.title = submissionTitle;
		this.submissionAbstract = paperAbstract;
		this.track = track;
		this.status = status;
		this.submissionType = submissionType;
		this.submitterId = submitterId;
		this.fileId = fileId;
		this.tentativeStatus = tentativeStatus;
	}
	
	public int getPaperId() {
		return paperId;
	}
	
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSubmissionAbstract() {
		return submissionAbstract;
	}
	
	public void setAbstract(String submissionAbstract) {
		this.submissionAbstract = submissionAbstract;
	}
	
	public String getTrack() {
		return track;
	}
	
	public void setTrack(String track) {
		this.track = track;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getTentativeStatus() {
		return tentativeStatus;
	}
	
	public void setTentativeStatus(String tentativeStatus) {
		this.tentativeStatus = tentativeStatus;
	}
	
	public int getSubmitterId() {
		return submitterId;
	}
	
	public void setSubmitterId(int submitterId) {
		this.submitterId = submitterId;
	}
	
	public int getSubmissionType() {
		return submissionType;
	}
	
	public void setSubmissionType(int submissionType) {
		this.submissionType = submissionType;
	}
	
	public Subjects getSubjects() throws DLException {
		subjects = new Subjects();
		subjects.getSubjectsByPaper(this.paperId);
		return subjects;
	}
	
	public void setSubjects(int paperId, String[] subjects) throws DLException {
		this.subjects = new Subjects(currentUser);
		this.subjects.setSubjectsByPaper(paperId, subjects);
	}
	
	public void setType(String typeName) throws DLException{
		type = new Type(currentUser);
		type.getTypeByName(typeName);		
	}
	
	public Type getType(int typeId) throws DLException {
		type = new Type(currentUser);
		type.getType(typeId); 
		return type;
	}
	
	public void setAuthors(int paperId, String[] firstNames, String[] lastNames) throws DLException {
		this.authors = new Users(currentUser);
		this.authors.setUsersByPaper(paperId, firstNames, lastNames);
	}
	
	public Users getAuthors() {
		return authors;
	}
	
	
	/**
	 * Gets the paper
	 * @param submissionId(paperId) 
	 * @return Paper object
	 * @throws DLException 
	 */
	public Paper getPaper(int paperId) throws DLException {			
		ArrayList<ArrayList<String>> resultList = null;
		try {
			MySQLDatabase db = new MySQLDatabase();
			boolean isConnected = db.connect();
			if(isConnected) {			
				String sqlStr = "SELECT * FROM papers WHERE paperId=?";
				ArrayList<String> paramValues = new ArrayList<String>();
				paramValues.add(String.valueOf(paperId));
				resultList = db.getData(sqlStr, paramValues);
				if(resultList!=null) {
				   this.setPaperId(Integer.parseInt(resultList.get(1).get(0)));
				   this.setTitle(resultList.get(1).get(1));	
				   this.setAbstract(resultList.get(1).get(2));
				   this.setTrack(resultList.get(1).get(3));
				   this.setStatus(resultList.get(1).get(4));
				   //get the submission type details from Type
				   this.setSubmissionType(resultList.get(1).get(5)!=null? Integer.parseInt(resultList.get(1).get(5)):-1);			   
				   this.setSubmitterId(resultList.get(1).get(6)!=null? Integer.parseInt(resultList.get(1).get(6)):-1);
				   this.setFileId(resultList.get(1).get(7));
				   this.setTentativeStatus(resultList.get(1).get(8));			   
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
	 * Updates the paper information if existing, else adds new paper
	 * @return number of records affected
	 * @throws DLException 
	 */
	public boolean setPaper(int submissionId, String submissionTitle, String submissionAbstract, String type, String fileName, String[] subjects, String[] authorFName, String[] authorLName) throws DLException {		
 	   	  boolean isSet=false;
 	   	  try {
 	   		boolean isConnected = dbObj.connect();
 		   	  if(isConnected) {  		  
 		   		  getPaper(submissionId);	   		  
 		   		  if(this.getPaperId()==0) {	   			  
 		   			  addPaper(submissionTitle, submissionAbstract, type, fileName, subjects, authorFName, authorLName);
 		   		  } else {	   			  
 		   			  dbObj.startTrans(); 		   			  
 		   			  this.setType(type);
 		   			  this.setSubjects(paperId,subjects);	   			  
 		   			  //set the authors too
 		   			  this.setAuthors(paperId, authorFName, authorLName);
 		   			  HashMap<String,String> attrMap = new HashMap<String,String>();
 		   			  attrMap.put("title", submissionTitle);
 			   		  attrMap.put("abstract", submissionAbstract);
 			   		  attrMap.put("fileId", fileName);	
 			   		  attrMap.put("submissionType", String.valueOf(this.type.getTypeId()));
 			   		  attrMap.put("submitterId", String.valueOf(currentUser.getUserId()));
 		   			  isSet = this.updatePaper(submissionId,attrMap);	   			  
 		   			  dbObj.endTrans();
 		   		  }			  
 				  dbObj.close();
 		   	  } 	   		  
 	   	  }
 	   	  catch(Exception ex) {
 	   		  dbObj.rollbackTrans();
 	   		  throw new DLException(ex, ex.getMessage());
 	   	  }		 
	   	  return isSet;
	}
	
	/**
	 * Adding new paper
	 * @param submissionTitle
	 * @param submissionAbstract
	 * @param type(submission type)
	 * @param fileName
	 * @param subjects
	 * @param authors first names
	 * @param authors last names
	 * @return boolean (true/false)
	 * @throws DLException 
	 */
	public boolean addPaper(String submissionTitle, String submissionAbstract, String type, String fileName, String[] subjects, String[] authorFName, String[] authorLname) throws DLException {
		  boolean isAdded=false;
		  try {
			  MySQLDatabase db = new MySQLDatabase();
		   	  boolean isConnected = db.connect();
		   	  if(isConnected) {	   		  
		   		  ArrayList<String> args = new ArrayList<String>();
		   		  String sqlStr = "SELECT max(paperId) FROM papers";
		   		  ArrayList<ArrayList<String>> result = db.getData(sqlStr, args);
		   		  if(result!=null ) {
		   			  int submissionId = Integer.parseInt(result.get(1).get(0))+1;	   			  
		   			  this.setType(type);
		   			  sqlStr = "INSERT INTO papers(paperId,title,abstract,submissionType,fileId, submitterId) VALUES(?,?,?,?,?,?)";
			   		  args.clear();
		   			  args.add(String.valueOf(submissionId));
			   		  args.add(submissionTitle);
			   		  args.add(submissionAbstract);
			   		  args.add(String.valueOf(this.type.getTypeId()));
			   		  args.add(fileName);
			   		  args.add(String.valueOf(currentUser.getUserId()));
			   		  int rc = db.setData(sqlStr,args);		   		  
			   		  if(rc>0) {
			   			  isAdded=true;
			   		  }
			   		  db.startTrans(); 
			   		  this.setSubjects(submissionId, subjects); //set subjects
		   			  this.setAuthors(submissionId, authorFName, authorLname); // set authors	
		   			  db.endTrans();  			  
			   		  if(this.subjects.getSubjects().size()>0 && this.type.getTypeName()!=null) {
			   			  isAdded = true;
			   		  }		   		  		   		  
		   		  }	   		  
		   		  db.close();
		   	  }			  
		  }
		  catch(Exception ex) {
			  dbObj.rollbackTrans();
	   		  throw new DLException(ex, ex.getMessage());
	   	  }		 
	   	 return isAdded;
	}
	
	/**
	 * Updating existing paper	
	 * @param hash map of attributes to update
	 * @param authors first names
	 * @param authors last names
	 * @return boolean (true/false)
	 * @throws DLException 
	 */
	public boolean updatePaper(int paperId, HashMap<String, String> attributes) throws DLException {
		boolean isUpdated=false;
		boolean isAuthorized=false;
		try {
			authors = new Users();
			authors.getUsersbyPaper(paperId); 
			ArrayList<User> paperAuthors = authors.getUsers();			
			//authorization check if current user is one of the paper's authors
			for(int i=0,len=paperAuthors.size(); i<len; i++) {
				if(currentUser.getUserId()==paperAuthors.get(i).getUserId()) {
					isAuthorized=true;
				}
			}
			if(isAuthorized) {				
				MySQLDatabase db = new MySQLDatabase();		
				boolean isConnected = db.connect();
				if(isConnected && attributes.size()>0) {
					String sqlStr = "UPDATE papers SET ";
					ArrayList<String> paramValues = new ArrayList<String>();
					int count=0, len=attributes.size();
					for(String key : attributes.keySet()) {
						if(count==len-1) {
							sqlStr+=key+"=? ";
						} else {
							sqlStr+=key+"=?, ";
						}								
						paramValues.add(attributes.get(key));
						count++;
					}
					sqlStr+="WHERE paperId=?";			
					paramValues.add(String.valueOf(paperId));
					int rc = db.setData(sqlStr, paramValues);
					if(rc==1) {
						isUpdated=true;
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
	 * Deletes the given paper
	 * @param submissionId(paperId)
	 * @return number of records affected
	 * @throws DLException
	 */
	public boolean deletePaper(int paperId) throws DLException {
		boolean isDeleted = false;	
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();
				if(isConnected) {			
					dbObj.startTrans();
					subjects = new Subjects(currentUser);
					subjects.deleteSubjectsByPaper(new ArrayList<Integer>(Arrays.asList(paperId))); //delete relation
					// delete authors relation from PaperAuthors
					pa = new PapersAuthors(currentUser);
					pa.deleteAuthorsByPaper(new ArrayList<Integer>(Arrays.asList(paperId)));
					String sqlStr = "DELETE FROM papers WHERE paperId =?";
					ArrayList<String> paramValues = new ArrayList<String>();			
					paramValues.add(String.valueOf(paperId));
					int rc = dbObj.setData(sqlStr, paramValues);
					if(rc==1) {
						isDeleted=true;
					}
					//delete subjects and authors relation for the paper			
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
	 * @return JSON strings
	 * @throws DLException
	 */
	 public String toString() {
	   String resultJson="";						
	   ObjectToJson jsonObj =  new ObjectToJson(this);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("problem in conversion "+ex.getMessage());		   
	   }
	   return resultJson;
	 }
	
}
