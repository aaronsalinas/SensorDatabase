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
	
	/**
	 * This function returns true if a admin account exists in database
	 * <p> Last Modified: Aaron D. Salinas
	 * On: 4/20/15 9:55AM 
	 * @return boolean - admin exists in database
	 */
	public static boolean checkIfAdminExists(String username){
		boolean adminExists = false;
		
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
				  + "FROM Administrator "
				  + "WHERE UserID = \'" + username + "\';";
				
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//Check if admin exists count > 0
				if(count > 0){
					adminExists = true;
				}
			}
		}
		catch(Exception exc){
			exc.printStackTrace();
			System.err.println("Error In Query");
		}
		finally{
			//Disconnect from Database
			try{myRs.close();} catch(Exception exc){};
			try{myStmt.close();} catch(Exception exc){}
			try{myConn.close();} catch(Exception exc){}
		}
		
		return adminExists;
	}
	
	
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
	
	/*
	 * Provided a filename, this function extracts information for a instrument model (taken from file)
	 * inserts the information into the table pertaining to the instrument model.
	 */
	public void extractFileData(String fileName){
		BufferedReader br = null;
		try {
 
			String sCurrentLine;
			String inputString;
			
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("*") == false){
					System.out.println(sCurrentLine + "\tnumber: " + i);
					i++; //Index number for entry
					System.in.read(); //Waits for user to press enter (TESTING)
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
