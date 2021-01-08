package Assignments.PE03;/* Sachin Mohan Sujir
*  ISTE 722 Database Connectivity and Access
*  Practice Exercise 3- Data Retrieval
*  09/27/2019
 */

public class Main {
    public static void main(String[] args) {
        MySQLDatabase mysqldb=new MySQLDatabase();
        mysqldb.connect();
        System.out.println("****************");
        /*Instantiates the equipment data object,
        *sets its equipmentId,
        * calls the data object’s fetch method,
        * and then displays the values to the user
         */
        Equipment eq=new Equipment(894);
        eq.fetch();
        System.out.println("****************");
        /* Create a new equipment object setting all attributes to new values.
         *Use post to insert anew record,
         *then print out how many records were inserted */
        Equipment eq1=new Equipment(1,"Boeing 777","Passenger",200);
        eq1.post();
        System.out.println("****************");

        /* Use mutators to change the equipment capacity attribute,
        * and use put to update the record,
        * then printing how many records were update */
        eq1.setEqCapacity(50);
        eq1.put(1);
        System.out.println("****************");
        /* Use the fetch method and display the inserted information to the user */
        eq1.fetch();
        System.out.println("****************");
        /* Use the delete method to remove the record from the database,
        * printing how many records were deleted */
        eq1.delete(1);
        System.out.println("****************");
        /* Use the fetch method for this equipmentId to show a user-friendly message
        * when no data is retrieved */
        Equipment eq2=new Equipment(0);
        eq2.fetch();
        System.out.println("****************");
        mysqldb.closeConn();
    }
}
