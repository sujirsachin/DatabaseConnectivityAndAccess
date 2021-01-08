package Assignments.PE06;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 6- Compiled Statements
 *  10/26/2019
 */

public class Main {
    public static void main(String[] args) throws DLException {
        try {
            Equipment eq = new Equipment(894);  //initializing equip id as 894
            eq.fetch();                              // fetch for equip id 894
            eq.setEqCapacity(200);                   // setting equip capacity to 200 for update

            eq.put();                               // updating equipment capacity where equip id is 894
            eq.fetch();                             // showing update
            Equipment eq1=new Equipment(1,"New Model","Faster",300);  // initializing all attributes for insert
            eq1.post();                             // inserting the new record
            eq1.fetch();                            // showing the newly added record
            eq1.delete();                           // deleting the new record to prevent duplicate key error during the next run
            eq.setEqCapacity(100);
            eq.put();                               // updating the equip id 894 back to 100 as I updated it to 200 to show the test case.(ready for one more run)


        }

        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }

    }
}



