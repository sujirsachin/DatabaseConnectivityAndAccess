package Assignments.PE03;

/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 3- Data Retrieval
 *  09/27/2019
 */
public class Equipment {
    // Attributes in Equipment table
public int equipmentID;
public String equipmentName;
public String equipmentDescription;
public int equipmentCapacity;
public Equipment() //default Constructor
{ }
public Equipment(int eqid) // Constructor that accepts and sets equipment ID
{
this.equipmentID=eqid;
} //Equipment Single parameter
public Equipment(int eqID,String eqName,String eqDescription,int eqCapacity)
//Constructor that accepts and sets all attributes
{
this.equipmentID=eqID;
this.equipmentName=eqName;
this.equipmentCapacity=eqCapacity;
this.equipmentDescription=eqDescription;
} //Equipment multiple parameters

    //Mutators
    public void setEqID(int eqID)
    {
        this.equipmentID=eqID;
    }
    public void setEqName(String eqName)
    {

        this.equipmentName=eqName;
    }
    public void setEqDescription(String eqDescription)
    {

        this.equipmentDescription=eqDescription;
    }
    public void setEqCapacity(int eqCapacity)

    {
        this.equipmentCapacity=eqCapacity;
    }

    //Accessors
    public int getEqID()
    {
        return equipmentID;
    }
    public String getEqName()
    {

        return equipmentName;
    }
    public String getEqDescription()
    {

        return equipmentDescription;
    }
    public int getEquipmentCapacity()
    {

        return equipmentCapacity;
    }
// Fetch Method
    public int fetch()
    {
        String result[][];

        MySQLDatabase mysqldb=new MySQLDatabase();
        result=mysqldb.getData("Select * from equipment where equipid="+equipmentID,4);
        if(result[0][0]==null)
        {
            System.out.println("Sorry, The database does not have data to display for this Equipment ID");
            return -1;
        }
        else {
            setEqID(Integer.parseInt(result[0][0]));
            setEqCapacity(Integer.parseInt(result[0][3]));
            setEqName(result[0][1]);
            setEqDescription(result[0][2]);
            System.out.println("Equipment ID: " + getEqID());
            System.out.println("Equipment Name: " + getEqName());
            System.out.println("Equipment Description: " + getEqDescription());
            System.out.println("Equipment Capacity: " + getEquipmentCapacity());
return 1;
        }
    }
    //Put Method
    public void put(int eqID) {

        String stmt = "Update equipment set EquipmentCapacity=" + getEquipmentCapacity() + " where EquipID=" + eqID;

        MySQLDatabase mysqldb = new MySQLDatabase();
        System.out.println("Updated");
        mysqldb.setData(stmt);
    }

    //Post Method
    public void post()
    {
        String stmt="Insert into equipment values("+equipmentID+",'"+equipmentName+"','"+equipmentDescription+" ' ,  "+equipmentCapacity+")";
        MySQLDatabase mysqldb=new MySQLDatabase();
        System.out.println("Inserted");
        mysqldb.setData(stmt);
    }

    //Delete method
    public void delete(int eqID) {

        String stmt = "Delete from equipment where equipID=" + eqID;

        MySQLDatabase mysqldb = new MySQLDatabase();
        System.out.println("Deleted");
        mysqldb.setData(stmt);
    }
    } //class





