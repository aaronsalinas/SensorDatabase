
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Filename: SensorDatabaseAccess.java
 * Author: Aaron D. Salinas (Aaron_Salinas@baylor.edu)
 * Description: This class holds methods that are used to interact with all 
 * information in the database relevant to ADCP Sensor's and their correspoding
 * attributes and readings. 
 * Created: 4/28/2015
 * Modified:5/4/2015
******************************************************************************/
public class SensorDatabaseAccess extends Database{
	

	/* ************************************************************************ 
	 * 								Create Functions
	 *************************************************************************/
	
	/**
	 * This function creates a table that stores readings for a given instrument if one does not already exist
	 * 
	 * @param instrument - name of the instrument
	 * @param serial
	 * @param attrDataTypeList
	 * @return <b>true</b> - successfully created table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean createInstrumentSensorReadingTable(String instrument, ArrayList<Pair<String, String> > attrDataTypeList){
		boolean success = false;
		
		if(!checkIfSensorReadingTableExists(instrument)){
			
			success = createInstrumentSensorReadingTableQuery(instrument, attrDataTypeList);
			
			if(!success) System.out.println("ERROR: Sensor Reading Table For " + instrument + " Not Created In Database!");
		}
		
		return success;
	}
	
	/**
	 * This function calls a query function which creates a table in the database to store
	 * current information for a given instrument type
	 * @param instrument - name of the instrument
	 * @param ADCPAttrDataType - the columns of the table and their corresponding type
	 * @return <b>true</b> - successful table creation<br>
	 * 		   <b>false</b> - unsuccessful table creation/failure in query
	 */
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
	 * @param instrument - name of the instrument to be added to table
	 * @return <b>true</b> - successfully added into database<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean addInstrument(String instrument){
		boolean success = false; //Assume failure
		
		//Check if instrument already exists in the database
		if(!checkIfInstrumentExists(instrument)){
			//Add instrument into the database
			success = addInstrumentQuery(instrument);
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
	 * @return <b>true</b> - successful added instrument/serial into database<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean addInstrumentSerial(String instrument, String serial){
		boolean success = false; //Assume Failure
		
		if(!checkIfInstrumentExists(instrument)){
			System.err.println("ERROR: Instrument Not Stored in Database");
			return false;
		}
		else{
			//Add into database if it doesn't already exist
			if(checkIfInstrumentSerialExists(instrument, serial) == false)
				if(addInstrumentSerialQuery(instrument, serial)){//call query to add instrument/serial
					success = true;
				}
		}
		if(!success){
			System.out.println("Instrument/Serial not added to database!");
		}
		
		return success;
	}
	
	/**
	 * This function adds a tuple in the corresponding ADCPCurrent table if one 
	 * does not already exist in the table.
	 * @param instrument - name of the instrument
	 * @param serial - serial number of instrument
	 * @param timeStamp - time stamp of read
	 * @param ADCPAttrVal - corresponding list of values for the read
	 * @return <b>true</b> - successfully inserted tuple into table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
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
	
	/**
	 * This function adds a reading into the corresponding table storing sensor readings for a
	 * instrument
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param sensorReadings - list storing all the sensor reads for the instrument/serial number
	 * @param addedReadings - readings which have been successfully added into the database
	 * @return
	 */
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
	 * This function calls a query function which adds a data type unit into the table
	 * storing attributes and their corresponding data types
	 * @param attribute - name of the attribute. e.g. "Pressure", "Volume"
	 * @param dataType - unit type of attribute. e.g. "MPa", "M"
	 * @return <b>true</b> - successfully inserted tuple into table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean addDataUnitsType(String attribute, String dataType){
		boolean success = false; //Assume Failure
		
		//Check if tuple exists already for tuple to be created
		if(!checkIfDataUnitsTypeExists(attribute, dataType)){
			success = addDataUnitsTypeQuery(attribute, dataType);
		}
		
		return success;
	}
	
	/**
	 * This function invokes a query call to update an ADCP given the corresponding instrument
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param timeStamp - time of reading of the instrument for the corresponding data
	 * @param attrValList - list storing readings for the corresponding instrument/serial 
	 * @return <b>true</b> - successfully updated tuple into table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
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
	
	/**
	 * This function removes a tuple from a ADCPCurrent table for the corresponding 
	 * instrument
	 * 
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return <b>true</b> - successfully removed tuple from table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean removeADCPCurrentData(String instrument, String serial){
		boolean success = false;
		
		if(checkIfADCPCurrentDataExists(instrument, serial)){
			success = removeADCPCurrentDataQuery(instrument, serial);
		}
		
		return success;
	}
	
	/**
	 * This function removes a tuple from the DataUnits table
	 * @param attribute - name of the attribute in the table
	 * @param dataType - DataType of the attribute
	 * @return <b>true</b> - successfully deleted tuple from table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean removeDataUnitsType(String attribute, String dataType){
		boolean success = false;
		
		if(checkIfDataUnitsTypeExists(attribute, dataType)){
			success = removeDataUnitsTypeQuery(attribute, dataType);
		}
		
		return success;
	}
	
	/**
	 *  removeInstrument
	 * <p>
	 * This function calls a query function which removes a instrument from the 
	 * Instrument table
	 * @author Aaron D. Salinas
	 * @param instrument - name of the instrument
	 * @return <b>true</b> - successfully deleted tuple from table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
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
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return <b>true</b> - successfully deleted tuple from table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
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
	
	/**
	 * This function removes a sensor reading from its corresponding instrument sensor
	 * readings table
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param readings - information of tuples to be deleted
	 * @return <b>true</b> - successfully deleted all tuple from table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
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
	
	/**
	 * This function drops a ADCP table from the database
	 * @param instrument - name of the instrument whose ADCPCurrent table is to be droped
	 * @return <b>true</b> - successfully dropped table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
	static public boolean dropADCPCurrentTable(String instrument){
		String query = "DROP TABLE `ADCPCurrentData_" + instrument + "`";
		boolean success = false;
		
		//Check if instrument exists
		if(checkIfADCPCurrentTableExists(instrument)){
			dropTable(query);
		}
		
		return success;
	}
	
	/**
	 * This function drops a sensor reading table from the database
	 * @param instrument - name of instrument whose sensor reading table is to be dropped from database
	 * @return <b>true</b> - successfully dropped table<br>
	 * 		   <b>false</b> - unsuccessful/failure in query
	 */
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
	 * @return list of instruments stored in the database
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
	 * @return list of all instruments and serial numbers stored in the database
	 */
	static public ArrayList<ArrayList<String>> toListAllInstrumentSerials(){
		ArrayList<ArrayList<String> > tempList = new ArrayList<ArrayList<String> >();
		String query = "SELECT * FROM InstrumentSerial";
		
		if(toListTuples(query, tempList) == false){
			System.err.println("Error in retrieving information");
		}

		return tempList;
	}
	
	/**
	 * This function calls a query which returns all serial numbers stored in the database
	 * for a given instrument
	 * @param instrument - name of the instrument
	 * @return List of serial numbers for a given instrument
	 */
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
	
	/**
	 * This function calls a query which returns the ADCPCurrent data for a given instrument
	 * and serial number combination
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return List storing the information of the tuples in the database
	 */
	static public ArrayList<String> toListADCPCurrentDataInstrSerial(String instrument, String serial){
		
		ArrayList<ArrayList<String> >tempList = new ArrayList<ArrayList<String> >();
		ArrayList<String> ADCPCurTuple = new ArrayList<String>();
 		
		String query = "SELECT * FROM `ADCPCurrentData_" + instrument + "` WHERE ";
		query = query + "`Instrument` = \'" + instrument +"\' AND `Serial Number` = \'" + serial + "\'";
		
		if(toListTuples(query, tempList) == false){
			System.err.println("Error in retrieving information");
		}else{
			//Store result set in one list from big list of list
			for(int i = 0; i < tempList.get(0).size(); i++){
				ADCPCurTuple.add(tempList.get(0).get(i));
			}
		}
		
		return ADCPCurTuple;
	}
	
	/**
	 * This function returns the column names of a corresponding CurrentData table given
	 * the name of an instrument
	 * @param instrument - name of instrument
	 * @return List storing the name of columns in the corresponding table
	 */
	static public ArrayList<String> toListADCPTableAttributes(String instrument){
		ArrayList<String> colNames = new ArrayList<String>();
		String tableName = "ADCPCurrentData_" + instrument;	
		
		return toListTableAttributes(tableName, colNames);
	}

	/**
	 * This function returns all list storing the ADCPCurrent data of all serial numbers 
	 * for a given instrument
	 * @param instrument - name of the instrument
	 * @return List storing all ADCPCurrent data of all serial numbers for a instrument
	 */
	static public ArrayList<ArrayList<String> > toListADCPCurrentDataInstr(String instrument){
		ArrayList<ArrayList<String> > ADCPCurTuple = new ArrayList<ArrayList<String> >();
		String query = "SELECT * FROM `ADCPCurrentData_" + instrument + "` WHERE ";
		query = query + "`Instrument` = \'" + instrument + "\'";
		
		if(toListTuples(query, ADCPCurTuple) == false){
			System.err.println("Error in retrieving information");
		}else{
			
		}
		return ADCPCurTuple;
	}
	
	/**
	 * This function returns a list of all sensor readings of all serial numbers of a given
	 * instrument
	 * @param instrument - name of the instrument
	 * @return ArrayList -  storing sensor readings
	 */
	static public ArrayList<ArrayList<String> > toListSensorReadingByInstrument(String instrument){
		ArrayList<ArrayList<String> > readings = new ArrayList<ArrayList<String> >();
		String query = "SELECT * FROM `SensorReading_" + instrument + "`"
						+ " WHERE `Instrument`=\'" + instrument + "\'";
		
		if(toListTuples(query, readings) == false){
			System.err.println("Error in retrieving information");
		}
		
		return readings;
	}
	
	/**
	 * This function returns a list of all sensor readings for a given instrument/serial number combination
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return List of sensor Readings
	 */
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
	
	/**
	 * This function returns a list of sensor readings given an instrument, serial number, and time frame.
	 * Instrument type MUST be specified, other variables may be initialized to "*" to specify a wildcard
	 * 
	 * <p>
	 * "*" can be passed into the function for a serial to specify any serial number
	 * <p>
	 * "*" can be passed into the function as a start or end time specifying not to include a start/end time
	 * constraint, respectively
	 * @param instrument - name of the instrument; "*" specifies any instrument
	 * @param serial - serial number of the instrument; "*" specifies any serial number
	 * @param startTime - being time frame of readings; "*" specifies no start time
	 * @param endTime - end time frame of readings; "*" specifies no end time
	 * @return
	 */
	static public ArrayList<ArrayList<String> > toListSensorReadingByInstrumentSerialTime(String instrument, String serial, 
													String startTime, String endTime){
		ArrayList<ArrayList<String> > readings = new ArrayList<ArrayList<String> >();
		
		String query = "SELECT * FROM `SensorReading_" + instrument + "`";
		
		if(!instrument.matches("\\*") || !serial.matches("\\*") || !startTime.matches("\\*") || !endTime.matches("\\*")){
			boolean addAnd = false;
			query = query + " WHERE";
			
			//Check if instrument is specified
			if(!instrument.matches("\\*")){
				query = query + " `Instrument`=\'" + instrument + "\'";
				addAnd = true;
			}
			
			//Check if serial number is specified
			if(!serial.matches("\\*")){
				if(addAnd) query = query + " AND";
				query = query + " `Serial Number`=\'" + serial + "\'";
				addAnd = true;
			}
			//Check if start time is set, "*" represents any start
			if(!startTime.matches("\\*")){
				if(addAnd) query = query + " AND";
				query = query + " `ReadTime`>='" + startTime + "\'";
				addAnd = true;
			}
			//Check if end time is set, "*" represents any start
			if(endTime.matches("\\*") == false){
				if(addAnd) query = query + " AND";
				query = query + " `ReadTime`<='" + endTime + "\'";
			}
		}
				
		//Call query function to store all the readings
		if(toListTuples(query, readings) == false){
			System.err.println("Error in retrieving information");
		}
		
		return readings; //return result set
	}
	
	/**
	 * This function returns the result set of sensor readings stored for a given instrument
	 * @param instrument
	 * @return
	 */
	static public ArrayList<String> toListSensorReadingAttributes(String instrument){
		ArrayList<String> colNames = new ArrayList<String>();
		String tableName = "SensorReading_" + instrument;	
		
		return toListTableAttributes(tableName, colNames); //return sensor readings for instrument
	}
	
	/**
	 * This function returns the unit type stored for any given attribute.
	 * E.g. "MPa" for attribute "Pressure"; "GMT" for attribute "Datetime"
	 * @param attribute - name of attribute
	 * @return unit type of attribute
	 */
	static public String toStringAttributeUnit(String attribute){
		String unit = "";
		ArrayList<ArrayList<String> > tempList = new ArrayList<ArrayList<String> > ();
		String query = "SELECT `Unit` FROM `DataUnits` WHERE `Attribute`=\'" + attribute + "\'";
		
		if(!toListTuples(query, tempList)){
			System.err.println("Error in retrieving information");
		}
		else{
			unit = tempList.get(0).get(0);
		}
		
		return unit;
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
		return checkIfInstrumentSerialExistsQuery(instrument, serial);
	}
	
	/**
	 * This function checks if a ADCP Current exists for a given instrument
	 * @param instrument - name of instrument
	 * @return <b>true</b> - if table exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfADCPCurrentTableExists(String instrument){
		boolean exists = false;
		
		exists = checkIfADCPCurrentTableExistsQuery(instrument); 
		
		return exists;
	}
	
	/**
	 * This function checks if a ADCPCurrent reading exists in the table for 
	 * a given instrument/serial number combination
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfADCPCurrentDataExists(String instrument, String serial){
		boolean exists = false;
		
		exists = checkIfADCPCurrentDataExistsQuery(instrument, serial);
		
		return exists;
		
	}
	
	/**
	 * This function checks if a ADCPCurrent reading passed in newer than the one already stored
	 * in the database for a given instrument/serial number combination
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param timeStamp - timeStamp associated with the reading passed in
	 * @return <b>true</b> - if reading passed in is newer exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfADCPCurrentDataNewer(String instrument, String serial, String timeStamp){		
		return checkIfADCPCurrentDataNewerQuery(instrument, serial, timeStamp);
	}
	
	/**
	 * This function checks if a attribute is already stored in the DataTypes table in the database
	 * @param attribute - name of th attribute
	 * @param dataType - unit associated with the datatype
	 * @return <b>true</b> - if in table <br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfDataUnitsTypeExists(String attribute, String dataType){
		return checkIfDataUnitsTypeExistsQuery(attribute);
	}
	
	/**
	 * This function checks if a sensorReading table for a given instrument exists in the system
	 * @param instrument - name of the instrument
	 * @return <b>true</b> - if table exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfSensorReadingTableExists(String instrument){		
		return checkIfSensorReadingTableExistsQuery(instrument);
	}
	
	/**
	 * This function checks if a sensorReading stored in an instruments sensorReading table
	 * exists
	 * @param instrument- name of the instrument
	 * @param serial - serial number associated with the instrument
	 * @param timeStamp - time stamp assocaited with the reading time
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static public boolean checkIfSensorReadingExists(String instrument, String serial, String timeStamp){		
		return checkIfSensorReadingExistsQuery(instrument, serial, timeStamp);
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
	
	/**
	 * This function calls a query function which adds a new data unit type into the 
	 * data unit table
	 * @param attribute - name of the attribute
	 * @param dataType - unit type of attribute
	 * @return <b>true</b> - if added<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean addDataUnitsTypeQuery(String attribute, String dataType){
		String query = "INSERT INTO `DataUnits` (`Attribute`, `Unit`) VALUES("
					 + "\'" + attribute + "\',\'" + dataType + "\')";
		return insertIntoTable(query); //Attempt to successfully run query, return result 
	}
	
	/**
	 * This function adds a new ADCPCurrent tuple into the corresponding ADCPCurrent
	 * table for a specified instrument
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param timeStamp - time associated with read
	 * @param attrValList - column values of the row to be added
	 * @return <b>true</b> - if added<br>
	 * 		   <b>false</b> - otherwise
	 */
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

	/**
	 * This function adds a sensor reading into the corresponding table for a specified
	 * instrument
	 * @param instrument - name of the instrument
	 * @param serial - serial number of instrument 
	 * @param sensorReadings - list of sensor readings to be added
	 * @param addedReadings - number of sensor readings successfully added into database
	 * @return <b>true</b> - if all added<br>
	 * 		   <b>false</b> - otherwise
	 */
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

	/**
	 * This function calls a query to add a new ADCPCurrent table for a specified instrument
	 * into the database
	 * @param instrument - name of the instrument
	 * @param ADCPAttrDataType - column attributes of the table
	 * @return <b>true</b> - if table created<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean createADCPCurrentTableQuery(String instrument,
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
	
	/**
	 * This function calls a query function to create a new Sensor Reading table for a 
	 * specified instrument
	 * @param instrument - name of the instrument
	 * @param attrDataTypeList - column attributes of the table
	 * @return <b>true</b> - if table created<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean createInstrumentSensorReadingTableQuery(String instrument, List<Pair<String, String> > attrDataTypeList){
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
	
	/**
	 * This function calls a query to update a existing for in a ADCPCurrent table for a specified instrument/serial number
	 * combination
	 * @param instrument - name of the instrument 
	 * @param serial - serial number of the instrument
	 * @param attrVal - column values to be updated to
	 * @return <b>true</b> - if tuple updated<br>
	 * 		   <b>false</b> - otherwise
	 */
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
	
	/**
	 * This function removes a tuple from a ADCPCurrent table for a specified instrument/serial number
	 * combination
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @return <b>true</b> - if tuple removed<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean removeADCPCurrentDataQuery(String instrument, String serial){
		boolean success = false;
		
		String tableName = "ADCPCurrentData_" + instrument;
		String query = "DELETE FROM `" + tableName + "` WHERE `Instrument`=\'" + instrument
					+ "\' AND `Serial Number`=\'" + serial + "\'";
		
		success = deleteFromTable(query); //Delete ADCPCurrent reading from table for instrument
		
		return success;
	}
	
	/**
	 * This function removes a tuple from the DataUnits table given the column values of a row
	 * in the table
	 * @param attribute - name of attribute
	 * @param dataType - unit type of attribute
	 * @return <b>true</b> - if tuple removed<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean removeDataUnitsTypeQuery(String attribute, String dataType){
		String query = "DELETE FROM `DataUnits`" 
					 + " WHERE `Attribute`=\'" + attribute + "\' AND "
					 + "`Unit`=\'" + dataType + "\'";
		
		return deleteFromTable(query);
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
					+ "AND `Serial Number` = '" + serial + "';";	
			
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
	 * This function calls a query to remove a sensor reading from an instruments sensor reading table
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param readings - column values of the row to be deleted
	 * @return <b>true</b> - if tuple deleted<br>
	 * 		   <b>false</b> - otherwise
	 */
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

	/**
	 * This function calls a query which checks if a ADCPCurrent table exists for a specified instrument
	 * @param instrument - name of instrument
	 * @return <b>true</b> - if table exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean checkIfADCPCurrentTableExistsQuery(String instrument){
		String tableName = "ADCPCurrentData_" + instrument;			
		
		return checkIfTableExists(tableName);
	}
	
	/**
	 * This function calls a query which checks to see if a ADCPCurrentData tuple exists 
	 * in the corresponding table for an instrument
	 * @param instrument - name of the instrument
	 * @param serial - serial number of instrument
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
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
	
	/**
	 * This function calls a query which checks if a ADCPCurrent reading passed into the function is 
	 * newer then the one stored in the database
	 * @param instrument - name of the instrument
	 * @param serial - serial number of the instrument
	 * @param timeStamp - time stamp associated with the info passed into the function
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
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
	
	/**
	 * This function calls a query function which checks to see if a attribute
	 * exists in the DataUnits table in the database
	 * @param attribute - name of the attribute
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean checkIfDataUnitsTypeExistsQuery(String attribute){
		String query = "SELECT COUNT(*) "
					 + "FROM `DataUnits`"
					 + " WHERE `Attribute`=\'" + attribute + "\'";
		
		return checkIfExists(query);
	}
	
	/**
	 * This function calls a query which checks if a sensor Reading table for a specified instrument
	 * exists in the database.
	 * @param instrument - name of the instrument
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean checkIfSensorReadingTableExistsQuery(String instrument){
		String tableName = "SensorReading_" + instrument;
		
		return checkIfTableExists(tableName);
	}
	
	/**
	 * This function calls a query function which checks if a SensorReading stored in an instruments 
	 * corresponding sensorReadings table exists 
	 * @param instrument - name of the instrument
	 * @param serial - serial number associated with the instrument
	 * @param timeStamp - time stamp associated with the sensorReading to be checked
	 * @return <b>true</b> - if exists<br>
	 * 		   <b>false</b> - otherwise
	 */
	static private boolean checkIfSensorReadingExistsQuery(String instrument, String serial, String timeStamp){

		String tableName = "SensorReading_" + instrument; //name of sensor reading table
		
		String query = "SELECT COUNT(*) "
			  + "FROM `" + tableName + "` "
			  + "WHERE `Instrument` = \'" + instrument + "\' AND "
			  + "`Serial Number` = '" + serial + "' AND `ReadTime` = \'" + timeStamp + "\'";
		
		return checkIfExists(query);
	}
}



