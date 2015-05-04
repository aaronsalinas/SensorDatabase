import java.io.File;
import java.util.ArrayList;

public class StoreSensorData {
	
	private SensorDataProcessing data;
	private String fileName = "";
	private boolean stored = false;
	private boolean DBAccess = false;
	
	/**
	 * Default constructor
	 * <p>
	 * This function invokes a function that calls the SensorDataProcessing
	 * class which parses and stores the information. After the data from the file
	 * is stored it is added to the database.
	 * @param file
	 */
	public StoreSensorData(String file){
		fileName = file; //Check that file can be opened
		DBAccess = Database.checkIfDatabaseConnect(); //Check that Database can be connected to
		data = new SensorDataProcessing(file); //Store parsed information from file

		
		if(data.isValidFile() && DBAccess){
			stored = storeInfoInDB(); //Store information into database
			//Test Print
			if(stored){
				System.out.println("Successful Store");
			}else{
				System.out.println("ERROR: Unsuccessful Store!");
			}
		}
		else{
			System.out.println("Invalid File");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean storeInfoInDB(){
		
		boolean success = true; //Assume true, if failure in any step of the query, will be false
		
		String instrument = data.getInstrument();
		String serial = data.getSerial();
		String timeStamp = data.getTimeStamp();
		ArrayList<Pair<String, String> > ADCPAttrDataType = data.toListADCPAttrDataTypeList();
		ArrayList<Pair<String, String> > ADCPCurrentData = data.toListADCPCurrentData();
		ArrayList<Pair<String, String> > readingAttrDataType = data.toListSensorReadingRowAttrTypeList();
		ArrayList<ArrayList<Pair<String, String> > > sensorReadings = data.toListSensorReadValues();
		ArrayList<Pair<String, String> > realDataTypes = data.toListRealTypesList(); //Store Real Data types for attributes, e.g. 'DEG. C', 'M', 'PSU'
		ArrayList<Pair<String, String> > ADCPCurDataRevert = new ArrayList<Pair<String, String> >(); //Stores old version of ADCP Current Value before update
		ArrayList<ArrayList<Pair<String, String> > > addedReadingsList = new ArrayList<>();
		ArrayList<Pair<String, String> > addedUnitsList = new ArrayList<Pair<String, String> >();

		boolean addedInstrument = false;
		boolean addedInstrSerial = false;
		boolean createADCPCurTable = false;
		boolean addedADCPCurrentData = false;
		boolean updatedADCPCurrentData = false;
		boolean createReadingTable = false;
		boolean addedReadings = false;
		boolean addedDataUnits = false;
		
		
		try{
			//Check if Instrument exists in the database
			if(!SensorDatabaseAccess.checkIfInstrumentExists(instrument)){
				//Add Instrument into DB if not exists
				if(SensorDatabaseAccess.addInstrument(instrument)){
					addedInstrument = true;
				}
				else{ //Instrument not added due to error in query
					success = false;
				}
			}
	
			//Check if Instrument/Serial exists in the database
			if(success && !SensorDatabaseAccess.checkIfInstrumentSerialExists(instrument, serial)){
				//Add Instrument/Serial into DB if not exists
				if(SensorDatabaseAccess.addInstrumentSerial(instrument, serial)){
					addedInstrSerial = true;
				}
				else{ //Instrument/Serial not added due to error in query
					success = false;
				}
			}
			
			
			//Check if ADCP CurrentData Table exists for the database
			if(success && !SensorDatabaseAccess.checkIfADCPCurrentTableExists(instrument)){
				//Create ADCP Table for the current instrument
				if(SensorDatabaseAccess.createADCPCurrentTable(instrument, ADCPAttrDataType)){
					createADCPCurTable = true;
				}
				else{ //ADCP Table not created due to error in query
					success = false;
				}
			}
	
			//Check if tuple for ADCPCurrent exists
			if(success && !SensorDatabaseAccess.checkIfADCPCurrentDataExists(instrument, serial)){
				//Add tuple for current instrument into ADCPCurrent
				if(SensorDatabaseAccess.addADCPCurrentData(instrument, serial, timeStamp, ADCPCurrentData)){
					addedADCPCurrentData = true;
				}
				else{
					success = false;
				}
			}//Update tuple if current ADCP is newer than one stored in DB
			else if(success && SensorDatabaseAccess.checkIfADCPCurrentDataNewer(instrument, serial, timeStamp)){
				
				//Store Copy of current existing tuple for ADCPCurrentData for instrument
				ArrayList<String> tempStr = SensorDatabaseAccess.toListADCPCurrentDataInstrSerial(instrument, serial); //Get values for current stored CurrentData			
				
				ADCPCurDataRevert.add(new Pair<String,String>("ReadTime", tempStr.get(0)));
							
				for(int i = 1; i < tempStr.size(); i++){
					Pair<String, String> tempPair = new Pair<String, String>(ADCPCurrentData.get(i).first, tempStr.get(i));
					ADCPCurDataRevert.add(tempPair);
				}
				
				//Update existing tuple for instrument/serial in ADCPCurrentData table
				if(SensorDatabaseAccess.updateADCPCurrentData(instrument, serial, timeStamp, ADCPCurrentData)){
					updatedADCPCurrentData = true;
				}
			}
						
			/* Check If table to store reads exists */
			if(success && !SensorDatabaseAccess.checkIfSensorReadingTableExists(instrument)){
				
				//Store units of the attributes for the new table
				for(int i = 0; i < realDataTypes.size(); i++){
					Pair<String, String> tempUnitsPair = new Pair<String, String>(realDataTypes.get(i).first, realDataTypes.get(i).second);
					//Check if unit is stored in the database already
					if(!SensorDatabaseAccess.checkIfDataUnitsTypeExists(tempUnitsPair.first, tempUnitsPair.second)){
						//Add unit it is it not stored in the database
						if(SensorDatabaseAccess.addDataUnitsType(tempUnitsPair.first, tempUnitsPair.second)){
							//Keep reference to which Units were added into database
							addedDataUnits = true;
							addedUnitsList.add(tempUnitsPair);
						}
						
					}
				}
				
				/* Create new table to store reads for the current instrument */
				if(SensorDatabaseAccess.createInstrumentSensorReadingTable(instrument, readingAttrDataType)){
					createReadingTable = true;				
				}
				else{
					success = false;
				}
			}
						
			//Add all sensor readings into tables
			addedReadings = true; //Assume all tuples can be added, If not set false
			if(success){
				if(SensorDatabaseAccess.addSensorReadings(instrument, serial, sensorReadings, addedReadingsList)){
					//Do Nothing If Successful
					addedReadings = true;
				}
				else{
					success = false;
					addedReadings = false;
				}
				
			}
		}
		catch(Exception exc){
			success = false;
		}
		
		
		/* If failure in any steps, undo all changes in reverse order*/
		if(!success){
			
			//Undo Insert into SensorReading table for instrument
			if(addedReadings) SensorDatabaseAccess.removeSensorReadings(instrument, serial, addedReadingsList);
			
			//Drop SensorReading table if created
			if(createReadingTable) SensorDatabaseAccess.dropSensorReadingTable(instrument);
			
			//Undo added units into database
			if(addedDataUnits){
				//Remove the units that were added into the database
				for(int i = 0; i < addedUnitsList.size(); i++){
					SensorDatabaseAccess.removeDataUnitsType(addedUnitsList.get(i).first, addedUnitsList.get(i).second);
				}
			}
			
			//Undo ADCP Current Add/Update into ADCP Current Table
			if(addedADCPCurrentData){
				SensorDatabaseAccess.removeADCPCurrentData(instrument, serial);
				
			}
			else if(updatedADCPCurrentData){
				SensorDatabaseAccess.updateADCPCurrentData(instrument, serial, ADCPCurDataRevert.get(0).second, ADCPCurDataRevert);
			}
			
			//Drop ADCP Current Table
			if(createADCPCurTable) SensorDatabaseAccess.dropADCPCurrentTable(instrument);
			
			//Undo Instrument/Serial
			if(addedInstrSerial) SensorDatabaseAccess.removeInstrumentSerial(instrument, serial);
			
			//Undo Instrument
			if(addedInstrument) SensorDatabaseAccess.removeInstrument(instrument);
			
			
		}	
		
		return success;
	}
	
	
	/**
	 * This class attempts to open a file. If the file can be opened 
	 * then it returns true, false otherwise
	 * @param file
	 * @return
	 */
	public boolean fileValid(String file){
		boolean valid = false;	
		File f = new File(file);
		if(f.exists() && !f.isDirectory()) { 
			valid = true;
		}else{
			valid = false;
		}	
		return valid;
	}
	
	
}
