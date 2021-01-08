package Assignments.PE09;/* Sachin Mohan Sujir
	*  ISTE 722 Database Connectivity and Access
	*  Practice Exercise 9- Backup
	*  12/05/2019
	*/
import java.io.*;
import java.util.*;


public class backup {

	String dbName="travel";
	static List<String> tables;
	MySQLDatabase mysql=new MySQLDatabase();

	public backup() {
		tables = new ArrayList<String>();
	}

	public List<String> getTables() throws DLException {
		boolean connection = mysql.connect();
		if(connection) {

			String query = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA=?";
			//query to get tables in a database
			ArrayList<ArrayList<String>> result = mysql.getData(query,dbName);
			for(int i=1;i<result.size(); i++) {
				tables.add(result.get(i).get(0)); //adding list of tables
			}

		}
		mysql.close();
		return tables;
	}

	public String getCreateTableQuery(int choice) throws DLException {
		String query="";
		String tableName = tables.get(choice-1); //table name based on choice-1 due to index 0
		HashMap<String,String> tableInfo = getTableInfo(tableName);
		String columnInfo = getColumnInfo(tableName);
		String primaryKey = getPrimaryKeys(tableName);
		String key = getNonUniqueKeys(tableName);
		String foreignKey = getForeignKeys(tableName);
		query+="CREATE TABLE `"+tableInfo.get("table")+"` (\n"+columnInfo;
		if(primaryKey.length()>0) {
			query+=",\n"+primaryKey;
		}
		if(key.length()>0) {
			query+=",\n"+key;
		}
		if(foreignKey.length()>0) {
			query+=",\n"+foreignKey+")";
		}

		return query;
	}

	public HashMap<String,String> getTableInfo(String tableName) throws DLException {
		boolean isConnected = mysql.connect();
		HashMap<String,String> tableInfo=null;
		if(isConnected) {
			String sqlStr = "SELECT TABLE_SCHEMA, TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";
			ArrayList<ArrayList<String>> result = mysql.getData(sqlStr, dbName,tableName);
			if(result!=null) {
				tableInfo =  new HashMap<String,String>();
				tableInfo.put("schema", result.get(1).get(0));
				tableInfo.put("table", result.get(1).get(1));
			}
			mysql.close();
		}
		return tableInfo;
	}

	public String getColumnInfo(String tableName) throws DLException {
		boolean connection = mysql.connect();
		String columns="";
		if(connection) {
			String sqlStr = "SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";

			ArrayList<ArrayList<String>> result = mysql.getData(sqlStr, dbName,tableName);
			if(result!=null) {
				for(int i=1;i<result.size(); i++) {
					columns+="`"+result.get(i).get(0)+"` "+result.get(i).get(1)+" ";
					if(result.get(i).get(2).equals("NO")) {
						columns+="NOT NULL ";
					}
					columns+="DEFAULT '"+result.get(i).get(3)+"'";
					if(i<(result.size()-1)) {
						columns+=",\n";
					}
				}
			}
			mysql.close();
		}
		return columns;
	}

	public String getPrimaryKeys(String tableName) throws DLException {
		boolean connection = mysql.connect();
		String primaryKeys = "";
		if(connection) {
			String sqlStr = "SELECT c.column_name, ct.constraint_type FROM information_schema.key_column_usage c INNER JOIN information_schema.TABLE_CONSTRAINTS ct USING(table_schema, table_name, constraint_name) WHERE table_schema=? AND table_name=? AND constraint_name=?";

			ArrayList<ArrayList<String>> result = mysql.getData(sqlStr, dbName,tableName,"Primary");
			if(result!=null) {
				primaryKeys+=result.get(1).get(1)+" (";
				for(int i=1;i<result.size(); i++) {
					primaryKeys+="`"+result.get(i).get(0)+"`";
					if(i<(result.size()-1)) {
						primaryKeys+=",";
					}
				}
				primaryKeys+=")";
			}

		}
		mysql.close();
		return primaryKeys;
	}

	public String getNonUniqueKeys(String tableName) throws DLException {
		boolean isConnected = mysql.connect();
		String keys = "";
		if(isConnected) {
			String sqlStr = "SELECT index_name, column_name FROM information_schema.statistics WHERE table_schema=? AND table_name=? AND non_unique=1";

			ArrayList<ArrayList<String>> result = mysql.getData(sqlStr, dbName,tableName);
			if(result!=null) {
				for(int i=1;i<result.size(); i++) {
					keys+="KEY `"+result.get(i).get(0)+"` (`"+result.get(i).get(1)+"`)";
					if(i<(result.size()-1)) {
						keys+=",\n";
					}
				}
			}

		}
		mysql.close();
		return keys;
	}

	public String getForeignKeys(String tableName) throws DLException {
		boolean isConnected = mysql.connect();
		String foreignKeys = "";
		if(isConnected) {
			String sqlStr="SELECT constraint_name, constraint_type, column_name, referenced_table_schema, referenced_table_name, referenced_column_name FROM information_schema.table_constraints  INNER JOIN information_schema.key_column_usage  USING(TABLE_SCHEMA,TABLE_NAME,CONSTRAINT_NAME) WHERE table_schema=? AND table_name=? AND constraint_type like \"FOREIGN%\"";
			ArrayList<String> paramValues = new ArrayList<String>();
			paramValues.add(dbName);
			paramValues.add(tableName);
			ArrayList<ArrayList<String>> result = mysql.getData(sqlStr, dbName,tableName);
			if(result!=null) {
				for(int i=1; i<result.size(); i++) {
					foreignKeys+="CONSTRAINT `"+result.get(i).get(0)+"` "+result.get(i).get(1)+ " (`"+result.get(i).get(2)+"`) REFERENCES `"+ result.get(i).get(3)+"`.`"+result.get(i).get(4)+"` (`"+result.get(i).get(5)+"`)";
					if(i<(result.size()-1)) {
						foreignKeys+=",\n";
					}
				}
			}

		}
		mysql.close();
		return foreignKeys;
	}

	public static void main(String[] args) throws DLException,IOException {
		backup back = new backup();
		back.getTables(); //retrieve table names from database
		String ch = "";
		try {
			do {
				System.out.println("Tables in travel database");
				System.out.println("+----------------+\n");
				for (int i = 0, len = tables.size(); i < len; i++) {
					System.out.print("| ");
					System.out.println(i + 1 + " \t" + tables.get(i)+"|");
				}
				System.out.println("+----------------+");
				System.out.println("Choose the table with the number");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int choice = Integer.parseInt(br.readLine());
				String query = back.getCreateTableQuery(choice);
				System.out.println("CREATE TABLE statement for : " + tables.get(choice - 1) + " is: \n\n" + query);
				System.out.println("\nDo you want to look up- choose 'y' or 'n'");
				ch = br.readLine();
			} while (ch.toLowerCase().equals("y"));
			System.out.println("Closed");
		}
		catch(Exception e)
		{
			throw new DLException(e,"Couldn't continue");
		}
	}
}
