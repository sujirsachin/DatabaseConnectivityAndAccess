package Project;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *PapersAuthors - List of PaperAuthor objects
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri 
 */
public class PapersAuthors {
	
	ArrayList<PaperAuthor> paperAuthorsList = new ArrayList<PaperAuthor>();
	
	@JsonIgnore
	private MySQLDatabase dbObj = new MySQLDatabase();
	@JsonIgnore
	private User currentUser;
	
	public PapersAuthors() {		
	}
	
	public PapersAuthors(User user) {
		this.currentUser = user;
	}		
		
	/**
	 * Gets the authors of the paper by order
	 * @param paperId
	 * @return PapersAuthors object
	 * @throws DLException
	 */
	public PapersAuthors getPapersAuthorsByOrder(int paperId) throws DLException {	   		   
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr = "SELECT * FROM PaperAuthors WHERE paperId=? ORDER BY displayOrder";		   
			   args.add(String.valueOf(paperId));
			   ArrayList<ArrayList<String>> result= dbObj.getData(sqlStr, args);
			   for(int i=1,len=result.size(); i<len; i++) {
				   PaperAuthor pa = new PaperAuthor(Integer.parseInt(result.get(i).get(0)), Integer.parseInt(result.get(i).get(1)), Integer.parseInt(result.get(i).get(2)));
				   paperAuthorsList.add(pa);
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
	 * Gets the papers of the user
	 * @param userId
	 * @return PapersAuthors object
	 * @throws DLException
	 */
	public PapersAuthors getPapersAuthorsByUser(int userId) throws DLException {
		try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr = "SELECT * FROM PaperAuthors WHERE userId=?";		   
			   args.add(String.valueOf(userId));
			   ArrayList<ArrayList<String>> result= dbObj.getData(sqlStr, args);
			   if(result!=null) {
				   for(int i=1,len=result.size(); i<len; i++) {
					   PaperAuthor pa = new PaperAuthor(Integer.parseInt(result.get(i).get(0)), Integer.parseInt(result.get(i).get(1)), Integer.parseInt(result.get(i).get(2)));
					   paperAuthorsList.add(pa);
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
	 * Sets the authors for the paper
	 * @param paperId
	 * @param authorIds
	 * @return boolean(true/false)
	 * @throws DLException
	 */
	public boolean setPaperAuthors(int paperId, HashMap<Integer,Integer> authorIds) throws DLException {
		boolean isSet=false;
		try {			
			if(currentUser.getIsAdmin()==1 || checkIfAuthor(paperId,authorIds)) {		   
				boolean isConnected = dbObj.connect();
				if(isConnected) {
					ArrayList<String> args = new ArrayList<String>();
					String sqlStr = "INSERT INTO PaperAuthors(paperId, userId, displayOrder) VALUES";
					int count=0;
					for(int key: authorIds.keySet()) {						
						if(count==0) {
							sqlStr+="(?,?,?)";
						}else {
							sqlStr+=",(?,?,?) ";
						}
					   args.add(String.valueOf(paperId));
					   args.add(String.valueOf(authorIds.get(key)));
					   args.add(String.valueOf(key));
					   count++;
					}
					sqlStr+="ON DUPLICATE KEY UPDATE displayOrder=VALUES(displayOrder)";
					int rc = dbObj.setData(sqlStr, args);
					if(rc>0) {
						isSet=true;
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
	 * Check if author of the paper
	 * @param paperId
	 * @param authorsOrder
	 * @return if author or not
	 * @throws DLException
	 */
	public boolean checkIfAuthor(int paperId, HashMap<Integer,Integer> authorsOrder) throws DLException {
		boolean isAuthor=false;
		for(int order: authorsOrder.keySet()) {
			if(currentUser.getUserId()==authorsOrder.get(order)) {
				isAuthor=true;
			}
		}
		return isAuthor;
	}
	
	/**
	 * Deletes from PaperAuthors by paperId
	 * @param paperIds - list of paperIds
	 * @return if the author is deleted or not
	 * @throws DLException
	 */
	public boolean deleteAuthorsByPaper(ArrayList<Integer> paperIds) throws DLException {
		  boolean isDeleted=false;
		  try {
			  if(currentUser.getIsAdmin()==1) {
				  boolean isConnected = dbObj.connect();
				  if(isConnected && paperIds!=null && !paperIds.isEmpty()) {
			         //get users with paperIds			
			         String sqlStr="DELETE FROM PaperAuthors WHERE paperId IN(";
			         ArrayList<String> args = new ArrayList<String>();
			         for(int i=0;i<paperIds.size();i++){
			            if(i==0){
			               sqlStr+="?";
			            }
			            else{
			            sqlStr+=",?";
			            }
			            args.add(String.valueOf(paperIds.get(i)));
			         }
			         sqlStr+=");";         
			         int rc = dbObj.setData(sqlStr,args);
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
	 * Deletes from PaperAuthors by userId
	 * @param userIds
	 * @return if deleted or not
	 * @throws DLException
	 */
	public boolean deletePapersByUser(ArrayList<Integer> userIds) throws DLException {
	  boolean isDeleted=false;
	  try {
		  if(currentUser.getIsAdmin()==1) {
			  boolean isConnected = dbObj.connect();
			  if(isConnected && userIds!=null && !userIds.isEmpty()) {
		         //get users with paperIds	         
			     String sqlStr="DELETE FROM PaperAuthors WHERE userId IN (";
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
			         int rc = dbObj.setData(sqlStr,args);	         
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
     *Converts object to JSON string
     *@return JSON String representation
     */
	public String toString() {
	   String resultJson="";	   
	   ObjectToJson jsonObj =  new ObjectToJson(this.paperAuthorsList);
	   try {
		 resultJson = jsonObj.convertToJson();  
	   }
	   catch(Exception ex) {
		   System.out.println("problem in conversion "+ex.getMessage());		   
	   }
	   return resultJson;
   }
		
		

}
