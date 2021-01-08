package Assignments.PE08;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Authorization and Authentication
 *  11/07/2019
 */

public class BLUser extends DLUser {
    public BLUser() {
        super();
    }

    public void BLUser()
    {

    }
    public BLUser(String uname,String password) {
        //calling super class constructor DLUser
        super(uname, password);

    }
}
