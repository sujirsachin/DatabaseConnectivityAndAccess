package Assignments.PE07;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Transactions
 *  10/31/2019
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

public class Equipment {
    public int equipmentID;
    public String equipmentName;
    public String equipmentDescription;
    public int equipmentCapacity;
    MySQLDatabase mysqldb=new MySQLDatabase();

    public Equipment() //default Constructor
    {


    }

    public Equipment(int eqid) // Constructor that accepts and sets equipment ID
    {
        this.equipmentID = eqid;
    } //Equipment Single parameter

    public Equipment(int eqID, String eqName, String eqDescription, int eqCapacity)
//Constructor that accepts and sets all attributes
    {
        this.equipmentID = eqID;
        this.equipmentName = eqName;
        this.equipmentCapacity = eqCapacity;
        this.equipmentDescription = eqDescription;
    } //Equipment multiple parameters

    //Mutators
    public void setEqID(int eqID) {
        this.equipmentID = eqID;
    }

    public void setEqName(String eqName) {

        this.equipmentName = eqName;
    }

    public void setEqDescription(String eqDescription) {

        this.equipmentDescription = eqDescription;
    }

    public void setEqCapacity(int eqCapacity) {
        this.equipmentCapacity = eqCapacity;
    }

    //Accessors
    public int getEqID() {
        return equipmentID;
    }

    public String getEqName() {

        return equipmentName;
    }

    public String getEqDescription() {

        return equipmentDescription;
    }

    public int getEquipmentCapacity() {

        return equipmentCapacity;
    }

    // Fetch Method
    /* public int fetch() throws DLException {
        try {
            mysqldb.connect();
            String result[][];


            result = mysqldb.getData("Select * from equipment where equipid=" + equipmentID, 4);
            if (result[0][0] == null) {
                System.out.println("Sorry, The database does not have data to display for this Equipment ID");
                return -1;
            } else {
                setEqID(Integer.parseInt(result[0][0]));
                setEqCapacity(Integer.parseInt(result[0][3]));
                setEqName(result[0][1]);
                setEqDescription(result[0][2]);
                System.out.println("Equipment ID: " + getEqID());
                System.out.println("Equipment Name: " + getEqName());
                System.out.println("Equipment Description: " + getEqDescription());
                System.out.println("Equipment Capacity: " + getEquipmentCapacity());
                mysqldb.close();
            }
        }
            catch (Exception ex)
            {
                throw new DLException(ex, "Unable to complete operation");
            }
            return 1;
        }  */


    //Put Method
    public void put() throws DLException {
        try {
            mysqldb.connect();
            String stmt = "Update equipment set EquipmentCapacity=?  where EquipID= ?";



           int count= mysqldb.setData(stmt,String.valueOf(getEquipmentCapacity()),String.valueOf(equipmentID));
            if(count<1)
            {
                System.out.println("No records updated");
            }
            else {
                System.out.println("Updated");
                System.out.println("Number of rows affected= " + count);

            }
           mysqldb.close();
        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }
        System.out.println("***************************************");
    }

    //Post Method
    public void post() throws DLException {
        try {
            mysqldb.connect();
            String stmt = "Insert into equipment values(?,?,?,?)";


           int count =mysqldb.setData(stmt,String.valueOf(equipmentID),equipmentName,equipmentDescription,String.valueOf(equipmentCapacity));
            if(count<1)
            {
                System.out.println("No records inserted");
            }
            else {
                System.out.println("Inserted");
                System.out.println("Number of rows affected= " + count);

            }

           mysqldb.close();
        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }
        System.out.println("***************************************");
    }
    //Delete method
    public void delete() throws DLException {
        try {
            mysqldb.connect();

            String stmt = "Delete from equipment where equipID=?";



           int count= mysqldb.setData(stmt,String.valueOf(equipmentID));
            if(count<1)
            {
                System.out.println("No records deleted");
            }
            else {
                System.out.println("Deleted");
                System.out.println("Number of rows affected= " + count);

            }

            mysqldb.close();
        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }
        System.out.println("***************************************");
    }

    public void fetch() throws DLException {
        try {


            mysqldb.connect();
            ArrayList<ArrayList<String>> result;


            result = mysqldb.getData("Select * from equipment where equipid=?",String.valueOf(equipmentID));
            if (result==null)   //check if result is null
                 {
                System.out.println("No Data found");
            }
                else {
                    for (int i = 0; i < result.size(); i++) {


                    ArrayList<String> record = result.get(i);

                    for (int j = 0; j < record.size(); j++) {
                        System.out.print(String.format("%-25s", record.get(j)));

                    }
                    System.out.println("");


                }
            }
            mysqldb.close();
                System.out.println("***************************************");
        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }
    }
public void swap(int equipid) throws DLException {
    //568 | Continental
    //894 | Bus 264
   try {
       ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
       int count1 = 0;
       int count2 = 0;
       mysqldb.connect();


       result = mysqldb.getData("Select equipmentname from equipment where equipID=? or equipID=?", String.valueOf(equipmentID), String.valueOf(equipid));
       // getting the equipment name for the two equip id's to swap


       if (result == null)   //check if result is null
       {
           System.out.println("No Data found");
       }
       mysqldb.startTrans();  //starting transaction before update
       String name1 = result.get(1).get(0);   //storing first equip id name to name1

       String name2 = result.get(2).get(0);   //storing second equip id name to name2

       String temp;
       temp = name1;                         // storing name1 to tempt
       name1 = name2;                       // storing name2 to name1( swap name2 to name1)
       name2 = temp;                        // storing temp to name2


       count1 = mysqldb.setData("Update equipment set equipmentname=? where equipid=?", name1, String.valueOf(equipmentID));  //swapping
       count2 = mysqldb.setData("Update equipment set equipmentname=? where equipid=?", name2, String.valueOf(equipid));      //swapping

       if (count1 < 1 || count2 < 1) { //check if both update has occurred
           mysqldb.rollbackTrans();     // even if one update failed, rollback(bad query)
           //System.out.println("Rolling back");
       }
       mysqldb.endTrans();              // ending the transaction
   }
   catch (Exception e)
   {
    throw new DLException(e,"Unable to process");
   }
mysqldb.close();

}

    }


