import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SensorDatabaseAccess extends Database{
	
	/* ************************************************************************ 
	 * 								Add Functions
	 *************************************************************************/
	/**
	 * addNewInstrument
	 * <p>
	 * This function calls a query function which adds a new instrument into the
	 * database.
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static public boolean addInstrument(String instrument){
		boolean success = false; //Assume failure
		
		//Check if instrument already exists in the database
		if(checkIfInstrumentExists(instrument) == false){
			//Add instrument into the database
			success = addInstrumentQuery(instrument);
		}
		else{
			System.out.println("Error: Instrument Already Exists In Database!");
		}
		
		return success;
	}
	
	/* ************************************************************************ 
	 * 								Remove Functions
	 *************************************************************************/
	/**
	 * removeInstrument
	 * <p>
	 * This function calls a query function which removes a instrument whose name 
	 * is passed into the function
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static public boolean removeInstrument(String instrument){
		boolean success = false; //assume failure
		
		if(checkIfInstrumentExists(instrument) == true){
			success = removeInstrumentQuery(instrument);
		}else{
			System.out.println("Instrument Does Not Exists In Database");
		}
		
		return success;
	}
	
	
	/* ************************************************************************ 
	 * 								Access Functions
	 *************************************************************************/
	/**
	 * listSensorInstruments
	 * <p>
	 * This function returns a list of strings storing all the instrument models 
	 * of the sensors stored in the database. This function calls a query function
	 * to retrieve information from the database
	 * @author Aaron D. Salinas
	 */
	static public List<String> toListAllSensorInstruments(){
		List<String> instrList = new ArrayList<String>();
		String query = "SELECT * FROM Instrument;";
		
		if(toListSensorInstrumentsQuery(instrList, query) == false){
			System.err.println("Error in retriving information.");
		}
		
		return instrList;
	}
	
	/**
	 * checkIfInstrumentExists
	 * <p>
	 * This function calls a query function which returns true if an instrument
	 * type exists in the database, false otherwise
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static public boolean checkIfInstrumentExists(String instrument){
		boolean exists = false;
		
		exists = checkIfInstrumentExistsQuery(instrument);
		
		return exists;
	}
	
	
	/* ************************************************************************ 
	 * 								Query Functions
	 *************************************************************************/
	
	/**
	 * addInstrumentQuery
	 * <p>
	 * This function calls a query which adds a new instrument type into the database.
	 * The database returns true if the add is successful, false otherwise
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static private boolean addInstrumentQuery(String instrument){
		boolean success = true; //assume success;
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		int myRs;
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
			query = "INSERT INTO Instrument (Instrument)"
					+ " VALUES('" + instrument + "')";			
			
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
	 * removeInstrumentQuery
	 * <p>
	 * This function calls a query which removes a instrument passed into
	 * the function.
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static private boolean removeInstrumentQuery(String instrument){
		boolean success = true; //Assume success
		//Connect to database
		Connection myConn = null;
		Statement myStmt = null;
		int myRs;
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
			query = "DELETE FROM Instrument WHERE Instrument = '" + instrument + "';";			
			myRs = myStmt.executeUpdate(query); //Execute Query (delete instrument from database)	
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
	 * toListSensorInstrumentQuery
	 * <p>
	 * This function calls a query, which is passed into the function, returns a 
	 * result set of tuples listing the instruments in the database. Constraints
	 * may be defined in query passed into function.
	 * @author Aaron D. Salinas
	 * @param instr
	 * @param query
	 * @return
	 */
	static private boolean toListSensorInstrumentsQuery(List<String> instr, String query){
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
			        //System.out.print(columnValue);
			        temp = temp + columnValue + " ";
			    }
				
				//Add tuple to list
				temp.trim(); //trim trailing whitespace
				instr.add(temp);
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
	
	/**
	 * checkIfInstrumentExistsQuery
	 * <p>
	 * This function calls a query checking if a given instrument, provided the name,
	 * exists in the database. If it does the function returns true, false if otherwise.
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @return
	 */
	static private boolean checkIfInstrumentExistsQuery(String instrument){
		boolean instrExists = false;
		
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
				  + "FROM Instrument "
				  + "WHERE Instrument = \'" + instrument + "\';";
				
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//Check if admin exists count > 0
				if(count > 0){
					instrExists = true;
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
		
		return instrExists;
	}
}
