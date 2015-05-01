import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	static protected boolean createTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
		return success;
	}
	
	static protected boolean dropTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
		return success;
	}
	
	static protected boolean updateTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
		return success;
	}

	static protected boolean insertIntoTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
		return success;
	}
	
	static protected boolean deleteFromTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
		return success;
	}
	
	static protected boolean updateValuesInTable(String query){
		boolean success = false;
		
		if(executeUpdateQuery(query)){
			success = true;
		}
		
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
	
	/**
	 * This function returns a boolean indicating if the database has successfully connected
	 * @return boolean
	 */
	public static boolean checkIfDatabaseConnect(){
		boolean connected = false;
		Connection myConn = null;

		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
			connected = true;
		}
		catch(Exception e){}
		try{myConn.close();} catch(Exception exc){};

		
		
		return connected;
	}
	
	/**
	 * A query is passed into this function which invokes a query function returning if a
	 * tuple exists in the table specified in the query.
	 * @param query
	 * @return
	 */
	static protected boolean checkIfExists(String query){
		boolean success = false;
		
		if(checkIfExistsQuery(query)){
			success = true;
		}
		
		return success;
	}
	
	/**
	 * This function takes in a query and invokes a query function which checks if a certain 
	 * table exists
	 * @param query
	 * @return
	 */
	public static boolean checkIfTableExists(String tableName){
		boolean exists = false;
		
		String query = "SELECT COUNT(*) "
				+ "FROM information_schema.tables "
				+ "WHERE table_schema = '" + DBNAME + "' "
				+ "AND table_name = '" + tableName + "'";
		
		exists = checkIfExistsQuery(query);
		
		return exists;
	}
	
	protected static boolean toListTuples(String query, ArrayList<String> list){
		boolean success = false;
		
		success = toListQuery(query, list);
		
		return success;
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
	static private boolean executeUpdateQuery(String query){
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
			myRs = myStmt.executeUpdate(query); //Execute Query (Create New Table)
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
	
	static private boolean executeQueryQuery(String query){
		boolean success = true; //assume success
		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs;
		
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
			myRs = myStmt.executeQuery(query); //Execute Query (Create New Table)
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
	 * @throws SQLException 
	 */
	static private boolean checkIfExistsQuery(String query){
		boolean tableExists = false;
		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
		}
		catch(Exception e){
			System.out.println(DBCONN_ERROR);
		}
		try{
			//2. Create a statement
			myStmt = myConn.createStatement();
		}
		catch(Exception e){
			System.out.println(DBSTATEMENT_ERROR);
		}
		//3, Execute SQL query
		
		try{
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
		catch(Exception e){
			System.out.println(DBQUERY_ERROR);
		}

		//Disconnect from Database
		try{myRs.close();} catch(Exception exc){};
		try{myStmt.close();} catch(Exception exc){}
		try{myConn.close();} catch(Exception exc){}
	

		return tableExists;
	}
	
	/**
	 * toListQuery
	 * <p>
	 * This function stores the results of a search query into a list of strings,
	 * each string pertaining to one tuple in the result. Empty set if no data exists
	 * for search.
	 * @author Aaron D. Salinas
	 * @param list
	 * @param query
	 * @return
	 */
	static private boolean toListQuery(String query, ArrayList<String> list){
		boolean success = true; //Assume success		
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		ResultSetMetaData rsMd;
		try{
			//1. Get a connection to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
		}catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBCONN_ERROR);
		}
		try{
			//2. Create a statement
			myStmt = myConn.createStatement();
		}catch(Exception exc){
			exc.printStackTrace();
			System.err.println(DBSTATEMENT_ERROR);
		}
		try{
			//3, Execute SQL query
			myRs = myStmt.executeQuery(query);
			
			rsMd = myRs.getMetaData();
			int numCol = rsMd.getColumnCount();
			//4. Process the result set
			while(myRs.next()){
				String temp = "";
				for (int i = 1; i <= numCol; i++) {
			        String columnValue = myRs.getString(i);
			        
			        temp = columnValue;
			        //Add tuple to list
					temp.trim(); //trim trailing whitespace
					list.add(temp);
			    }
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



