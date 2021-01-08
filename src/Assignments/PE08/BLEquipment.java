package Assignments.PE08;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 7- Authorization and Authentication
 *  11/07/2019
 */


public class BLEquipment extends Equipment {
    BLUser blUser=new BLUser();

    public BLEquipment(int eqipID)
    {
        this.equipmentID=eqipID;
    }
    public boolean save(BLUser blUser1, int equipId) throws DLException {
        mysqldb.connect();
        boolean check=false;
        String access=blUser1.getAccess();
        //checking if access is admin or editor
        if((access.toLowerCase().equals("admin"))||(access.toLowerCase().equals("editor")))
        {
           this.swap(equipId); //calling swap from equipment
            mysqldb.close();
            return true;
        }
        else
        {
            System.out.println("Unauthorized User can't edit, only read");
            mysqldb.close();
            return false;
        }


    }

}


