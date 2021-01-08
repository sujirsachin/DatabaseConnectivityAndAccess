package Project;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 *PaperAuthor - Information about PaperAuthor table 
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class PaperAuthor {
	private int paperId, userId, displayOrder;
		
	@JsonIgnore
	private MySQLDatabase dbObj=new MySQLDatabase();	
	@JsonIgnore	
	private User currentUser;
	
	public PaperAuthor() {		
	}
	
	public PaperAuthor(User user) {		
		this.currentUser = user;
	}
	
	public PaperAuthor(int paperId, int userId) {
		this.paperId = paperId;
		this.userId = userId;
	}
	public PaperAuthor(int paperId, int userId, int displayOrder) {
		this.paperId = paperId;
		this.userId = userId;
		this.displayOrder = displayOrder;
	}
	
	public int getPaperId() {
		return this.paperId;
	}
	
	public void setPaperId(int paperId) {
		this.paperId = paperId;		
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getDisplayOrder() {
		return displayOrder;		
	}
	
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	/**
	 * Gets the paper author
	 * @param paperId
	 * @param userId
	 * @return PaperAuthor object
	 * @throws DLException
	 */
	public PaperAuthor getPaperAuthor(int paperId, int userId) throws DLException {
		ArrayList<ArrayList<String>> resultList = null;
		try {
			boolean isConnected = dbObj.connect();
			if(isConnected) {			
				String sqlStr = "SELECT * FROM paperAuthors WHERE paperId=? AND userId=?";
				ArrayList<String> args = new ArrayList<String>();
				args.add(String.valueOf(paperId));
				args.add(String.valueOf(userId));
				resultList = dbObj.getData(sqlStr, args);
				if(resultList!=null) {
				   this.setPaperId(Integer.parseInt(resultList.get(1).get(0)));
				   this.setUserId(Integer.parseInt(resultList.get(1).get(1)));			   			   					  
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
	 * Deletes a paper author
	 * @param paperId
	 * @param userId
	 * @return boolean - if deleted/not
	 * @throws DLException
	 */
	public boolean deletePaperAuthor(int paperId, int userId) throws DLException{
		boolean isDeleted = false;
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();
				if(isConnected) {
					String sqlStr = "DELETE FROM paperAuthors WHERE paperId =? AND userId=?";
					ArrayList<String> args = new ArrayList<String>();			
					args.add(String.valueOf(paperId));
					args.add(String.valueOf(userId));
					int rc = dbObj.setData(sqlStr, args);
					if(rc==1) {
						isDeleted = true;
					}
				}
			}						
		}
		catch(Exception ex) {
	   		throw new DLException(ex, ex.getMessage());
		}	
		return isDeleted;
	}
	
	/**
     *Convert to string
     *@return JSON String representation
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
