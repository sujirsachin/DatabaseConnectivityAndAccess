package Assignments.PE05;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 5- Exceptions
 *  10/11/2019
 */

public class Main {
    public static void main(String[] args) throws DLException {
        try {
            Equipment eq = new Equipment();

            Equipment eq1=new Equipment(1,"Air Bus","New Flight",100);


            eq1.post();
            eq1.fetch();
            eq1.delete(1);

            System.out.println("Column Name, Width: True");
            eq.fetch(true);

        }
        catch (Exception ex)
        {
            throw new DLException(ex, "Unable to complete operation");
        }

    }
}



