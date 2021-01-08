package Assignments.PE08;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Authorization and Authentication
 *  11/07/2019
 */

import java.util.ArrayList;
import java.util.Scanner;

public class DLUser {
    public  String userName;
    public  String access;
    public String name;
    public  String password;

    public DLUser()
    {

    }
//Constructor to initialize username and password
    public DLUser(String uname, String password) {

        setUserName(uname);
        setPassword(password);
    }
// getters and setters for users
    public  String getUserName()
    {

        return userName;
    }
    public String getAccess()
    {
        return access;
    }
    public String getName()
    {
        return name;
    }
    public  String getPassword()
    {
        return password;
    }

    public void setUserName(String uname)
    {

        this.userName=uname;
    }
    public void setAccess(String access)
    {
        this.access=access;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public  void setName(String name)
    {
        this.name=name;
    }

//login method
    public  boolean login() throws DLException {
        MySQLDatabase mysqldb= new MySQLDatabase();
        mysqldb.connect();
        boolean login=false;
        String dbPass="";

        ArrayList<ArrayList<String>>data=new ArrayList<ArrayList<String>>();
        // getting username,password and access from db using username from user
        data=mysqldb.getData("Select username,password,access,name from users where username=?",getUserName());
        //bad username
        if(data==null)
        {
            System.out.println("Bad username, Try again");
            System.exit(1);

        }
        else
        {
            dbPass=data.get(1).get(1);
        }
        if(getPassword().toLowerCase().equals(dbPass.toLowerCase())) //checking if user's password and password in db match
        {
            login=true;
            setAccess(data.get(1).get(2)); //setting access from db


            setName(data.get(1).get(3));
        }
        else
        {
            System.out.println("Wrong Password");
        }
        mysqldb.close();
        return login;
    }
}
