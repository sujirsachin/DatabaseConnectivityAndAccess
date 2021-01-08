package Assignments.PE02;

public class ConnEstablish {
    public static void main(String[] args) {
        SQLServerDatabase sqlDB = new SQLServerDatabase();
        boolean checkSqlConn = sqlDB.connect();

        System.out.println("Connection Start: " +checkSqlConn);
        boolean checkSqlClose=sqlDB.closeConn();
        System.out.println("Connection Close: " +checkSqlClose);

        MySQLDatabase mysqlDB = new MySQLDatabase();
        boolean checkMySqlConn = mysqlDB.connect();

        System.out.println("Connection Start: "+checkMySqlConn );
        boolean checkMySqlClose=mysqlDB.closeConn();
        System.out.println("Connection Close: "+checkMySqlClose);
    }//end main

}
