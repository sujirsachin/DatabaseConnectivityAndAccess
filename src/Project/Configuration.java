package Project;

import java.util.*;
 import com.fasterxml.jackson.annotation.*;
 
 /**
  * Configuration - contains configuration information for a conference
  * @author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
  */
 public class Configuration {

    private String submissionOpen;
    private String submissionClose;
    private String reviewOpen;
    private String reviewClose;
    private String reviewerOpen;
    private String reviewerClose;
    private String fileUploadClose;
    private String PCEmail;
    private String PCName;
    private String PC2Email;
    private String PC2Name;
    private String shortName;
    private String logoFile;
    private String conferenceLocation;
    private String conferenceHost;
    private String conferenceHotel;
    private String conferenceURL;
    private String registrationURL;
    private String authorRegistartionClose;
    private String conferenceDates;
    private int configId;
     
    @JsonIgnore
    MySQLDatabase dbObj = new MySQLDatabase();    
    @JsonIgnore
    User currentUser;

    public Configuration()
    {
    }
    
    public Configuration(User user)
    {
    	this.currentUser = user;
    }
 
    public Configuration(int configId)
    {
        this.configId=configId;
    }
    
   public Configuration(String PCName,String PC2Name,int configId,  String PCEmail, String PC2Email, String shortName, String logoFile, String conferenceLocation,
                              String conferenceHost, String conferenceHotel, String conferenceURL, String registrationURL, String authorRegistartionClose,
                              String conferenceDates, String submissionOpen, String submissionClose, String reviewOpen, String reviewClose,
                              String reviewerOpen, String reviewerClose, String fileUploadClose) {       
        this.PCEmail=PCEmail;
        this.PC2Email=PC2Email;
        this.shortName=shortName;
        this.logoFile=logoFile;
        this.conferenceLocation=conferenceLocation;
        this.conferenceHost=conferenceHost;
        this.conferenceHotel=conferenceHotel;
        this.conferenceURL=conferenceURL;
        this.registrationURL=registrationURL;
        this.authorRegistartionClose=authorRegistartionClose;
        this.conferenceDates=conferenceDates;
        this.submissionOpen=submissionOpen;
        this.submissionClose=submissionClose;
        this.reviewOpen=reviewOpen;
        this.reviewClose=reviewClose;
        this.reviewerOpen=reviewerOpen;
        this.reviewerClose=reviewerClose;
        this.fileUploadClose=fileUploadClose;
        this.configId=configId;        
    }
    
    public void setPCName(String PCName){
        this.PCName=PCName;
    }

    public String getPCName(){
        return PCName;
    }
 
    public void setPC2Name(String PC2Name){
        this.PC2Name=PC2Name;
    }

    public String getPC2Name(){
        return PC2Name;
    }
        
    public  void setConfigId(int configId){
        this.configId=configId;
    }
    
    public int getConfigId(){
        return configId;
    }
    
    public void setPCEmail(String PCEmail){
        this.PCEmail=PCEmail;
    }

    public String getPCEmail(){
        return PCEmail;
    }

    public void setPC2Email(String PC2Email){
        this.PC2Email=PC2Email;
    }

    public String getPC2Email(){
        return PC2Email;
    }   
		   
    public void setShortName(String shortName){
        this.shortName=shortName;
    }

    public String getShortName(){
        return shortName;
    }

    public void setLogoFile(String logoFile){
        this.logoFile=logoFile;
    }

    public String getLogoFile(){
        return logoFile;
    }
       
    public void setConferenceLocation(String conferenceLocation){
        this.conferenceLocation=conferenceLocation;
    }

    public String getConferenceLocation(){
        return conferenceLocation;
    } 
      
    public void setConferenceHost(String conferenceHost){
        this.conferenceHost=conferenceHost;
    }

    public String getConferenceHost(){
        return conferenceHost;
    }    

    public void setConferenceHotel(String conferenceHotel){
        this.conferenceHotel=conferenceHotel;
    }

    public String getConferenceHotel(){
        return conferenceHotel;
    }   

    public void setConferenceURL(String conferenceURL){
        this.conferenceURL=conferenceURL;
    }

	public String getConferenceURL(){
	    return conferenceURL;
	}

	public void setRegistrationURL(String registrationURL){
        this.registrationURL=registrationURL;
	}

    public String getRegistrationURL(){
        return registrationURL;
    }

    public void setAuthorRegistartionClose(String authorRegistartionClose){
        this.authorRegistartionClose=authorRegistartionClose;
    }

    public String getAuthorRegistartionClose(){
        return authorRegistartionClose;
    }

    public void setConferenceDates(String conferenceDates){
        this.conferenceDates=conferenceDates;
    }

    public String getConferenceDates(){
        return conferenceDates;
    }
    
    public void setSubmissionOpen(String submissionOpen){
        this.submissionOpen=submissionOpen;
    }

    public String getSubmissionOpen(){
        return submissionOpen;
    }

    public void setSubmissionClose(String submissionClose){
        this.submissionClose=submissionClose;
    }

    public String getSubmissionClose(){
        return submissionClose;
    }

    public void setReviewOpen(String reviewOpen){
        this.reviewOpen=reviewOpen;
    }

    public String getReviewOpen(){
        return reviewOpen;
    }
    
    public void setReviewClose(String reviewClose){
        this.reviewClose=reviewClose;
    }

    public String getReviewClose(){
        return reviewClose;
    }

    public void setReviewerOpen(String reviewerOpen){
        this.reviewerOpen=reviewerOpen;
    }

    public String getReviewerOpen(){
        return reviewerOpen;
    }
    
    public void setReviewerClose(String reviewerClose){
        this.reviewerClose=reviewerClose;
    }

    public String getReviewerClose(){
        return reviewerClose;
    }

    public void setFileUploadClose(String fileUploadClose){
        this.fileUploadClose=fileUploadClose;
    }

    public String getFileUplaodClose(){
        return fileUploadClose;
    }

    /**
	 * Gets the configuration
     * @param configId
	 * @return 2D list structure containing the Configuration information
	 * @throws DLException 
	 */    
	public Configuration getConfiguration(int configId) throws DLException, Exception {		
		ArrayList<ArrayList<String>> resultList = null;
		try {
			boolean isConnected = dbObj.connect();      
			if(isConnected) {			
				String sqlStr = "SELECT * FROM _configuration WHERE configId=?";			
				ArrayList<String> paramValues = new ArrayList<String>();
				paramValues.add(String.valueOf(configId));         
				resultList = dbObj.getData(sqlStr,paramValues);         
				if(resultList!=null) {
					this.setSubmissionOpen(resultList.get(1).get(0));
					this.setSubmissionClose(resultList.get(1).get(1));	
					this.setReviewOpen(resultList.get(1).get(2));
					this.setReviewClose(resultList.get(1).get(3));
					this.setReviewerOpen(resultList.get(1).get(4));
					this.setReviewerClose(resultList.get(1).get(5));
					this.setFileUploadClose(resultList.get(1).get(6));
					this.setPCEmail(resultList.get(1).get(7));
					this.setPCName(resultList.get(1).get(8));
					this.setPC2Email(resultList.get(1).get(9));
					this.setPC2Name(resultList.get(1).get(10));
					this.setShortName(resultList.get(1).get(11));
					this.setLogoFile(resultList.get(1).get(12));
					this.setConferenceLocation(resultList.get(1).get(13));
					this.setConferenceHost(resultList.get(1).get(14));
					this.setConferenceHotel(resultList.get(1).get(15));
					this.setConferenceURL(resultList.get(1).get(16));
					this.setRegistrationURL(resultList.get(1).get(17)); 
					this.setAuthorRegistartionClose(resultList.get(1).get(18));
					this.setConferenceDates(resultList.get(1).get(19));
					this.setConfigId(Integer.parseInt(resultList.get(1).get(20)));				
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
	 * Updates any number of attributes in _configuration
	 * @param configId	
	 * @param attributes (hashmap with key and value)
	 * @return number of records affected
	 * @throws DLException 
	 */        
 	public boolean updateConfiguration(int configId, HashMap<String, String> attributes) throws DLException {
		boolean isUpdated=false;
		try {
			if(currentUser.getIsAdmin()==1) {
				boolean isConnected = dbObj.connect();
				if(isConnected) {
					String sqlStr = "UPDATE _configuration SET ";
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
					sqlStr+="WHERE configId=?";				
					paramValues.add(String.valueOf(configId));
					int rc = dbObj.setData(sqlStr, paramValues);
					if(rc==1) {
						isUpdated=true;
					}
				}
			}			
		}
		catch(Exception ex) {
			throw new DLException(ex, ex.getMessage());
		} 		
		return isUpdated;
	}
 	
 	/**
     *Convert to string
     *@return JSON string representation
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