package Assignments.PE04;/* Sachin Mohan Sujir
 *  ISTE 722 Database Connectivity and Access
 *  Practice Exercise 4- Meta Data
 *  09/30/2019
 */

public class Main {
    public static void main(String[] args) {

        Equipment eq=new Equipment();
        System.out.println("Column Name, Width: True");
        eq.fetch(true);
        System.out.println("Column Name, Width: False");
        eq.fetch(false);

    }
}



