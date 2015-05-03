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
 * @version May 2, 2015 5:37 PM
 */
class Database implements DatabaseInformation{	
	
	/* *******************************************************
	 * 				Create Functions
	 *********************************************************/
	/**
	 * This function calls a query function which creates a new table in the database.
	 * The SQL statement is passed into the function
	 * @param query - SQL statement used for the query
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean createTable(String query){		
		//create table
		return executeUpdateQuery(query); //true if table created
	}
	
	/**
	 * This function calls a query function which drops a table in the database.
	 * @param query - SQL statement used in query
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean dropTable(String query){
		//Drop table
		return executeUpdateQuery(query); //True if table dropped
	}
	
	/**
	 * This function calls a query function which updates a table in the database
	 * @param query - SQL statement used for query
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean updateTable(String query){
		//Execute update
		return executeUpdateQuery(query);//true if successfully executed
	}

	/**
	 * This function calls a query function which inserts a tuple into a table in the
	 * database.
	 * @param query - SQL statement is passed into the function
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean insertIntoTable(String query){
		//Insert tuple into table
		return executeUpdateQuery(query); //True if successful insert
	}
	
	/**
	 * This function calls a query function which deletes a table from the database
	 * @param query - SQL statement
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean deleteFromTable(String query){
		//Delete tuple from table
		return executeUpdateQuery(query);//True if successful delete from table
	}
	
	/**
	 * This function updates a tuple stored in a table in the database
	 * @param query - SQL statement
	 * @return <b>true</b> - successful query<br>
	 * 		   <b>false</b> - unsuccessful query/failure in query
	 */
	static protected boolean updateValuesInTable(String query){
		//Update tuple in table
		return executeUpdateQuery(query);//true if successful
	}
	
	/* *******************************************************
	 * 				Access Functions
	 *********************************************************/
	/**
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
	 * This function returns a list of strings storing the name of the columns of a table in the database
	 * @param tableName - name of table in database
	 * @param colNames - list of strings storing column names
	 * @return 
	 */
	public static ArrayList<String> toListTableAttributes(String tableName, ArrayList<String> colNames){
		colNames = new ArrayList<String>();
		ArrayList<ArrayList<String> > tempList = new ArrayList<ArrayList<String> >();
		
		//SQL statement used in query
		String query = "SELECT COLUMN_NAME "
					+ "FROM INFORMATION_SCHEMA.COLUMNS"
					+ " WHERE `TABLE_NAME`=\'" + tableName + "\'";
		
		//Get column names from database
		if(toListQuery(query, tempList)){
			for(int i = 0; i < tempList.size(); i++){
				//store names in list of strings
				colNames.add(tempList.get(i).get(0));
			}
			return colNames;
		}
		//error in query execution
		else{
			System.out.println(DBQUERY_ERROR); //print error message
		}
		return new ArrayList<String>(); // return empty list of strings, error in query
	}
	
	/**
	 * This function returns a boolean indicating if the database has successfully connected
	 * @return boolean - Succesful connection to database
	 */
	public static boolean checkIfDatabaseConnect(){
		boolean connected = false; //Connection status
		Connection myConn = null; //Connection object

		try{
			//Attempt to connect to the database
			myConn = DriverManager.getConnection(DBPATH, DBUSER, DBPASSWORD);
			connected = true; //connection successful
		}
		catch(Exception e){ connected = false; } //Couldn't connect to database
		try{myConn.close();} catch(Exception exc){}; //Close connection to database

		return connected;
	}
	
	/**
	 * A query is passed into this function which invokes a query function returning if a
	 * tuple exists in the table specified in the query.
	 * @param query - SQL statement
	 * @return 
	 */
	static protected boolean checkIfExists(String query){	
		return checkIfExistsQuery(query);
	}
	
	/**
	 * This function checks to see if a table exists in the database, given the table's name
	 * @param tableName - the name of the table in database
	 * @return <b>True</b> - if table exists <br><b>False</b> - Otherwise
	 */
	public static boolean checkIfTableExists(String tableName){
		boolean exists = false;
		
		String query = "SELECT COUNT(*) "
				+ "FROM information_schema.tables "
				+ "WHERE table_schema = '" + DBNAME + "' "
				+ "AND table_name = '" + tableName + "'";
		
		exists = checkIfExistsQuery(query); //check if table exists
		
		return exists; //true if table exists, false otherwise
	}
	
	/**
	 * This function calls a query function which returns the tuples of a table
	 * @param query - SQL statement
	 * @param list - result set of query execution
	 * @return <b>true</b> - if query executed successfully
	 */
	protected static boolean toListTuples(String query, ArrayList<ArrayList<String> > list){
		//Call to query function that stores the result set
		return toListQuery(query, list); //true if successful execution/false otherwise
	}
	
	/* *******************************************************
	 * 				Query Functions
	 *********************************************************/
	/**
	 * This function takes in a query that is attempted to be execution.
	 * This function is used for all queries that update the database.
	 * @param query - SQL statement
	 * @return <b>true</b> - if successful query execution
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
			myRs = myStmt.executeUpdate(query); //Execute Query
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
		
		return success; //true if successful query execution/false otherwise
	}

	/**
	 * This function takes in a query that is attempted to be execution.
	 * This function is used for all queries that return a resultSet
	 * @param query
	 * @return
	 */
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
			myRs = myStmt.executeQuery(query); //Execute Query and store result set
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
	 * This function calls a query that checks to see if a given tuple/table exists
	 * in the currently working database.
	 * @param query - SQL statement
	 * @return <b>true</b> - if tuple/table exists <br><b>false</b> - not exists/failure in query
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
	static private boolean toListQuery(String query, ArrayList<ArrayList<String>> list){
		boolean success = true; //Assume success	
		ArrayList<String> resultTuple = new ArrayList<String>();
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
				resultTuple = new ArrayList<String>();
				for (int i = 1; i <= numCol; i++) {
					temp = "";
			        String columnValue = myRs.getString(i);
			        
			        temp = columnValue;
			        //Add tuple to list
					temp.trim(); //trim trailing whitespace
					resultTuple.add(temp);
			    }
				list.add(resultTuple);
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



