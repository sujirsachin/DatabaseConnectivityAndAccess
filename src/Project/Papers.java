package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 *Papers - List of Paper objects
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Papers {

	private ArrayList<Paper> papers;
	
	@JsonIgnore
	private MySQLDatabase dbObj = new MySQLDatabase();	
	@JsonIgnore
	private User currentUser;
	
	public Papers(){
		papers = new ArrayList<Paper>();
	}
	
	public Papers(User user){
		papers = new ArrayList<Paper>();
		this.currentUser = user;
	}
	
	public ArrayList<Paper> getPapers() {
		return papers;
	}
	
	public void setPapers(ArrayList<Paper> papers) {
		this.papers = papers;
	}	
	
	/**
	 * Gets all the papers
	 * @return 2D list structure containing all the paper information
	 * @throws DLException
	 */
	public Papers getAllPapers() throws DLException {		 	   
		 ArrayList<ArrayList<String>> result = null;
		 try {
			 boolean isConnected = dbObj.connect();
			 if(isConnected) {
				 String sqlStr="SELECT * FROM Papers;";
				 ArrayList<String> args = new ArrayList<String>();
				 result = dbObj.getData(sqlStr,args);			   
				 for(int i=1,len=result.size(); i<len; i++) {
					 Paper p = new Paper(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), result.get(i).get(2), result.get(i).get(3), result.get(i).get(4), result.get(i).get(5)!=null?Integer.parseInt(result.get(i).get(5)):-1, result.get(i).get(6)!=null?Integer.parseInt(result.get(i).get(6)):-1, result.get(i).get(7), result.get(i).get(8));				   
					 papers.add(p);
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
	 * Gets the paper
	 * @param paperIds
	 * @return Paper object
	 * @throws DLException
	 */
	public Papers getPapersById(ArrayList<Integer> paperIds) throws DLException {		 	   
		 ArrayList<ArrayList<String>> result = null;
		 try {
			 boolean isConnected = dbObj.connect();
			 if(isConnected) {
				 String sqlStr="SELECT * FROM Papers WHERE paperId IN (";
				 ArrayList<String> args = new ArrayList<String>();
				 for(int i=0,len=paperIds.size(); i<len; i++) {
					 if(i==0) {
						 sqlStr+="?";					   
					 } else {
						 sqlStr+=",?";
					 }
					 args.add(String.valueOf(paperIds.get(i)));
				 }
				 sqlStr+=")";
				 result = dbObj.getData(sqlStr,args);		
				 papers = new ArrayList<Paper>();
				 for(int i=1,len=result.size(); i<len; i++) {
					 Paper p = new Paper(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), result.get(i).get(2), result.get(i).get(3), result.get(i).get(4), result.get(i).get(5)!=null?Integer.parseInt(result.get(i).get(5)):-1, result.get(i).get(6)!=null?Integer.parseInt(result.get(i).get(6)):-1, result.get(i).get(7), result.get(i).get(8));				   
					 papers.add(p);
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
	 * Gets the papers by subject 
	 * @param subjectId
	 * @return Papers object
	 * @throws DLException
	 */
	public Papers getPapersBySubject(int subjectId) throws DLException {
		//refer the subject id in paperSubjects and get all paper Ids and return the papers		
		ArrayList<ArrayList<String>> result= null;
		try {
			boolean isConnected = dbObj.connect();
			if(isConnected) {			
				ArrayList<String> args = new ArrayList<String>();
				String sqlStr = "SELECT paperId from PaperSubjects WHERE subjectId=?";
				args.add(String.valueOf(subjectId));
				result = dbObj.getData(sqlStr, args);
				if(result!=null) {
					ArrayList<Integer> paperIds = new ArrayList<Integer>();
					for(int i=1,len=result.size(); i<len; i++) {
						paperIds.add(Integer.parseInt(result.get(i).get(0)));
					}
					this.getPapersById(paperIds);
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
	 * Gets the paper
	 * @param userId
	 * @return Papers object
	 * @throws DLException
	 */
	public Papers getPapersByUser(int userId) throws DLException{
		ArrayList<ArrayList<String>> result= null;
		try {
			boolean isConnected = dbObj.connect();
			if(isConnected) {			
				ArrayList<String> args = new ArrayList<String>();
				String sqlStr = "SELECT * from papers WHERE submitterId=?";
				args.add(String.valueOf(userId));
				result = dbObj.getData(sqlStr, args);
				if(result!=null) {
					ArrayList<Integer> paperIds = new ArrayList<Integer>();
					for(int i=1,len=result.size(); i<len; i++) {
						paperIds.add(Integer.parseInt(result.get(i).get(0)));
					}
					this.getPapersById(paperIds);
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
	 * Gets the paper by authorId(userId)
	 * @param userId
	 * @return Papers object
	 * @throws DLException
	 */
	public Papers getPapersByAuthor(int userId) throws DLException {				
		ArrayList<ArrayList<String>> result= null;
		try {
			boolean isConnected = dbObj.connect();
			if(isConnected) {			
				ArrayList<String> args = new ArrayList<String>();
				String sqlStr = "SELECT paperId from PaperAuthors WHERE userId=?";
				args.add(String.valueOf(userId));
				result = dbObj.getData(sqlStr, args);
				if(result!=null) {
					ArrayList<Integer> paperIds = new ArrayList<Integer>();
					for(int i=1,len=result.size(); i<len; i++) {
						paperIds.add(Integer.parseInt(result.get(i).get(0)));
					}				
					this.getPapersById(paperIds);
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
	 * Gets the paper by submission type
	 * @param typeId
	 * @return Papers object
	 * @throws DLException
	 */
	public Papers getPapersByType(int typeId) throws DLException {		
		ArrayList<ArrayList<String>> result= null;
		try {
			boolean isConnected = dbObj.connect();
			if(isConnected) {			
				ArrayList<String> args = new ArrayList<String>();
				String sqlStr = "SELECT * from Papers WHERE submissionType=?";
				args.add(String.valueOf(typeId));
				result = dbObj.getData(sqlStr, args);
				if(result!=null) {
					for(int i=1, len=result.size(); i<len; i++) {
						Paper p = new Paper(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), result.get(i).get(2), result.get(i).get(3), result.get(i).get(4), result.get(i).get(5)!=null? Integer.parseInt(result.get(i).get(5)):-1, result.get(i).get(6)!=null?Integer.parseInt(result.get(i).get(6)):-1, result.get(i).get(7), result.get(i).get(8));
						papers.add(p);
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
	 * Deletes the paper by submissionType
	 * @param typeId
	 * @return if paper is deleted or not
	 * @throws DLException
	 */
	public boolean deletePapersByType(int typeId) throws DLException{
		boolean isDeleted=false;
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();
				if(isConnected) {
					dbObj.startTrans();
					///get the paperIDs of the submission type and then delete the subjects relation and authors relation for each paper
					ArrayList<String> args = new ArrayList<String>();
					String sqlStr = "SELECT paperId FROM Papers WHERE submissionType=?";
					args.add(String.valueOf(typeId));
					ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);			
					if(result!=null && result.size()>1) {
						ArrayList<Integer> paperIds = new ArrayList<Integer>();
						for(int i=1, len=result.size(); i<len; i++) {
							paperIds.add(Integer.parseInt(result.get(i).get(0)));
						}				
						// delete subject relation and authors relation for those papers				
						Subjects subjects = new Subjects(currentUser);				
						subjects.deleteSubjectsByPaper(paperIds); //deletes from paperSubjects
						PapersAuthors pa= new PapersAuthors(currentUser);
						pa.deleteAuthorsByPaper(paperIds); // deletes from PaperAuthors
						
						//after deletion from related tables
						sqlStr = "DELETE FROM papers WHERE submissionType=?";
						int rc = dbObj.setData(sqlStr, args);
						if(rc>0) {
							isDeleted=true;
						}				
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
	 * Deletes the paper
	 * @param subjectId
	 * @return if paper is deleted or not
	 * @throws DLException
	 */
	public boolean deletePapersBySubject(int subjectId) throws DLException{
		boolean isDeleted=false;
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();
				if(isConnected) {				
					///get the paperIDs of that subject and then delete the authors relation for each paper
					ArrayList<String> args = new ArrayList<String>();
					String sqlStr = "DELETE FROM PaperSubjects WHERE subjectId=?";
					args.add(String.valueOf(subjectId));
					int rc = dbObj.setData(sqlStr, args);
					if(rc>0) {
						isDeleted=true;
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
	 * Delets the papers by user
	 * @param userIds (list of userIds)
	 * @return If paper has been deleted or not
	 * @throws DLException
	 */	
	public boolean deletePapersByUser(ArrayList<Integer> userIds) throws DLException {
		boolean isDeleted=false;
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();			
				if(isConnected && !userIds.isEmpty()) {
					dbObj.startTrans();								
					// from paperauthors
					PapersAuthors pa = new PapersAuthors(currentUser);
					isDeleted = pa.deletePapersByUser(userIds);				
					//from papers for which the user is a submitter
					// select the paperId from papers where the user is a submitter
					ArrayList<String> args = new ArrayList<String>();
					String sqlStr = "SELECT paperId FROM Papers WHERE submitterId IN (";
					String paramStr="";
					for(int i=0;i<userIds.size();i++){
			            if(i==0){
			               paramStr+="?";
			            }
			            else{
			               paramStr+=",?";
			            }
			            args.add(String.valueOf(userIds.get(i)));
			         }
					sqlStr=sqlStr+paramStr+")";				
					ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
					if(result!=null) {
						ArrayList<Integer> paperIds = new ArrayList<Integer>();
						for(int i=1, len=result.size(); i<len; i++) {
							paperIds.add(Integer.parseInt(result.get(i).get(0)));
						}
						PapersAuthors pa_1 = new PapersAuthors(currentUser);
						isDeleted = pa_1.deleteAuthorsByPaper(paperIds);
						Subjects subs = new Subjects(currentUser);					
						subs.deleteSubjectsByPaper(paperIds);					
						sqlStr = "DELETE FROM Papers WHERE submitterId IN ("+paramStr+")";					
						int rc = dbObj.setData(sqlStr, args);					
						if(rc>0 || isDeleted) {
							isDeleted=true;
						}
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
	   ObjectToJson jsonObj =  new ObjectToJson(this.papers);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("problem in conversion "+ex.getMessage());		   
	   }
	   return resultJson;
   }

}
