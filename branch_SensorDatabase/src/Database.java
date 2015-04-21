import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class stores functionality for interacting with the 
 * Sensor Database
 * @author Aaron D. Salinas
 *
 */
class Database implements DatabaseInformation{	
	
	/* *******************************************************
	 * 				Create Functions
	 *********************************************************/
	/**IN PROGRESS
	 * @return
	 */
	static public boolean createTable(){
		boolean success = false;
		String query = "CREATE TABLE IF NOT EXISTS `Sensor Database`.`SampleTable` (`Attribute1` INT NOT NULL,"
				+ "`Attribute2` VARCHAR(45) NOT NULL, PRIMARY KEY (`Attribute1`, `Attribute2`)) ENGINE = InnoDB";
		
		success = createTableQuery(query);
		
		return success;
	}
	
	/* *******************************************************
	 * 				Access Functions
	 *********************************************************/
	/**
	 * showTables()
	 * <p>
	 * This function displays all of the tables in the database to standard
	 * output
	 * @author Aaron D. Salinas
	 */
	public static void showTables(){
		try{
			//1. Get a connection to the database
			Connection myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
			//2. Create a statement
			Statement myStmt = myConn.createStatement();
			//3, Execute SQL query
			ResultSet myRs = myStmt.executeQuery("show tables");
			//4. Process the result set
			while(myRs.next()){
				System.out.println(myRs.getString("Tables_in_sensor database"));
			}
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public static boolean checkIfTableExists(String tableName){
		boolean exists = false;
		
		exists = checkIfTableExistsQuery(tableName);
		
		return exists;
	}
	
	/* *******************************************************
	 * 				Query Functions
	 *********************************************************/
	/**
	 * This function takes in a query as a string that is to create a new 
	 * table in the database. The string passed in should be in the appropriate
	 * SQL statement to create a new table, including all attributes and constraints.
	 * @param query
	 * @return
	 */
	static private boolean createTableQuery(String query){
		boolean success = true; //assume success;
		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		int myRs;
		
		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
		}
		catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBCONN_ERROR);
			success = false;
		}
		try{
			//2. Create a statement
			myStmt = myConn.createStatement();
		}
		catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBSTATEMENT_ERROR);
			success = false;
		}
		try{	
			myRs = myStmt.executeUpdate(query); //Execute Query (add new instrument)
		}
		catch(SQLException exc){
			exc.printStackTrace();
			System.out.println(DBQUERY_ERROR);
			success = false;
		}
		finally{
			//Disconnect from Database
			try{myStmt.close();} catch(Exception exc){}
			try{myConn.close();} catch(Exception exc){}
		}
		
		return success;
	}
	
	/**
	 * This function calls a query that checks if a table, whose name is passed
	 * into the function by the user, exists in the currently working database.
	 * Returns true if table exists, false if table doesn't exists or failure
	 * in query (in which a error message will result if failure)
	 * @param tableName
	 * @return
	 */
	static private boolean checkIfTableExistsQuery(String tableName){
		boolean tableExists = false;
		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		String query;
		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
			//2. Create a statement
			myStmt = myConn.createStatement();
			//3, Execute SQL query
			query = "SELECT COUNT(*) "
					+ "FROM information_schema.tables "
					+ "WHERE table_schema = '" + DBNAME + "' "
					+ "AND table_name = '" + tableName + "'";
			
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//Check if admin exists count > 0
				if(count > 0){
					tableExists = true;
				}
			}
		}
		catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBQUERY_ERROR);
		}
		finally{
			//Disconnect from Database
			try{myRs.close();} catch(Exception exc){};
			try{myStmt.close();} catch(Exception exc){}
			try{myConn.close();} catch(Exception exc){}
		}

		return tableExists;
	}
	
	
	
}



