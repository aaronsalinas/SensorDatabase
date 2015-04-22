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
	 * 								Create Functions
	 *************************************************************************/
	/**
	 * This function Creates a new instrument tables that stores sensor readings for the type of instrument
	 * it is. The name of the table is identical to the name of the instrument. A List of Pairs is passed into
	 * the function storing the attribute name and data type as strings, respectively to each pair. The key of the
	 * a pair should be the name of the attribute, the value should be the data type 
	 *<p>
	 * NOTE: the data type of each attribute should be the proper constant variable as found in the DatabaseUnits
	 * 		 class. 
	 * @param instrument - the name of the table 
	 * @param columnList
	 * @return
	 */
	static public boolean createInstrumentReadingsTable(String instrument, List<Pair<String, String>> columnList){
		boolean success = false;
		
		//Check that datatypes are in correct format
		for(int i = 0; i < columnList.size(); i++){
			if(!DatabaseUnits.validDatabaseDatatype(columnList.get(i).getValue())){
				System.err.println("Incorrect Database Datatype");
				return false; //returns false if a datatype is incorrect
			}
		}
				
		/*	**Create Statement Template**
		 * 
		 * 	CREATE TABLE table_name
		 * 	(
		 * 		column_name1 data_type(size),
		 * 		column_name2 data_type(size),
		 *		column_name3 data_type(size)
		 * 	)
		 */
		String query = "CREATE TABLE " + instrument + "(";
		for(int i = 0; i < columnList.size(); i++){
			query = query + "`" + columnList.get(i).getKey() + "` "; // name of attribute for current column
			query = query + columnList.get(i).getValue();
			if(i < columnList.size() - 1){
				query = query + ", "; //trailing comma after each column attribute value
			}
			
		}
		query = query + ")";
		
		
		if(!checkIfTableExists(instrument)){
			if(createTable(query)){
				success = true; //Table successfully created
			}
		}
		
		
		return success;
	}
	
	/**
	 * This function Creates a new instrument tables that stores sensor readings for the type of instrument
	 * it is. The name of the table is identical to the name of the instrument. A List of Pairs is passed into
	 * the function storing the attribute name and data type as strings, respectively to each pair. The key of the
	 * a pair should be the name of the attribute, the value should be the data type. A string storing the attribute
	 * which is the primary key of the table is passed into the function
	 * 
	 * <p>
	 * NOTE: the data type of each attribute should be the proper constant variable as found in the DatabaseUnits
	 * 		 class. 
	 * @param instrument
	 * @param columnList
	 * @param primaryKey
	 * @return
	 */
	static public boolean createInstrumentReadingsTable(String instrument, List<Pair<String, String>> columnList, String primaryKey){
		boolean success = false;
		
		//Check that datatypes are in correct format
		for(int i = 0; i < columnList.size(); i++){
			if(!DatabaseUnits.validDatabaseDatatype(columnList.get(i).getValue())){
				System.err.println("Incorrect Database Datatype");
				return false; //returns false if a datatype is incorrect
			}
		}
		
		/*	**Create Statement Template**
		 * 
		 * 	CREATE TABLE table_name
		 * 	(
		 * 		column_name1 data_type(size),
		 * 		column_name2 data_type(size),
		 *		column_name3 data_type(size)
		 * 	)
		 */
		String query = "CREATE TABLE " + instrument + "(";
		for(int i = 0; i < columnList.size(); i++){
			query = query + "`" + columnList.get(i).getKey() + "` "; // name of attribute for current column
			query = query + columnList.get(i).getValue();
			query = query + ", "; //trailing comma after each column attribute value
		}
		query = query + "PRIMARY KEY(`" + primaryKey + "`)";
		query = query + ")";
		
		
		if(!checkIfTableExists(instrument)){
			if(createTable(query)){
				success = true; //Table successfully created
			}
		}
		
		
		return success;
	}
	
	/**
	 * This function Creates a new instrument tables that stores sensor readings for the type of instrument
	 * it is. The name of the table is identical to the name of the instrument. A List of Pairs is passed into
	 * the function storing the attribute name and data type as strings, respectively to each pair. The key of the
	 * a pair should be the name of the attribute, the value should be the data type. A string storing the attribute
	 * which is the primary key of the table is passed into the function
	 * 
	 * <p>
	 * NOTE: the data type of each attribute should be the proper constant variable as found in the DatabaseUnits
	 * 		 class. 
	 * @param instrument
	 * @param columnList
	 * @param primaryKeyList
	 * @return
	 */
	static public boolean createInstrumentReadingsTable(String instrument, List<Pair<String, String>> columnList, List<String> primaryKeyList){
		boolean success = false;
		
		//Check that datatypes are in correct format
		for(int i = 0; i < columnList.size(); i++){
			if(!DatabaseUnits.validDatabaseDatatype(columnList.get(i).getValue())){
				System.err.println("Incorrect Database Datatype");
				return false; //returns false if a datatype is incorrect
			}
		}
		
		/*	**Create Statement Template**
		 * 
		 * 	CREATE TABLE table_name
		 * 	(
		 * 		column_name1 data_type(size),
		 * 		column_name2 data_type(size),
		 *		column_name3 data_type(size)
		 * 	)
		 */
		String query = "CREATE TABLE " + instrument + "(";
		//Assign columns of table
		for(int i = 0; i < columnList.size(); i++){
			query = query + "`" + columnList.get(i).getKey() + "` "; // name of attribute for current column
			query = query + columnList.get(i).getValue();
			query = query + ", "; //trailing comma after each column attribute value
		}
		//Assign Primary Key
		query = query + "PRIMARY KEY(";
		for(int i = 0; i < primaryKeyList.size(); i++){
			query = query + "`" + primaryKeyList.get(i) + "`";
			if(i < columnList.size() - 1){
				query = query + ", ";
			}
		}
		query = query + "))";
		
		if(!checkIfTableExists(instrument)){
			if(createTable(query)){
				success = true; //Table successfully created
			}
		}
		
		return success;
	}
	
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
			if(addInstrumentQuery(instrument)){
				success = true;
			}
		}
		else{
			System.err.println("Error: Instrument Already Exists In Database!");
		}
		
		return success;
	}
	
	/**
	 * addInstrumentSerial
	 * <p>
	 * This function takes in a instrument and serial and adds the model
	 * into the database. If the instrument does not exists in the database then
	 * the function returns false w/out attempting to add the serial number into
	 * the database.
	 * @param instrument
	 * @param serial
	 */
	static public boolean addInstrumentSerial(String instrument, String serial){
		boolean success = false; //Assume Failue
		
		if(!checkIfInstrumentExists(instrument)){
			System.err.println("ERROR: Instrument Not Stored in Database");
			return false;
		}
		else{
			//Add into database if it doesn't already exist
			if(checkIfInstrumentSerialExists(instrument, serial) == false)
				if(addInstrumentSerialQuery(instrument, serial)){//call query to add instr/serial
					success = true;
				}
		}
		if(!success){
			System.out.println("Instrument/Serial not added to database!");
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
			if(removeInstrumentQuery(instrument)){
				success = true;
			}
		}
		
		return success;
	}
	
	/**
	 * This function removes a tuple from the InstrumentSerial table in the database.
	 * The instrument and serial values are passed into the function. Returns true 
	 * if the tuple is successfully removed, otherwise returns false
	 * @param instrument
	 * @param serial
	 * @return
	 */
	static public boolean removeInstrumentSerial(String instrument, String serial){
		boolean success = false; //Assume failure
		
		if(checkIfInstrumentSerialExists(instrument, serial)){
			if(removeInstrumentSerialQuery(instrument, serial)){
				success = true;
			}
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
	static public List<String> toListAllInstruments(){
		List<String> instrList = new ArrayList<String>();
		String query = "SELECT * FROM Instrument;";
		
		if(toListQuery(instrList, query) == false){
			System.err.println("Error in retriving information.");
		}
		
		return instrList;
	}
	
	/**
	 * toListAllInstrumentSerials
	 * <p>
	 * This function calls a query function which returns all tuples in the
	 * InstrumentSerial table in the database. Result set returned as a 
	 * list of strings, each string pertaining to one tuple.
	 * @author Aaron D. Salinas
	 * @return
	 */
	static public List<String> toListAllInstrumentSerials(){
		List<String> instrSerialList = new ArrayList<String>();
		String query = "SELECT * FROM InstrumentSerial";
		
		if(toListQuery(instrSerialList, query) == false){
			System.err.println("Error in retrieving information");
		}
		
		
		return instrSerialList;
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
	
	/**
	 * checkIfInstrumentSerialExists
	 * <p>
	 * This function returns true if a instrument/serial combination exists in the 
	 * InstrumentSerial table of the database.
	 * @author Aaron D. Salinas
	 * @param instrument
	 * @param serial
	 * @return
	 */
	static public boolean checkIfInstrumentSerialExists(String instrument, String serial){
		boolean exists = false;
		
		exists = checkIfInstrumentSerialExistsQuery(instrument, serial);
		
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
	 * addInstrumentSerialQuery
	 * <p>
	 * This function calls a query too add a Instrument/Serial tuple into the 
	 * InstrumentSerial table. Returns true if query is successful, otherwise false.
	 * @param instrument
	 * @param serial
	 * @return
	 */
	static private boolean addInstrumentSerialQuery(String instrument, String serial){
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
			query = "INSERT INTO InstrumentSerial (Instrument, Serial)"
					+ " VALUES('" + instrument + "', '" + serial + "')";			
			
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
	 * This function calls a query which removes a tuple from the InstrumentSerial
	 * table in the database, provided the instrument and serial values, respectively
	 * @param instrument
	 * @param serial
	 * @return
	 */
	static private boolean removeInstrumentSerialQuery(String instrument, String serial){
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
			query = "DELETE FROM InstrumentSerial WHERE Instrument = '" + instrument + "' "
					+ "AND Serial = '" + serial + "';";	
			
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
	static private boolean toListQuery(List<String> list, String query){
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
			        temp = temp + "'" + columnValue + "'" + " ";
			    }
				
				//Add tuple to list
				temp.trim(); //trim trailing whitespace
				list.add(temp);
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
	
	/**
	 * checkIfInstrumentSerialExistsQuery
	 * <p>
	 * This function calls a query checking if a instrument/serial combination provided
	 * exists in the InstrumentSerial table in the database. Returns true if tuple exists,
	 * false otherwise.
	 * @param instrument
	 * @param serial
	 * @return
	 */
	static private boolean checkIfInstrumentSerialExistsQuery(String instrument, String serial){
		
		boolean instrSerialExists = false;
		
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
				  + "FROM InstrumentSerial "
				  + "WHERE Instrument = \'" + instrument + "\' AND "
				  + "Serial = '" + serial + "';";
			
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//Check if admin exists count > 0
				if(count > 0){
					instrSerialExists = true;
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

		return instrSerialExists;
	}

	

}



