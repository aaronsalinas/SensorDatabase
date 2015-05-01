
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
	
	static public boolean createInstrumentSensorReadingTable(String instrument, String serial, ArrayList<Pair<String, String> > attrDataTypeList){
		boolean success = false;
		
		if(!checkIfSensorReadingTableExists(instrument) && checkIfInstrumentSerialExists(instrument, serial)){
			
			success = createInstrumentSensorReadingTableQuery(instrument, serial, attrDataTypeList);
			
			if(!success) System.out.println("ERROR: Sensor Reading Table For " + instrument + " Not Created In Database!");
		}
		
		return success;
	}
	
	static public boolean createADCPCurrentTable(String instrument,
												ArrayList<Pair<String, String> > ADCPAttrDataType){
		boolean success = false;
		
		if(!checkIfADCPCurrentTableExists(instrument)){
			success = createADCPCurrentTableQuery(instrument, ADCPAttrDataType);
			if(!success){
				System.out.println("ERROR: ADCP Current Data Table For " + instrument + " Not Added To Database!");
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
	 * the database
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
	
	static public boolean addADCPCurrentData(String instrument, String serial, String timeStamp, 
											ArrayList<Pair<String, String> > ADCPAttrVal){
		boolean success = false;
		
		if(checkIfADCPCurrentTableExists(instrument) && !checkIfADCPCurrentDataExistsQuery(instrument, serial)){
			success = addADCPCurrentDataQuery(instrument, serial, timeStamp, ADCPAttrVal);
			
			if(!success){
				System.out.println("ADCP Current Data For " + instrument + " " + serial + " Not Added To Database!");
			}
		}
		else{
			System.out.println("ERROR: ADCP Current Data for " + instrument + " " + serial + "Already Exists!");
		}
		
		return success;
	}
	
	static public boolean addSensorReadings(String instrument, String serial, 
			ArrayList<ArrayList<Pair<String, String> > > sensorReadings, ArrayList<ArrayList<Pair<String,String> > > addedReadings){
		boolean success = false;
		
		if(checkIfSensorReadingTableExists(instrument)){
			
			success = addSensorReadingsQuery(instrument, serial, sensorReadings, addedReadings);
		}
		else{
			System.out.println("ERROR: Table For Instrument \"" + instrument + "\" Readings Not In Database!");
		}
		
		
		return success;
	}
	
	/**
	 * This function invokes a query call to update a ADCP given the corresponding instrument
	 * @param instrument
	 * @param serial
	 * @param timeStamp
	 * @param attrValList
	 * @return
	 */
	static public boolean updateADCPCurrentData(String instrument, String serial, String timeStamp,
												ArrayList<Pair<String, String> > attrValList){
		boolean success = false;
				
		//Check if ADCP Current Data tuple exists in table
		if(!checkIfADCPCurrentDataExists(instrument, serial)){
			//If not in table, add row for Instrument/Serial
			success = addADCPCurrentData(instrument, serial, timeStamp, attrValList);
			if(!success) System.out.println("ERROR: ADCP Current Data For " + instrument + " " 
																+ serial + "Not Added to Database");
		}
		else{

			success = updateADCPCurrentDataQuery(instrument, serial, attrValList);
			if(!success) System.out.println("ERROR: ADCP Current Data For " + instrument + " "
											+ serial + "Not Updated!");	
		}
		
		
		return success;
	}
	
	/* ************************************************************************ 
	 * 								Remove Functions
	 *************************************************************************/
	
	static public boolean removeADCPCurrentData(String instrument, String serial){
		boolean success = false;
		
		if(checkIfADCPCurrentDataExists(instrument, serial)){
			success = removeADCPCurrentDataQuery(instrument, serial);
		}
		
		return success;
	}
	
	/**
	 *  removeInstrument
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
	
	static public boolean removeSensorReadings(String instrument, String serial, ArrayList<ArrayList<Pair<String, String> > > readings){
		boolean success = false;
		
		if(checkIfSensorReadingTableExists(instrument)){
			success = removeSensorReadingsQuery(instrument, serial, readings);
		}
		else{
			System.out.println("ERROR: Table For Instrument \"" + instrument + "\" Readings Not In Database!");
		}
		
		return success;
	}
	
	static public boolean dropADCPCurrentTable(String instrument){
		String query = "DROP TABLE `ADCPCurrentData_" + instrument + "`";
		boolean success = false;
		
		//Check if instrument exists
		if(checkIfADCPCurrentTableExists(instrument)){
			dropTable(query);
		}
		
		return success;
	}
	
	static public boolean dropSensorReadingTable(String instrument){
		String query = "DROP TABLE `SensorReading_" + instrument + "`";
		boolean success = false;
		
		if(checkIfSensorReadingTableExists(instrument)){
			dropTable(query);
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
	static public ArrayList<String> toListAllInstruments(){
		ArrayList<ArrayList<String> > tempList = new ArrayList<ArrayList<String> > ();
		String query = "SELECT * FROM Instrument;";
		
		if(toListTuples(query, tempList) == false){
			System.err.println("Error in retriving information.");
		}
		//Concatenate the results into one list
		ArrayList<String> instrList = new ArrayList<String>();
		for(int i = 0; i < tempList.size(); i++){
			instrList.add(tempList.get(i).get(0));
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
	static public ArrayList<ArrayList<String>> toListAllInstrumentSerials(){
		ArrayList<ArrayList<String> > tempList = new ArrayList<ArrayList<String> >();
		String query = "SELECT * FROM InstrumentSerial";
		
		if(toListTuples(query, tempList) == false){
			System.err.println("Error in retrieving information");
		}

		return tempList;
	}
	
	static public ArrayList<String> toListSerialsByInstrument(String instrument){
		ArrayList<String> serials = new ArrayList<String>();
		ArrayList<ArrayList<String>> tempList = new ArrayList<ArrayList<String> >();
		String query = "SELECT `Serial Number` FROM `InstrumentSerial` "
				+ "WHERE `Instrument`=\'" + instrument + "\'";
		
		if(toListTuples(query, tempList) == false){
			System.err.println("Error in retrieving information");
		}else{
			for(int i = 0; i < tempList.size(); i++){
				serials.add(tempList.get(i).get(0));
			}
		}
		
		return serials;
	}
	
	static public ArrayList<ArrayList<String> > toListADCPCurrentDataInstrSerial(String instrument, String serial){
		
		ArrayList<ArrayList<String> >ADCPCurTuple = new ArrayList<ArrayList<String> >();
		
		String query = "SELECT * FROM `ADCPCurrentData_" + instrument + "` WHERE ";
		query = query + "`Instrument` = \'" + instrument +"\' AND `Serial Number` = \'" + serial + "\'";
		
		if(toListTuples(query, ADCPCurTuple) == false){
			System.err.println("Error in retrieving information");
		}
		
		return ADCPCurTuple;
	}
	
	static public ArrayList<ArrayList<String> > toListADCPCurrentDataInstr(String instrument){
		ArrayList<ArrayList<String> >ADCPCurTuple = new ArrayList<ArrayList<String> >();
		
		String query = "SELECT * FROM `ADCPCurrentData_" + instrument + "` WHERE ";
		query = query + "`Instrument` = \'" + instrument + "\'";
		
		if(toListTuples(query, ADCPCurTuple) == false){
			System.err.println("Error in retrieving information");
		}
		
		return ADCPCurTuple;
	}

	
	static public ArrayList<ArrayList<String> > toListSensorReadingByInstrument(String instrument){
		ArrayList<ArrayList<String> > readings = new ArrayList<ArrayList<String> >();
		String query = "SELECT * FROM `SensorReading_" + instrument + "`"
						+ " WHERE `Instrument`=\'" + instrument + "\'";
		
		if(toListTuples(query, readings) == false){
			System.err.println("Error in retrieving information");
		}
		return readings;
	}
	
	static public ArrayList<ArrayList<String> > toListSensorReadingByInstrumentSerial(String instrument, String serial){
		ArrayList<ArrayList<String> > readings = new ArrayList<ArrayList<String> >();
		
		String query = "SELECT * FROM `SensorReading_" + instrument + "`"
				+ " WHERE `Instrument`=\'" + instrument + "\' AND" 
				+ " `Serial Number`=\'" + serial + "\'";

		if(toListTuples(query, readings) == false){
			System.err.println("Error in retrieving information");
		}
		
		return readings;
	}
	
	static public ArrayList<ArrayList<String> > toListSensorReadingByInstrumentSerialTime(String instrument, String serial, 
													String startTime, String endTime){
		ArrayList<ArrayList<String> > readings = new ArrayList<ArrayList<String> >();
		
		String query = "SELECT * FROM `SensorReading_" + instrument + "`"
				+ " WHERE `Instrument`=\'" + instrument + "\' AND" 
				+ " `Serial Number`=\'" + serial + "\' AND `ReadTime` >= \'" + startTime + "\'"
				+ " AND `ReadTime`<= \'" + endTime + "\'";

		if(toListTuples(query, readings) == false){
			System.err.println("Error in retrieving information");
		}
		
		return readings;
	}
	
	static public ArrayList<String> toListADCPTableAttributes(String instrument){
		ArrayList<String> colNames = new ArrayList<String>();
		String tableName = "ADCPCurrentData_" + instrument;	
		
		return toListTableAttributes(tableName, colNames);
	}
	
	static public ArrayList<String> toListSensorReadingAttributes(String instrument){
		ArrayList<String> colNames = new ArrayList<String>();
		String tableName = "SensorReading_" + instrument;	
		
		return toListTableAttributes(tableName, colNames);
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
	
	static public boolean checkIfADCPCurrentTableExists(String instrument){
		boolean exists = false;
		
		exists = checkIfADCPCurrentTableExistsQuery(instrument); 
		
		return exists;
	}
	
	static public boolean checkIfADCPCurrentDataExists(String instrument, String serial){
		boolean exists = false;
		
		exists = checkIfADCPCurrentDataExistsQuery(instrument, serial);
		
		return exists;
		
	}
	
	static public boolean checkIfADCPCurrentDataNewer(String instrument, String serial, String timeStamp){
		boolean newer = false;
		
		newer = checkIfADCPCurrentDataNewerQuery(instrument, serial, timeStamp);
				
		return newer;
	}
	
	static public boolean checkIfSensorReadingTableExists(String instrument){
		boolean exists = false;
		
		exists = checkIfSensorReadingTableExistsQuery(instrument);
		
		return exists;
	}
	
	
	static public boolean checkIfSensorReadingExists(String instrument, String serial, String timeStamp){
		boolean exists = false;
		
		exists = checkIfSensorReadingExistsQuery(instrument, serial, timeStamp);
		
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
		
		String query;
		
		query = "INSERT INTO Instrument (Instrument)"
				+ " VALUES('" + instrument + "')";			
		
		success = insertIntoTable(query);
		
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

		String query;

		query = "INSERT INTO `InstrumentSerial` (`Instrument`, `Serial Number`)"
				+ " VALUES('" + instrument + "', '" + serial + "')";			
		
		success = insertIntoTable(query);
		
		
		return success;
	}
	
	static private boolean addADCPCurrentDataQuery(String instrument, String serial,
													String timeStamp, ArrayList<Pair<String, String> > attrValList){
		boolean success = true; //assume success;
		
		String query;
		
		String tableName = "ADCPCurrentData_" + instrument;
		String attributes = "";
		String values = "";
	
		query = "INSERT INTO `" + tableName + "` (";
		
		for(int i = 0; i < attrValList.size(); i++){
			if(i > 0){
				attributes = attributes + ",";
				values = values + ",";
			}
			attributes = attributes + "`" + attrValList.get(i).first +"`";
			values = values + "'" + attrValList.get(i).second +"'";
		}
		query = query + attributes + ") VALUES(" + values;
		query = query + ")";
				
		success = insertIntoTable(query);
		
		
		return success;
	}

	static private boolean addSensorReadingsQuery(String instrument, String serial, 
		ArrayList<ArrayList<Pair<String, String> > > sensorReadings, ArrayList<ArrayList<Pair<String,String> > > addedReadings){
		boolean success = true; //Assume all good reads, If error occurs then the flag will be set to false;
		String tableName = "SensorReading_" + instrument;
		String query = "";
		
		for(int i = 0; i < sensorReadings.size(); i++){
			ArrayList<Pair<String, String> > rowRead = sensorReadings.get(i);
			String timeStamp = rowRead.get(0).second;

			if(!checkIfSensorReadingExists(instrument, serial, timeStamp)){
				query = "";
				query = query + "INSERT INTO `" + tableName + "` VALUES(\'" + instrument + "\'"
						+ ",\'" + serial + "\',";
				
				//Add Attributes
				for(int j = 0; j < rowRead.size(); j++){
					if(j > 0) query = query + ",";
					query = query + "'" + rowRead.get(j).second + "'";
				}
				
				query = query + ")";
				if(insertIntoTable(query)){
					addedReadings.add(rowRead); //Push newly added read into reference array to rows successfully added
				}else{
					success = false;
				}
			}
		}
		
		return success;
	}

	private static boolean createADCPCurrentTableQuery(String instrument,
			ArrayList<Pair<String, String>> ADCPAttrDataType) {
		
		boolean success = true; //assume success;
		
			String query = "CREATE TABLE IF NOT EXISTS `ADCPCurrentData_" + instrument + "` (";		
			
			//Add Table Columns
			for(int i = 0; i < ADCPAttrDataType.size(); i++){
				query = query + "`" + ADCPAttrDataType.get(i).first + "` ";
				query = query + ADCPAttrDataType.get(i).second + " NOT NULL , ";
			}
			//Add Primary Key
			query = query + "PRIMARY KEY (`Instrument`,`Serial Number`,`ReadTime`), ";
			
			//Add Constraints
			query = query + "CONSTRAINT `ADCPCurrentData_" + instrument +"(Instrument, Serial Number)` ";
			query = query + "FOREIGN KEY (`Instrument` , `Serial Number`) ";
			query = query + "REFERENCES `InstrumentSerial` (`Instrument` , `Serial Number`)";
			
			query = query + ")";
			
			success = createTable(query);		
	
		return success;
	}
	
	private static boolean createInstrumentSensorReadingTableQuery(String instrument, String serial, List<Pair<String, String> > attrDataTypeList){
		boolean success = false;
		
		String query = "CREATE TABLE IF NOT EXISTS `SensorReading_" + instrument 
						+ "`(`Instrument` " + DatabaseUnits.STRING_UNIT + " NOT NULL,`Serial Number`" 
						+ DatabaseUnits.STRING_UNIT + " NOT NULL,";
		//Add attributes/data types
		for(int i = 0; i < attrDataTypeList.size(); i++){
			query = query + "`" + attrDataTypeList.get(i).first + "` ";
			query = query + attrDataTypeList.get(i).second + " NOT NULL,";
		}
		
		//Add Primary Key
		query = query + "PRIMARY KEY (`Instrument`,`Serial Number`,`ReadTime`),"; 
		
		//Add Constraints
		query = query + "CONSTRAINT `SensorReading_" + instrument + "(Instrument, Serial Number)` ";
		query = query + "FOREIGN KEY (`Instrument` , `Serial Number`) ";
		query = query + "REFERENCES `InstrumentSerial` (`Instrument` , `Serial Number`)";
		query = query + ")";
		
		success = createTable(query);
		
		return success;
	}
	
	static private boolean updateADCPCurrentDataQuery(String instrument, String serial, ArrayList<Pair<String, String> > attrVal){		
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
			String tableName = "ADCPCurrentData_" + instrument;
			String attributes = "";
			String values = "";
		
			query = "UPDATE `" + tableName + "` SET ";
			
			for(int i = 0; i < attrVal.size(); i++){
				if(i > 0){
					query = query + ",";
				}
				query = query + "`" + attrVal.get(i).first + "`=\'" + attrVal.get(i).second + "\'";
			}
			
			query = query + " WHERE `Instrument`=\'" + instrument + "\' AND `Serial Number`=\'" + serial + "\'";
			myRs = myStmt.executeUpdate(query); //Execute Query (add new ADCP current reading for instrument)			
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
	
	static private boolean removeADCPCurrentDataQuery(String instrument, String serial){
		boolean success = false;
		
		String tableName = "ADCPCurrentData_" + instrument;
		String query = "DELETE FROM `" + tableName + "` WHERE `Instrument`=\'" + instrument
					+ "\' AND `Serial Number`=\'" + serial + "\'";
		
		deleteFromTable(query); //Delete ADCPCurrent reading from table for instrument
		
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
	
	static private boolean removeSensorReadingsQuery(String instrument, String serial, ArrayList<ArrayList<Pair<String, String> > > readings){
		boolean success = false;
		String tableName = "SensorReading_" + instrument;
			
		for(int i = 0; i < readings.size(); i++){	
			ArrayList<Pair<String, String> > tempRead = readings.get(i);
			String query = "DELETE FROM `" + tableName + "` WHERE `Instrument`=\'" + instrument + "\'"
							+ " AND `Serial Number`=\'" + serial + "\'";
			
			for(int j = 0; j < tempRead.size(); j++){
				query = query + " AND `" + tempRead.get(j).first 
						+ "`=\'" + tempRead.get(j).second + "\'";
			}
			
			deleteFromTable(query);	
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
				  + "FROM `Instrument` "
				  + "WHERE `Instrument` = \'" + instrument + "\';";
				
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
				  + "FROM `InstrumentSerial` "
				  + "WHERE `Instrument` = \'" + instrument + "\' AND "
				  + "`Serial Number` = '" + serial + "';";
			
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

	static private boolean checkIfADCPCurrentTableExistsQuery(String instrument){
		boolean exists = false;
		String tableName = "ADCPCurrentData_" + instrument;			
		
		exists = checkIfTableExists(tableName);
				
		return exists;
	}
	
	static private boolean checkIfADCPCurrentDataExistsQuery(String instrument, String serial){
		boolean ADCPCurExists = false;
		
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
			String tableName = "ADCPCurrentData_" + instrument;
			
			query = "SELECT COUNT(*) "
				  + "FROM `" + tableName + "` "
				  + "WHERE `Instrument` = \'" + instrument + "\' AND "
				  + "`Serial Number` = '" + serial + "';";
			
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//Check if ADCP tuple exists for Instrument/Serial exists count > 0
				if(count > 0){
					ADCPCurExists = true;
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

		return ADCPCurExists;
	}
	
	static private boolean checkIfADCPCurrentDataNewerQuery(String instrument, String serial, String timeStamp){
		boolean curNewer = false;
		
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
			String tableName = "ADCPCurrentData_" + instrument;
			
			query = "SELECT COUNT(*) "
				  + "FROM `" + tableName + "` "
				  + "WHERE `Instrument` = \'" + instrument + "\' AND "
				  + "`Serial Number` = '" + serial + "' AND "
				  + "`ReadTime` >= \'" + timeStamp +"\'";
						
			myRs = myStmt.executeQuery(query);
			//4. Process the result set
			while(myRs.next()){
				int count = Integer.parseInt(myRs.getString("COUNT(*)"));
				//If count > 0 then read stored in DB is newer than read trying to be stored
				if(count > 0){
					curNewer = false;
				}else{
					curNewer = true;
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

		return curNewer;
	}
	
	static private boolean checkIfSensorReadingTableExistsQuery(String instrument){
		boolean exists = false;
		String tableName = "SensorReading_" + instrument;
		
		exists = checkIfTableExists(tableName);
		
		return exists;
	}

	static private boolean checkIfSensorReadingExistsQuery(String instrument, String serial, String timeStamp){
		boolean readingExists = false;

		String tableName = "SensorReading_" + instrument;
		
		String query = "SELECT COUNT(*) "
			  + "FROM `" + tableName + "` "
			  + "WHERE `Instrument` = \'" + instrument + "\' AND "
			  + "`Serial Number` = '" + serial + "' AND `ReadTime` = \'" + timeStamp + "\'";
		
		readingExists = checkIfExists(query);

		return readingExists;
	}
	

}



