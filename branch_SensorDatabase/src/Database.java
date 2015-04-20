import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This class stores functionality for interacting with the 
 * Sensor Database
 * @author Aaron D. Salinas
 *
 */
class Database implements DatabaseInformation{	
	
	/*
	 * This function tests for a successful database connection
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
}
