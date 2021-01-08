package Project;

import java.util.*;
 import com.fasterxml.jackson.annotation.*;
 
 /**
  *Type - Information about submission types
  *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
  */
 public class Type {
   
    private int typeId;
    private String typeName;
    
    @JsonIgnore
    private Papers papers;        
    @JsonIgnore  
    private MySQLDatabase dbObj = new MySQLDatabase();
    @JsonIgnore
    private User currentUser;

    public Type()
    {     
    }
    
    public Type(User user) {
    	this.currentUser = user; 
    }
    
    public Type(int typeId)
    {
        this.typeId=typeId;
    }
    
    public Type(int typeId, String typeName)
    {
        this.typeId=typeId;
        this.typeName=typeName;
    }
    
    public void setTypeId(int typeId)
    {
        this.typeId=typeId;
    }

    public int getTypeId()
    {
        return typeId;
    }

    public void setTypeName(String typeName)
    {
        this.typeName=typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    /**
	 *GET type by ID
	 *@param typeId	
	 *@return Type object
	 */
   public Type getType(int typeId) throws DLException {	   
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   this.setTypeId(typeId);
		   this.setTypeName(null);
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * FROM _Types WHERE typeId=?";
			   args.add(String.valueOf(typeId));
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   this.setTypeId(Integer.parseInt(result.get(1).get(0)));
				   this.setTypeName(result.get(1).get(1));				   
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
	*GET type by name
	*@param typeName	
	*@return Type object
	*/
   public Type getTypeByName(String typeName) throws DLException {	   	   
	   ArrayList<ArrayList<String>> result = null;
	   try {
		   boolean isConnected = dbObj.connect();
		   if(isConnected) {			
			   ArrayList<String> args = new ArrayList<String>();
			   String sqlStr="SELECT * FROM _Types WHERE typeName=?";
			   args.add(typeName);
			   result = dbObj.getData(sqlStr,args);
			   if(result!=null && result.size()>1) {
				   this.setTypeId(Integer.parseInt(result.get(1).get(0)));
				   this.setTypeName(result.get(1).get(1));			   
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
	*Add new paper type
	*@param typeName	
	*@return boolean(true/false)
	*/
   public boolean addType(String typeName) throws DLException {	   	   	   
	   boolean isAdded=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {			   
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {		   
				   // get maximum id from the submission types
				   ArrayList<String> args = new ArrayList<String>();
				   int maxTypeId=0;
				   String sqlStr = "SELECT max(typeId) from _Types;";
				   ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
				   if(result!=null && result.size()>1) {
					   maxTypeId = Integer.parseInt(result.get(1).get(0)); 
				   }
				   sqlStr = "INSERT INTO _Types VALUES(?,?)";
				   this.setTypeId(maxTypeId+1);
				   this.setTypeName(typeName);
				   args.add(String.valueOf(maxTypeId+1));
				   args.add(typeName);
				   int rc = dbObj.setData(sqlStr,args);
				   if(rc==1) {
					   isAdded=true;
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
	*Set paper type, if exists, update else add new type
	*@param typeId
	*@param new typeName	
	*@return boolean(true/false)
	*/
   public boolean setType(int typeId, String typeName) throws DLException {    	
   	  boolean isSet = false;
   	  try {
   		if(currentUser.getIsAdmin()==1) {
   		  boolean isConnected = dbObj.connect();
	 	  if(isConnected) {
	 		  getType(typeId);	 		  
	 		  if(this.getTypeName()==null) {	 			  
	 			  isSet = addType(typeName);
	 		  } else {
	 			  isSet = updateType(typeId, typeName);
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
  	*Update existing type name
  	*@param typeId
  	*@param new typeName	
  	*@return boolean(true/false)
  	*/
   public boolean updateType(int typeId, String typeName) throws DLException {	   
	   boolean isUpdated = false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   this.setTypeId(typeId);
			   this.setTypeName(typeName);
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   ArrayList<String> args = new ArrayList<String>();
				   String sqlStr = "UPDATE _Types SET typeName = ? WHERE typeId = ?";
				   args.add(typeName);
				   args.add(String.valueOf(typeId));		   
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
 	*Delete existing type
 	*@param typeId 		
 	*@return boolean(true/false)
 	*/
   public boolean deleteType(int typeId) throws DLException{
	   boolean isDeleted=false;
	   try {
		   if(currentUser.getIsAdmin()==1) {
			   this.setTypeId(typeId);	   
			   boolean isConnected = dbObj.connect();
			   if(isConnected) {
				   dbObj.startTrans();		   
				   ArrayList<String> args = new ArrayList<String>();		
				   papers = new Papers(currentUser);
				   papers.deletePapersByType(typeId);		   
				   String sqlStr = "DELETE FROM _Types WHERE typeId = ?";		   
				   args.add(String.valueOf(typeId));
				   int rc = dbObj.setData(sqlStr,args);			  
				   if(rc==1) {
					   isDeleted=true;
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
   
   /*
	*Object to JSON conversion	
	*/
   public String toString() {
	   String resultJson="";	   
	   ObjectToJson jsonObj =  new ObjectToJson(this);	  
	   try {
		   resultJson = jsonObj.convertToJson();
	   } catch (DLException ex) {		
		   System.out.println("Couldn't convert to JSON."+ex.getMessage());
	   	}
	   return resultJson;
   }

}