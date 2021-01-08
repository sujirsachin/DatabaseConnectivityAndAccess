package Assignments.PE08;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Authorization and Authentication
 *  11/07/2019
 */

public class Main {
    public static void main(String args[]) throws DLException {

        BLEquipment bleq=new BLEquipment(568); //initializing BLEquipment
        BLUser blUser=new BLUser("general1","justread"); //initializing BLUser as a general user
       boolean loggedIn= blUser.login(); //logging in
       if(loggedIn) {
           bleq.fetch(); //read
       }
       else
       {
           System.out.println("Please Login");
       }
        BLUser blUser1=new BLUser("admin1","admin"); // initializing admin

       boolean loggedIn1= blUser1.login(); //logging in
       if(loggedIn1) {


           bleq.save(blUser1,894); //checking access and swapping if admin or editor
           bleq.fetch(); //fetching result
       }
       else
       {
           System.out.println("Please Login"); //not admin or editor
       }

    }

}
