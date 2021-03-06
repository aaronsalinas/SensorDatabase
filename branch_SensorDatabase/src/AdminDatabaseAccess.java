/******************************************************************************
 * Filename: AdminDatabaseAccess.java
 * Author: Aaron D. Salinas (Aaron_Salinas@baylor.edu)
 * Description: This class holds methods that access the Administrative information
 * stored in the database. Functionality includes ability to add/modify/delete
 * administrative information as well as ability to check if any specific
 * information is stored in the database.
 * Created: 4/28/2015
 * Modified:5/4/2015
******************************************************************************/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AdminDatabaseAccess extends Database{
	
	
	/**
	 * addAdministrator
	 * <p>
	 * This function calls another function which performs insertion of a new
	 * administrator if the administrator doesn't exist in the database.
	 * 
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return <b>true</b> - admin added into database <br>
	 * 		   <b>false</b> - admin not added
	 */
	public static boolean addAdministrator(String username, String password, String firstName, 
																		String lastName, String email){
		boolean success = false; //denotes success/failure of add
		
		//Check if administrator is not already in the system
		if(checkIfAdminExists(username) == false){
			//Add administrator into database
			success = addAdministratorQuery(username, password, firstName, lastName, email);
			if(success == false){
				System.err.println("Error in Adding New Administrator");
			}
		}
		else{
			System.out.println("User already exists in the Database!");
		}
		return success;
		
	}
	
	/**
	 * This function calls a query function which removes a administrator from the database
	 * using a provided username from the user.
	 * @param username
	 * @return
	 */
	public static boolean removeAdministrator(String username){
		boolean success = false;
		
		//Check if administrator to remove exists in the database
		if(checkIfAdminExists(username) == true){
			success = removeAdministratorQuery(username);
		}
		
		return success;
	}
	
	public static boolean changePassword(String username, String newPassword){
		boolean success = true;//Assume success
		
		//Call query to change password
		success = changePasswordQuery(username, newPassword);	
		
		return success;
	}
	
	/**
	 * checkIfAdminExists
	 * <p>
	 * This function returns true if a administrator account exists in the
	 * database.
	 * <p>
	 * Last Modified: Aaron D. Salinas On: 4/20/15 9:55AM
	 * 
	 * @return boolean - admin exists in database
	 */
	public static boolean checkIfAdminExists(String username){
		boolean adminExists = false;
		
		
		String query = "SELECT COUNT(*) "
				  + "FROM Administrator "
				  + "WHERE UserID = \'" + username + "\';";
		
		adminExists = checkIfExists(query);

		return adminExists;
	}
	
	/**
	 * retrieveUserPassword
	 * <p>
	 * This function returns an administrators password to the user after calling a function
	 * which queries for the password using a provided username from the user.
	 * @param username
	 * @return
	 */
	public static String retrieveUserPassword(String username){
		String password = "";
		StringBuilder strBuilder = new StringBuilder();
		boolean flag;
		
		flag = retrieveUserPasswordQuery(username, strBuilder);
		
		if(flag == false){
			password = ""; //return blank string if error in query process
			System.err.println("Error in geting password");
		}
		else{
			password = strBuilder.toString().trim();
		}
		
		return password;
	}
	
	
	public static boolean validateUser(String username, String password){
		boolean valid = false;
		
		//Check if the user is in the database
		if(checkIfAdminExists(username)){
			valid = validateUserQuery(username, password);
		}
		
		return valid;
	}
	
	
	/* ****************************************************************
	 * 						List Functions 			
	 * 
	 * ****************************************************************/
	/**
	 * listAddAdministrators
	 * <p> This function returns an List of strings holding all information
	 * for all administrators in the system
	 * @return
	 */
	public static List<String> toListAllAdministrators(){
		List<String> admins = new ArrayList<String>();
		String query = "SELECT * FROM Administrator";
		boolean success; //Denotes successful query
		
		// format of return string (UserID, Password, FirstName, LastName, Email)
		success = toListAdministratorsQuery(admins, query);
		
		if(!success){
			System.err.println(DBQUERY_ERROR);
		}
		
		return admins;
	}
	
	/**
	 * listAdministratorsByUsername
	 * <p>
	 * This function returns information for an administrator by username.
	 * Empty string returned if none exists.
	 * @param userName
	 * @return
	 */
	public static List<String> toListAdministratorsByUsername(){
		List<String> admins = new ArrayList<String>();
		String query = "SELECT `UserID` FROM Administrator ";
					    	
		boolean success; //Denotes successful query
		
		// format of return string (UserID, Password, FirstName, LastName, Email)
		success = toListAdministratorsQuery(admins, query);
		
		if(!success){
			System.err.println(DBQUERY_ERROR);
		}
		
		return admins;
	}
	
	/**
	 * listAdministratorsByFirstName
	 * <p>
	 * This function returns information for all admins with a first name
	 * provided by the user. Empty string returned if none exists.
	 * @param userName
	 * @return
	 */
	public static List<String> toListAdministratorsByFirstName(){
		List<String> admins = new ArrayList<String>();
		String query = "SELECT `FirstName` FROM Administrator "; 
						
		boolean success; //Denotes successful query
		
		// format of return string (UserID, Password, FirstName, LastName, Email)
		success = toListAdministratorsQuery(admins, query);
		
		if(!success){
			System.err.println(DBQUERY_ERROR);
		}
		
		return admins;
	}
	
	/** listAdministratorsByLastName
	 * <p>
	 * This function returns information for all admins with a last name
	 * provided by the user. Empty string returned if none exists.
	 * @param userName
	 * @return
	 */
	public static List<String> toListAdministratorsByLastName(){
		List<String> admins = new ArrayList<String>();
		String query = "SELECT `LastName` FROM Administrator"; 
						
		boolean success; //Denotes successful query
		
		success = toListAdministratorsQuery(admins, query);
		
		if(!success){
			System.err.println(DBQUERY_ERROR);
		}
		
		return admins;
	}
	
	/** listAdministratorsByEmail
	 * <p>
	 * This function returns information for all admins using an admins email on record
	 * as a match. Empty List is returned if user doesn't exists.
	 * @param userName
	 * @return
	 */
	public static List<String> toListAdministratorsByEmail(){
		List<String> admins = new ArrayList<String>();
		String query = "SELECT `Email` FROM Administrator ";
						
		boolean success; //Denotes successful query
		
		// format of return string (UserID, Password, FirstName, LastName, Email)
		success = toListAdministratorsQuery(admins, query);
		
		if(!success){
			System.err.println(DBQUERY_ERROR);
		}
		
		return admins;
	}
	
	
	/* ****************************************************************
	 * 						Query Functions 			
	 * 
	 * ****************************************************************/
	
	/**
	 * addAdministratorInsert
	 * <p>
	 * This function connects to the database and adds a new administrator
	 * 
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	private static boolean addAdministratorQuery(String username, String password, String firstName,
										String lastName, String email){
		
		boolean success = true; //Assume success, false if failure occurs
	
		String query;
	
		query = "INSERT INTO Administrator (UserID, Password, FirstName, LastName, Email)"
				+ " VALUES(" +
				 "'" + username + "',"+
				 "'" + password + "'," +
				 "'" + firstName + "'," +
				 "'" + lastName + "'," +
				 "'" + email + "');";			
			
		success = insertIntoTable(query);
		
		return success;
	}
	
	/**
	 * retrieveUserPasswordQuery
	 * <p>
	 * This function stores a password into a StringBuilder object after querying it using a 
	 * username provided from the user.
	 * @param username
	 * @param password
	 * @return
	 */
	private static boolean retrieveUserPasswordQuery(String username, StringBuilder password){
		boolean success = true;
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		ResultSetMetaData rsMd = null;
		String query;
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
			//3, Execute SQL query
			query = "SELECT Password FROM Administrator "
					+ "WHERE UserID = '" + username + "'";	
			
			myRs = myStmt.executeQuery(query); //Execute Query (retrieve user's password)	
			
			//Process the Result set
			rsMd = myRs.getMetaData();
			int numCol = rsMd.getColumnCount();
			while(myRs.next()){
				//Only one result should exists. If no-result then nothing to return
				if(numCol == 1){
					String colVal = myRs.getString(1);
					password.append(colVal);
				}
			}
		}
		catch(SQLException exc){
			exc.printStackTrace();
			System.out.println(DBQUERY_ERROR);
			success = false;
		}
		finally{
			//Disconnect from Database
			try{myRs.close();} catch(Exception exc){}
			try{myStmt.close();} catch(Exception exc){}
			try{myConn.close();} catch(Exception exc){}
		}
		
		
		return success;
	}

	/**
	 * This function runs a query to remove an administrator for the database provided with
	 * the username of the administrator to be removed
	 * @param username
	 * @return
	 */
	private static boolean removeAdministratorQuery(String username){
		boolean success = true; //Assume successful query
		
		String query = "DELETE FROM Administrator WHERE UserID = '" + username + "';";			
		
		success = updateTable(query);
		
		return success;
	}
	
	
	private static boolean changePasswordQuery(String username, String newPassword){
		boolean success = true;
		
		
		String query = "UPDATE Administrator SET Password = '" + newPassword +
				"' WHERE UserID = '" + username + "'";
							
		success = updateTable(query);
		
		
		return success;
	}

	private static boolean validateUserQuery(String username, String password){
		boolean valid = false; //Assume false
		
		String query = "SELECT COUNT(*) FROM `Administrator` WHERE `UserID`=\'" + username + "\' ";
		query = query + "AND `Password`=\'" + password + "\'";
		
		valid = checkIfExists(query);
		
		
		return valid;
	}

	/**
	 * listAdministratorsQuery
	 * <p>
	 * This function fetches a result set of administrators in the system and returns 
	 * it in a list of strings, each string pertaining to a single tuple in the table.
	 * A query is passed into the function, giving expanded functionality for constraints
	 * defined by the user.
	 * @param admins
	 * @param query
	 * @return
	 */
	private static boolean toListAdministratorsQuery(List<String> admins, String query){
		boolean success = true;
		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		ResultSetMetaData rsMd;
		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
			//2. Create a statement
			myStmt = myConn.createStatement();
			//3, Execute SQL query
			myRs = myStmt.executeQuery(query);
			rsMd = myRs.getMetaData();
			int numCol = rsMd.getColumnCount();
			//4. Process the result set
			while(myRs.next()){
				String temp = "";
				for (int i = 1; i <= numCol; i++) {
			        String columnValue = myRs.getString(i);
			        temp = temp + columnValue;
			    }
				
				//Add tuple to list
				temp.trim(); //trim trailing whitespace
				admins.add(temp);
			}
		}
		catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBQUERY_ERROR);
			success = false;
		}
		finally{
			//Disconnect from Database
			try{myRs.close();} catch(Exception exc){};
			try{myStmt.close();} catch(Exception exc){}
			try{myConn.close();} catch(Exception exc){}
		}
		
		return success;
	}

}






