package Project;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User - Information about a user registered for the conference
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class User {
   private int userId;
   private String lastName;
   private String firstName;
   private String email;
   private String pswd;
   private String canReview;
   private String expiration;
   private int isAdmin;
   private Affiliation affiliation;
   private int affiliationId;
   
   @JsonIgnore
   private Papers papers;       
   @JsonIgnore
   private MySQLDatabase dbObj = new MySQLDatabase();

   public User() {   
   }
   
   public User(int userId){
      this.userId = userId;
   }
   
   public User(int userId, String firstName, String lastName) {
	   this.userId = userId;
	   this.firstName = firstName;
	   this.lastName = lastName;
   }
   public User(int userId, String firstName, String lastName, String email) {
	   this.userId = userId;
	   this.firstName = firstName;
	   this.lastName = lastName;
	   this.email = email;
   }
   
   public User(int userId, String lastName, String firstName, String email, String pswd, String canReview, String expiration, int isAdmin) {
      this.userId = userId;
      this.lastName = lastName;
      this.firstName = firstName;
      this.email = email;
      this.pswd = pswd;
      this.canReview = canReview;
      this.expiration = expiration;
      this.isAdmin = isAdmin;			   
   }

   //GETTERS and SETTERS
   public int getUserId(){
      return this.userId;
   }

   public void setUserId(int userId){
      this.userId = userId;
   }

   public String getLastName(){
      return this.lastName;
   }

   public void setLastName(String lastName){
      this.lastName =lastName;
   }
   
   public String getFirstName(){
      return this.firstName;
   }

   public void setFirstName(String firstName){
      this.firstName =firstName;
   }
   
   public String getEmail(){
      return this.email;
   }

   public void setEmail(String email){
      this.email=email;
   }

   public String getPswd(){
      return this.pswd;
   }

   public void setPswd(String pswd){
      this.pswd =pswd;
   }

   public String getCanReview(){
      return this.canReview;
   }

   public void setCanReview(String canReview){
      this.canReview =canReview;
   }

   public String getExpiration(){
      return this.expiration;
   }

   public void setExpiration(String expiration){
      this.expiration =expiration;
   }

   public int getIsAdmin(){
      return this.isAdmin;
   }

   public void setIsAdmin(int isAdmin){
      this.isAdmin =isAdmin;
   }

   public int getAffiliationId(){
      return this.affiliationId;
   }

   public void setAffiliationId(int affiliationId){
      this.affiliationId =affiliationId;
   }

   public Affiliation getAffiliation() {
      return this.affiliation;
   }
   
   public void setAffiliation(Affiliation aff) {
      this.affiliation = aff;
   }

    /**
     *login
     *@param email
     *@param password
     *@return boolean(login success or not)
     */
    public boolean login(String email, String password) throws DLException {
	   boolean isAuthenticated=false;   
	   try {
	       boolean isConnected=dbObj.connect();
	       if(isConnected) {
	           String dbPass="";
	           ArrayList<String> args = new ArrayList<String>();
	           args.add(email);
	           ArrayList<ArrayList<String>>result=new ArrayList<ArrayList<String>>();
	           // getting email,password  from db using email from user
               result=dbObj.getData("Select userId, email,pswd,isAdmin from users where email=?",args);
               //bad username
               if(result==null || result.get(1).get(1)==null){
                   return isAuthenticated;
               }
               else{
                   dbPass=result.get(1).get(2); //getting the password
                 //checking if password in db match with the password the user has entered.
                   if(dbPass.equals(get_SHA_1_SecurePassword(password))){
                        isAuthenticated=true;
                        this.setUserId(Integer.parseInt(result.get(1).get(0)));
                        this.setEmail(result.get(1).get(1));
                        this.setPswd(result.get(1).get(2));
                        this.setIsAdmin(Integer.parseInt(result.get(1).get(3)));
		            }
               }
               dbObj.close();
	       }
	   }
	   catch(Exception ex) {
		   throw new DLException(ex, ex.getMessage());
	   }
	   return isAuthenticated;
   }

   /**
     *set new password
     *@param email
     *@param new password
     *@return boolean(is set or not)
     */
    public boolean setPassword(String email,String password) throws DLException {
        boolean isSetPassword=false;
        try {
            boolean isConnected = dbObj.connect();
            if (isConnected) {
                this.email = email;
                ArrayList<String> args = new ArrayList<String>();
                args.add(email);
                ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
                result = dbObj.getData("Select email,expiration from users where email=?", args); //get email and expiration from db
                if (result != null) {
                    String expiration = result.get(1).get(1); //get expiration
                    Timestamp currentTime = new Timestamp(new Date().getTime());  //current system time
                    Timestamp dbTime = expiration != null ? stringToDate(expiration) : currentTime;
                    // if current time is past the expiration time, pin expired
                    if (dbTime.equals(currentTime) || dbTime.after(currentTime)) {
                        Timestamp newTime = new Timestamp(new Date().getTime()); // get current system time
                        Calendar cal = Calendar.getInstance();
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTimeInMillis(newTime.getTime());
                        cal1.clear(Calendar.MILLISECOND);
                        cal.setTimeInMillis(newTime.getTime());
                        cal.add(Calendar.YEAR, 5);  // setting expiration to five years later in the future
                        cal.clear(Calendar.MILLISECOND);
                        newTime = new Timestamp(cal.getTime().getTime());
                        String newExpiration = dateToString(newTime); // converting the timestamp to string
                        String securePassword = get_SHA_1_SecurePassword(password); //hashing the password using SHA1
                        args.clear();
                        args.add(securePassword);
                        args.add(newExpiration);
                        args.add(email);
                        int rc = dbObj.setData("Update users set pswd=?, expiration=? where email=?", args); //update password and expiration
                        if (rc == 1) {
                            isSetPassword = true;
                        }
                    }
                }
                dbObj.close();
            }
        }
        catch (Exception e)
        {
            throw new DLException(e,e.getMessage());
        }
        return isSetPassword;
  }

   /**
     *Reset password
     *@param email
     *@return boolean(isReset or not)
     */
    public boolean resetPassword(String email) throws DLException {
        boolean isResetPassword=false;
        try {
            boolean isConnected=dbObj.connect();
            if(isConnected) {
        	    ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
                ArrayList<String> args=new ArrayList<String>();
                args.add(email);
                result=dbObj.getData("SELECT email,expiration from users where email=?",args); //get email and password from db
                if(result!=null)
                { //email exists in the db
                    Timestamp timestamp = new Timestamp(new Date().getTime()); //current system time
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(timestamp.getTime());
                    cal.add(Calendar.MINUTE, 5);  // setting 5 minutes for the pin to expire
                    cal.clear(Calendar.MILLISECOND);
                    timestamp = new Timestamp(cal.getTime().getTime());
                    String expiration=dateToString(timestamp); //current time to string
                    args.clear();
                    args.add(expiration);
                    args.add(email);
                    Email _email=new Email(); // calling email api to send email to user
                    _email.sendEmail(email); // sending email to the user.
                    int reset=dbObj.setData("Update users set expiration=? where email=?",args);
                    //updating expiration
                    if(reset==1) {
                	    isResetPassword=true;
                    }
                }
                dbObj.close();
            }
        }
        catch(Exception ex){
            throw new DLException(ex,"problem in reseting password");
        }
        return isResetPassword;
    }

    
   /**
     *GET user
     *@param userId 
     *@return User object
     */
    public User getUser(int userId) throws DLException {
         ArrayList<ArrayList<String>> resultList = null;
         try {
             boolean isConnected = dbObj.connect();
             if (isConnected) {                 
                 String sqlStr = "SELECT * FROM users WHERE userId=?";
                 ArrayList<String> paramValues = new ArrayList<String>();
                 paramValues.add(String.valueOf(userId));
                 resultList = dbObj.getData(sqlStr, paramValues);
                 if (resultList != null) {
                     this.setUserId(Integer.parseInt(resultList.get(1).get(0)));
                     this.setLastName(resultList.get(1).get(1));
                     this.setFirstName(resultList.get(1).get(2));
                     this.setEmail(resultList.get(1).get(3));
                     this.setPswd(resultList.get(1).get(4));
                     this.setCanReview(resultList.get(1).get(5));
                     this.setExpiration(resultList.get(1).get(6));
                     this.setIsAdmin(Integer.parseInt(resultList.get(1).get(7)));
                     this.setAffiliationId(Integer.parseInt(resultList.get(1).get(8)));
                     dbObj.close();
                 }
             }
         }
         catch (Exception e){
             throw new DLException(e,e.getMessage());
         }
      return this;
   }

   /**
     *Add new user
     *@param lastName 
     *@param firstName
     *@param email
     *@param pswd
     *@param canReview
     *@param expiration
     *@param isAdmin
     *@param affiliationId
     *@return boolean(true/false)
     */
    public boolean addUser(String lastName, String firstName, String email, String pswd, String canReview,String expiration,int isAdmin,int affiliationId) throws DLException {
	  boolean isAdded=false;
	  try {
          boolean isConnected = dbObj.connect();
          if (isConnected) {
              int maxUserId = 0;
              ArrayList<String> args = new ArrayList<String>();
              String sqlStr = "SELECT max(userId) FROM users";
              ArrayList<ArrayList<String>> result = dbObj.getData(sqlStr, args);
              if (result != null) {
                  maxUserId = Integer.parseInt(result.get(1).get(0)) + 1;
                  sqlStr = "INSERT INTO users(userId,lastName,firstName,email,pswd,canReview,expiration,isAdmin,affiliationId) VALUES(?,?,?,?,?,?,?,?,?)";
                  args.add(String.valueOf(maxUserId));
                  args.add(lastName);
                  args.add(firstName);
                  args.add(email);
                  args.add(get_SHA_1_SecurePassword(pswd)); // store hashed password
                  args.add(canReview);
                  args.add(expiration);
                  args.add(Integer.toString(isAdmin));
                  args.add(Integer.toString(affiliationId));
                  int rc = dbObj.setData(sqlStr, args);
                  if (rc == 1) {
                      isAdded = true;
                  }
              }
              dbObj.close();
          }
      }
	  catch (Exception e){
          throw new DLException(e,e.getMessage());
      }
     return isAdded;
   }

   /**
     *Update existing user
     *@param attributes - hash map of key-value pairs(attributes of User to update)
     *@return Types Boolean
     */
    public boolean updateUser(int userId, HashMap<String, String> attributes) throws DLException {
      boolean isUpdated=false;
      try {
    	  if(this.getIsAdmin()==1 || this.getUserId()==userId) {
    		  boolean isConnected = dbObj.connect();
              if (isConnected && !attributes.isEmpty()) {
                  String sqlStr = "UPDATE users SET ";
                  ArrayList<String> paramValues = new ArrayList<String>();
                  int count = 0, len = attributes.size();
                  for (String key : attributes.keySet()) {
                      if (count == len - 1) {
                          sqlStr += key + "=? ";
                      } else {
                          sqlStr += key + "=?, ";
                      }
                      paramValues.add(attributes.get(key));
                      count++;
                  }
                  sqlStr += "WHERE userId=?";
                  paramValues.add(String.valueOf(userId));
                  int rc = dbObj.setData(sqlStr, paramValues);
                  if (rc == 1) {
                      isUpdated = true;
                  }
                  dbObj.close();
              }
    	  }
      }
      catch (Exception e){
          throw new DLException(e,e.getMessage());
      }
      return isUpdated;
   }

   /**
     *Delete user
     *@param userId
     *@return Types Boolean
     */
    public boolean deleteUser(int userId) throws DLException {
	  boolean isDeleted = false;
	  try {
		  if(this.getIsAdmin()==1) {
			  boolean isConnected = false;
	          isConnected = dbObj.connect();
	          if (isConnected) {
	              dbObj.startTrans();
	              papers = new Papers(this);
	              papers.deletePapersByUser(new ArrayList<Integer>(Arrays.asList(userId)));
	              String sqlStr = "DELETE FROM users WHERE userId =?";
	              ArrayList<String> paramValues = new ArrayList<String>();
	              paramValues.add(String.valueOf(userId));
	              int rc = dbObj.setData(sqlStr, paramValues);
	              if (rc == 1) {
	                  isDeleted = true;
	              }
	              dbObj.endTrans();
	          }
		  }
	  }catch (Exception e){
		  dbObj.rollbackTrans();
	      throw new DLException(e,e.getMessage());
	  }
	  return isDeleted;
    }   
    
   /**
     *Hash password using SHA1
     *@return String hashed password
     */
    public static String get_SHA_1_SecurePassword (String passwordToHash) throws DLException { //hashing password algorithm
	      String generatePassword = null;
	      try {
	         MessageDigest md = MessageDigest.getInstance("SHA-1"); //method to hash- SHA-1
	         byte[] bytes = md.digest(passwordToHash.getBytes());
	         StringBuilder sb = new StringBuilder();
	         for (int i = 0; i < bytes.length; i++) {
	            sb.append((Integer.toString((bytes[i] & 0xff) +0x100,16).substring(1))); //converting to strong hash password
	         }
	         generatePassword=sb.toString();
	      
	      }
	      catch (NoSuchAlgorithmException e) {
	         throw new DLException(e,"No such Algorithm found");
	      }
	      return generatePassword;
   }

    /**
     *Date to string
     *@return Types String
     */
    public static String dateToString(Date date) throws DLException { //converting timestamp to string
	    SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat("YYYYMMddHHmmss");
            }
	    catch (Exception ex){
            throw new DLException(ex,ex.getMessage());
        }
	    return sdf.format(date);
   }

    /**
     *String to date conversion
     *@return Types Timestamp
     */
    public static Timestamp stringToDate(String date) throws  DLException { //converting string to a timestamp
      Timestamp timestamp=null;
      try {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
         Date parsedDate = dateFormat.parse(date);
         timestamp = new Timestamp(parsedDate.getTime());
      }
      catch (Exception ex){
         throw new DLException(ex,"Problem in parsing string to date.");
      }
      return timestamp;
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