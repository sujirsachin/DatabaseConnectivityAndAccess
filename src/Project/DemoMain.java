package Project;

import java.util.ArrayList;

/**
 *Class for DAO API demo
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 * */
class DemoMain{
	
   public static void main(String[] args) throws DLException {
      User u1=new User();
      
      //First sign up
       boolean isSignedUp=u1.addUser("Bagayatkar","Yash","y.r.bagayatkar@gmail.com","12345","1","20250101000000",0,0);
       System.out.println("signing up success: "+isSignedUp);
       	//resetting password
	     if(u1.resetPassword("y.r.bagayatkar@gmail.com")){
	        System.out.println("Password reset is requested");	        
	        //set password
	        if(u1.setPassword("y.r.bagayatkar@gmail.com","dsfthgw")){
	           System.out.println("Password is reset");
	        }
	        else{
	           System.out.println("Reset Pin Expired");
	        }
	     }
	     else{
	        System.out.println("Could not request reset password");
	     }
	     //login user
	     if(u1.login("y.r.bagayatkar@gmail.com","dsfthgw")){
	        //perform operations
	    	 System.out.println("login success");
	    	 Paper p = new Paper(u1);
	    	 String[] subjects = new String[3];
			 subjects[0]="Emerging Technologies";subjects[1]="Human-Computer Interaction";subjects[2]="Distance/Online Education";
			 String[] authorFName = new String[2];
			 String[] authorLName = new String[2];
			 authorFName[0]="Yash";authorLName[0]="Bagayatkar";authorFName[1]="Jessica";authorLName[1]="Nguyen";
			 System.out.println("paper new "+p.setPaper(120,"data new", "machinesadlearn", "Paper - Applied", "V6V14ZERZZ",subjects,authorFName,authorLName ));
				int[] typeIds = new int[3];
				typeIds[0]=1;typeIds[1]=2;typeIds[2]=3;
				Type t = new Type(u1);
				System.out.println(t.getType(1));
				Type t2 = new Type(u1);
				System.out.println(t2.getTypeByName("Panel"));
				Types t1 = new Types(u1);
				System.out.println("All types" +t1.getAllTypes());
				System.out.println("by id "+t1.getTypesById(typeIds));
				Subject s = new Subject(u1);
				System.out.println("by id" +s.getSubject(1));
				System.out.println(s.getSubjectByName("Information Assurance"));
				Subjects s1 = new Subjects(u1);
				int[] subj = new int[4];
				subj[0]=1;subj[1]=2;subj[2]=3;subj[3]=4;
				String[] subs = new String[4];
				subs[0]="Application Security";subs[1]="Human-Computer Interaction";subs[2]="Administration of IT Education";
				System.out.println("all subjects"+s1.getAllSubjects());								
				Affiliation a = new Affiliation();
				System.out.println("get aff "+a.getAffiliation(274));
				System.out.println("get aff by name "+a.getAffiliationByName("University of Washington"));
				String[] affArr = new String[3];
				affArr[0]="MAPUA UNIVERSITY";affArr[1]="Kyushu University";affArr[2]="Rochester Institute of Technology";
				int[] affiliationIds = new int[3];
				affiliationIds[0]=266;affiliationIds[1]=267;affiliationIds[2]=268;
				Affiliations a1 = new Affiliations();				
				System.out.println("get aff by id"+a1.getAffiliationsById(affiliationIds));
				Affiliations a2 = new Affiliations();
				System.out.println("get aff by name"+a2.getAffiliationsByName(affArr));
				Paper p1 = new Paper();
				System.out.println("get paper "+p1.getPaper(120));
				ArrayList<Integer> paperIds = new ArrayList<Integer>();
				paperIds.add(38);
				paperIds.add(9);
				paperIds.add(20);
				paperIds.add(83);
				paperIds.add(96);
				Papers p2 = new Papers();
				System.out.println("get all by id "+p2.getPapersById(paperIds));				
				Papers p3 = new Papers();
				System.out.println("get papers by subject "+p3.getPapersBySubject(1));
				ArrayList<Integer> userIds = new ArrayList<Integer>();
				userIds.add(1);
				userIds.add(2);
				userIds.add(3);
				User u = new User();
				System.out.println("user info "+u.getUser(4));				
				Users u5 = new Users();
				System.out.println("by ids "+u5.getUsersById(userIds));				
				Users u4 = new Users();
				System.out.println("affiliation "+u4.getUsersByAffiliation(77));				
	     }
      
      
      
      //ADMIN USER	     
	  System.out.println("ADMIN USER");   	           
      User u2=new User();
      //isSignedUp=false;
      //      First sign up
      isSignedUp=u2.addUser("Seshadri","Khavya","rkhavyaseshadri@gmail.com","54321","1","20260202000000",1,0);
      System.out.println("signing up "+isSignedUp);  
         //reset password
         if(u2.resetPassword("rkhavyaseshadri@gmail.com")){
            System.out.println("Password reset is requested");
            
            //set password
            if(u2.setPassword("rkhavyaseshadri@gmail.com","sdgsdg")){
               System.out.println("Password is set");
            }
            else{
               System.out.println("Reset Pin Expired");
            }
         }
         else{
            System.out.println("Could not request reset password");
         }
         //login
         if(u2.login("rkhavyaseshadri@gmail.com","sdgsdg")){
            //perform operations
        	 System.out.println("login success");
        	 Type t_1 = new Type(u2);
				System.out.println("adding"+t_1.addType("poster pr"));
				 Type t_2 = new Type(u2);
				System.out.println("update " +t_2.setType(t_2.getTypeId(), "poster presentation"));
				System.out.println("deletion "+t_1.deleteType(7));
				
				String[] newTypes = new String[2];
				newTypes[0]="Poster";
				newTypes[1]="Presentation";
				Types t_3 = new Types(u2);
				System.out.println("add "+t_3.addTypes(newTypes));
				 //Affiliation/Affiliations
				Affiliation aff = new Affiliation(u2);
				System.out.println("add aff "+aff.addAffiliation("Georgia Insitute"));
				Affiliation aff1 = new Affiliation(u2);
				System.out.println("set aff "+aff1.setAffiliation(300, "Georgia Insitute of tech"));
				Affiliation aff2 = new Affiliation(u2);
				System.out.println("delete affiliation "+aff2.deleteAffiliation(94));
				Paper pp = new Paper(u2);
				System.out.println("delete paper"+pp.deletePaper(120));				
         } else {
        	 System.out.println("login error");
         }            
   }
}
