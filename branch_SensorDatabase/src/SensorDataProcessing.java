import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorDataProcessing{
	private String filename;
	private String instrument;
	private String serial;
	private String timeStamp;
	private boolean validFile = false;
	private ArrayList<Pair<String, String> > ADCPCurrentData = new ArrayList<Pair<String, String> >();
	
	/* Stores ADCP Attribute and Type (Attribute, Data Type) */
	private ArrayList<Pair<String, String> > ADCPAttrDataTypeList = new ArrayList<Pair<String, String> >();
	
	private ArrayList<Pair<String, String> > realTypesList = new ArrayList<Pair<String, String> >();
	
	/* Stores sensor Readings (Attribute, Value) in a list of list of pairs*/
	private ArrayList<ArrayList<Pair<String, String> > > sensorReadValues = new ArrayList<ArrayList<Pair<String, String> > >();
	private ArrayList<Pair<String, String> > sensorReadingRowAttrTypeList = new ArrayList<Pair<String, String> >();

	
	/**
	 * Default Constructor
	 * @param file
	 */
	public SensorDataProcessing(String file){
		//Assign default values
		filename = file;
		instrument = "";
		serial = "";
		timeStamp= "";
		
		//Try to store the information if the file is valid
		if(fileExists(filename) && isValidFile()){
			try{
				storeInformation(filename);
				
			}catch(Exception exc){
				validFile = false;
				System.out.println("ERROR READING FILE!");
			}
		}
	}
	
	/**
	 * This function test to see if a file exists/can be opened to be read
	 * @param filename
	 * @return
	 */
	public boolean fileExists(String filename){
		boolean isFile = true;
		File f = new File(filename);
		if(f.exists() && !f.isDirectory()) { 
			return true;
		}else{
			return false;
		}
	}
	
	private boolean validateSensorReadFile(){
		boolean valid = true;
		
		if(fileExists(filename)){
			BufferedReader br = null;
			String sCurrentLine;
			
			try {
				br = new BufferedReader(new FileReader(filename));
				int idx = 0;
				while((sCurrentLine = br.readLine()) != null && valid && idx < 10){
					if(idx == 0 || idx == 9){
						if(!sCurrentLine.startsWith("*")){
							valid = false;
						}
					}else{
						if(!sCurrentLine.startsWith("**")){
							valid = false;
						}
					}
					
					idx++;
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return valid;
	}
	
	/**
	 * This function parses a file storing sensor readings and stores all of the information
	 * as member variables for later reference for the user.
	 * @param fileName
	 * @throws IOException
	 */
	public void storeInformation(String fileName) throws Exception{
		BufferedReader br = null;
		String sCurrentLine;
		
		br = new BufferedReader(new FileReader(fileName));
		
		while ((sCurrentLine = br.readLine()) != null) {
			//Skip past irrelevant information
			while((sCurrentLine = br.readLine()) != null){
				if(sCurrentLine.startsWith("*ADCP Current Data") == true){
					break;
				}	
			}
			//Read attribute information
			ADCPCurrentData = new ArrayList<>();
			ADCPAttrDataTypeList = new ArrayList<>();
					
			while((sCurrentLine = br.readLine()) != null){
				//End of current data reached. Break loop, no more info to read
				if(sCurrentLine.startsWith("*") && sCurrentLine.length() == 1){
					break; 
				}
				else{
					//Format: 'attribute : value'
					sCurrentLine = sCurrentLine.substring(1).trim();
										
					String tempPart1 = "";
					String tempPart2 = ""; 
					tempPart1 = tempPart1 + sCurrentLine;
					String[] temp;
					temp = tempPart1.split(":", 2);
					tempPart1 = temp[0];
					tempPart2 = temp[1];
					
					if(tempPart1.trim().matches("Time Stamp")){
						tempPart1 = "ReadTime";
					}
					
					Pair<String, String> a = new Pair<String,String>(tempPart1.trim(), tempPart2.trim());
					Pair<String, String> attrType = new Pair<String,String>(tempPart1.trim(), DatabaseUnits.DOUBLE_UNIT);
					
					//Store ReadTime as DATETIME unit for database
					if(tempPart1 == "ReadTime"){
						attrType.second = DatabaseUnits.DATETIME_UNIT;
					}
					
					/* Add ADCP Current Data for the instrument */
					ADCPCurrentData.add(a);
					
					///Special Cases 
					if(sCurrentLine.startsWith("Instrument") == true){
						this.instrument = tempPart2.trim();
					}
					if(sCurrentLine.startsWith("Serial Number") == true){
						this.serial = tempPart2.trim();
					}
					if(sCurrentLine.startsWith("Time Stamp") == true){
						this.timeStamp = tempPart2.trim();
					}
					
					//Store Attribute/Data Type pairs in list
					if(attrType.first.matches("Instrument")){
						attrType.second = DatabaseUnits.STRING_UNIT;
					}
					else if(attrType.first.matches("Serial Number")){
						attrType.second = DatabaseUnits.STRING_UNIT;
					}
					else if(attrType.first.matches("Mooring")){
						attrType.second = DatabaseUnits.STRING_UNIT;
					}
					else if(attrType.first.matches("Time Stamp")){
						attrType.second = DatabaseUnits.DATETIME_UNIT;
					}
					
					//PUSH PAIR INTO ATTRIBUTE/DATA TYPE LIST
					ADCPAttrDataTypeList.add(attrType);			
				}
			}
			
			while((sCurrentLine = br.readLine()) != null){
				if(sCurrentLine.startsWith("*Parameter") == true){
					break;
				}	
			}
			Boolean isTime = false;
			Boolean addOnce = false;
			String dateTimeUnit = DatabaseUnits.DATETIME_UNIT;
			//Read attribute information
			while((sCurrentLine = br.readLine()) != null){
				//End of attributes reached. Break loop, no more info to read
				if(sCurrentLine.startsWith("*END*")){
					break; 
				}
				else{
					sCurrentLine = sCurrentLine.substring(1);

					String v = "";
					StringTokenizer st = new StringTokenizer(sCurrentLine);
					String x = "";
					String[] parts = sCurrentLine.split(" ");
					String lastWord = parts[parts.length - 2];
					
					String tempAttr = "";
					String tempType = DatabaseUnits.DOUBLE_UNIT;
				
					//Store the Attribute
					while (st.hasMoreTokens()) {
						String n = st.nextToken();	 
						if(isInteger(n)){
							 tempAttr = v.trim(); 
							 v = "";
						}
						else{
							 v += " ";
							 v += n;
						}
					}
					
					
					//Store the Data Type
					if(!lastWord.equals(" ")){
						x = lastWord;
						x += " ";
						x += parts[parts.length - 1];
					}
					else{
						x = parts[parts.length - 1];
					}
					tempType = x.trim();
					
					
					
					//Store Attribute/DataType for rows of the readings
					if(x.equals("GMT") && isTime == false){
						if(addOnce == false){
							tempType = DatabaseUnits.DATETIME_UNIT;
							addOnce = true;
						}
						isTime = true;
					}
					
					//Add to list storing sensor reading Attribute
					sensorReadingRowAttrTypeList.add(new Pair<String,String>(tempAttr, tempType.trim()));
					
				}
			}
			
			/* Store the sensor readings from file */
			while ((sCurrentLine = br.readLine()) != null) {				
				if(sCurrentLine.startsWith("*") == false){
					String[] tempRead = sCurrentLine.split("\\s+");
					
					for(int i = 1; i < 5; i++){
						if(tempRead[i].length() == 1){
							tempRead[i] = "0" + tempRead[i];
						}
					}
					
					//Store time stamp for current read index [0 - 5] Year Month Day Hour Min Sec
					String tempTimeStamp = "";
					tempTimeStamp = tempTimeStamp + tempRead[0].trim() + "-"; //year
					tempTimeStamp = tempTimeStamp + tempRead[1].trim() + "-"; //month
					tempTimeStamp = tempTimeStamp + tempRead[2].trim() + " "; //day
					
					tempTimeStamp = tempTimeStamp + tempRead[3].trim() + ":"; //hour
					tempTimeStamp = tempTimeStamp + tempRead[4].trim() + ":"; //minute
					tempTimeStamp = tempTimeStamp + tempRead[5].trim(); //second
					
					//Store time stamp in list storing reading for current row
					ArrayList<Pair<String, String> > tempRowList = new ArrayList<Pair<String,String> >();
					tempRowList.add(new Pair<String,String>("ReadTime", tempTimeStamp));
					
					//Add the rest of the attributes in the file
					for(int i = 6; i < tempRead.length; i++){
						tempRowList.add(new Pair<String,String>(sensorReadingRowAttrTypeList.get(i).first, tempRead[i].trim() ));
					}
					
					//Add row into the list holding all the reads for the file
					sensorReadValues.add(tempRowList);
				}   
			}	
		}
		
		//Update Sensor Readings Attribute/DataType to account for conversion from GMT fields to a Datetime Unit
		//Store the types of data types
		ArrayList<Pair<String, String >> tempPairList = new ArrayList<Pair<String, String> >();
		
		tempPairList.add(new Pair<String, String>("ReadTime", DatabaseUnits.DATETIME_UNIT));
		for(int i = 6; i < sensorReadingRowAttrTypeList.size(); i++){
			tempPairList.add(new Pair<String, String>(sensorReadingRowAttrTypeList.get(i).first, DatabaseUnits.DOUBLE_UNIT));
			realTypesList.add(sensorReadingRowAttrTypeList.get(i));
		}
		
		sensorReadingRowAttrTypeList = tempPairList;
		
		br.close();
	}
	
	public String getInstrument(){
		return instrument;
	}

	public String getSerial(){
		return serial;
	}
	
	public String getTimeStamp(){
		return timeStamp;
	}

	public String getFileName(){
		return filename;
	}
	
	public ArrayList<Pair<String, String> > toListADCPCurrentData(){
		return ADCPCurrentData;
	}
	
	public ArrayList<Pair<String, String> > toListADCPAttrDataTypeList(){
		return ADCPAttrDataTypeList;
	}
	
	public ArrayList<ArrayList<Pair<String, String> > > toListSensorReadValues(){
		return sensorReadValues;
	}
	
	public ArrayList<Pair<String, String> > toListSensorReadingRowAttrTypeList(){
		return sensorReadingRowAttrTypeList;
	}
	
	public ArrayList<Pair<String, String> > toListRealTypesList(){
		return realTypesList;
	}
	
	/**
	/**
	 * isInteger
	 * 
	 * This function checks to see if a string is an integer.
	 * 
	 * @param s		string passed in to check if integer
	 * @return true
	 */
	public boolean isInteger(String s) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),10) < 0) return false;
	    }
	    return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isValidFile(){
		return validateSensorReadFile();
	}
	
	/**
	 * 
	 */
	public void testPrint(){
		
		System.out.println("Instrument: " + instrument);
		System.out.println("Serial Number: " + serial);
		System.out.println("Time Stamp: " + timeStamp + "\n");
		
		System.out.println("------------ADCPCurrentData-----------------");
		for (int i = 0; i < ADCPCurrentData.size(); i++){
			System.out.println(i + ": \'"  + ADCPCurrentData.get(i).first + "\', \'" + ADCPCurrentData.get(i).second + "\'");
		}
		
		System.out.println("------------AttributeDataTypesList-----------------");
		for (int i = 0; i < ADCPAttrDataTypeList.size(); i++){
			System.out.println(i + ": \'"  + ADCPAttrDataTypeList.get(i).first + "\', \'" + ADCPAttrDataTypeList.get(i).second + "\'");
		}

		System.out.println("--------------sensorReadingRowAttrTypeList------------------");
		for (int i = 0; i < sensorReadingRowAttrTypeList.size(); i++){
			System.out.println(i + ": \'" + sensorReadingRowAttrTypeList.get(i).first + "\' \'" + sensorReadingRowAttrTypeList.get(i).second + "\'");
		}

		
		System.out.println("------------sensorReadValues-----------------");
		for (int i = 0; i < realTypesList.size(); i++){
			System.out.println(realTypesList.get(i).toString());
		}
		
		
		
		System.out.println("------------sensorReadValues-----------------");
		for (int i = 0; i < sensorReadValues.size(); i++){
			System.out.println(sensorReadValues.get(i).toString());
		}
		
	}
	
}


