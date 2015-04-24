import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//New Parsing Class
public class SensorDataProcessing {
	
	/**
	 * readFileData
	 * <p>
	 * Given a file name, this function opens the file and extracts the sensor readings from the file
	 * and stores each reading in list of strings, each string pertaining to information for one single 
	 * sensor read
	 * @param fileName - name of file to extract sensor readings
	 * @throws IOException 
	 */
	public static ArrayList<String> toListFileDataSensorReadings(String fileName) throws IOException{	 
		BufferedReader br = null;
		ArrayList<String> sensorReadings = new ArrayList<String>();
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*") == false){
				sensorReadings.add(sCurrentLine);
			}
		}
		br.close();
		return sensorReadings; 
	}

	/**
	 * getDataAttributes
	 * <p>
	 * Given the name of a file which contains sensor information and readings,
	 * the function parses through the file and stores the attributes for the sensor readings.
	 * These attributes correspond to the rows of readings that the sensor takes and stores in the file.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	//This gives us the lines of what the data is
	public static ArrayList<String> toListFileDataAttributes(String fileName) throws IOException{
		     ArrayList<String> rowAttributes = new ArrayList<String>();
			BufferedReader br = null;
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			//Skip past irrelevant information
			while((sCurrentLine = br.readLine()) != null){
				if(sCurrentLine.startsWith("*Parameter") == true){
					break;
				}	
			}
			//Read attribute information
			while((sCurrentLine = br.readLine()) != null){
				//End of attributes reached. Break loop, no more info to read
				if(sCurrentLine.startsWith("*END*")){
					break; 
				}
				else{
					sCurrentLine = sCurrentLine.substring(1);
					System.out.println(sCurrentLine);				
					rowAttributes.add(sCurrentLine);
				}
			}
			br.close();
			return rowAttributes;
		}
	
	/**
	 * toListADCPCurrentData
	 * <p>
	 * This function stores the current ADCP information/data from a 
	 * file whose name is passed into the function. A list of strings holds
	 * each attribute and value. Each single string has both attribute and value 
	 * which are separated by a colon, i.e, "instrument : 75kHz ADCP", "Serial Number : 123"
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList<String> toListADCPCurrentData(String fileName) throws IOException{
		ArrayList<String> ADCPCurrentData = new ArrayList<String>();
		BufferedReader br = null;
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
			
		//Skip past irrelevant information
		while((sCurrentLine = br.readLine()) != null){
			if(sCurrentLine.startsWith("*ADCP Current Data") == true){
				break;
			}	
		}
		//Read attribute information
		while((sCurrentLine = br.readLine()) != null){
			//End of current data reached. Break loop, no more info to read
			if(sCurrentLine.startsWith("*") && sCurrentLine.length() == 1){
				break; 
			}
			else{
				//Format: 'attribute : value'
				sCurrentLine = "'" + sCurrentLine.substring(1).trim() + "'";
				ADCPCurrentData.add(sCurrentLine);
				System.out.println(sCurrentLine);
			}
		}	
		br.close();
		return ADCPCurrentData;
	}

	/**
	 * toStringFileInstrument
	 * 
	 * This function returns the type of the instrument that the provided file 
	 * has taken readings for
	 * @param fileName - name of sensor data readings file
	 * @return instrument - that file readings are for
	 * @throws IOException 
	 */
	public static String toStringFileInstrument(String fileName) throws IOException{
		BufferedReader br = null;
		String instrument = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Instrument") == true){
				instrument = instrument + sCurrentLine;
				String[] temp;
				temp = instrument.split(":");
				instrument = temp[1];
				instrument.trim();
				break;
			}
		}
		br.close();
		return instrument;
	}

	/**
	 * toStringFileSerial
	 * 
	 * This function returns the serial number of the instrument stored in the sensor 
	 * data reading file. The file name is passed into the function from the user
	 * @param fileName
	 * @return serial
	 * @throws IOException 
	 */
	public static String toStringFileSerial(String fileName) throws IOException{
		BufferedReader br = null;
		String serial = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Serial Number") == true){
				serial = serial + sCurrentLine;
				String[] temp;
				temp = serial.split(":");
				serial = temp[1];
				serial.trim();
				break;
			}
		}		
		br.close();
		return serial;
	}

	/**
	 * toStringFileLatitude
	 * 
	 * This function returns the latitude of the instrument stored in the sensor 
	 * data reading file. The file name is passed into the function from the user
	 * @param fileName
	 * @return latitude
	 * @throws IOException 
	 */
	public static String toStringFileLatitude(String fileName) throws IOException{
		BufferedReader br = null;
		String latitute = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Latitude") == true){
				latitute = latitute + sCurrentLine;
				String[] temp;
				temp = latitute.split(":");
				latitute = temp[1];
				latitute.trim();
				break;
			}
		}
		br.close();
		return latitute;
	}
	
	/**
	 * toStringFileLongitude
	 * 
	 * This function returns the longitude of the instrument stored in the sensor 
	 * data reading file. The file name is passed into the function from the user
	 * @param fileName
	 * @return longitude
	 * @throws IOException 
	 */
	public static String toStringFileLongitude(String fileName) throws IOException{
		BufferedReader br = null;
		String longitude = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Longitude") == true){
				longitude = longitude + sCurrentLine;
				String[] temp;
				temp = longitude.split(":");
				longitude = temp[1];
				longitude.trim();
				break;
			}
		}
		br.close();
		return longitude;
	}
	
	/**
	 * toStringFileWaterDepth
	 * 
	 * This function returns the water depth of the instrument stored in the sensor 
	 * data reading file. The file name is passed into the function from the user
	 * @param fileName
	 * @return waterDepth
	 * @throws IOException 
	 */
	public static String toStringFileWaterDepth(String fileName) throws IOException{
		BufferedReader br = null;
		String waterDepth = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Water Depth") == true){
				waterDepth = waterDepth + sCurrentLine;
				String[] temp;
				temp = waterDepth.split(":");
				waterDepth = temp[1];
				waterDepth.trim();
				break;
			}
		}
		br.close();
		return waterDepth;
	}
	
	/**
	 * toStringFileInstDepth
	 * 
	 * This function returns the water depth of the instrument stored in the sensor 
	 * data reading file. The file name is passed into the function from the user
	 * @param fileName
	 * @return instDepth
	 * @throws IOException 
	 */
	public static String toStringFileInstDepth(String fileName) throws IOException{
		BufferedReader br = null;
		String instDepth = "";
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*Inst Depth") == true){
				instDepth = instDepth + sCurrentLine;
				String[] temp;
				temp = instDepth.split(":");
				instDepth = temp[1];
				instDepth.trim();
				break;
			}
		}
		br.close();
		return instDepth;
	}
	
	/**
	 * toListAttributes
	 * <p>
	 * This function stores the current Parameter (attributes) from a 
	 * file whose name is passed into the function.
	 * 
	 * @param fileName
	 * @return ArrayList<String>
	 * @throws IOException 
	 */
	//This gives us the lines of what the data is
	public static ArrayList<String> toListAttributes(String fileName) throws IOException{
		     ArrayList<String> rowAttributes = new ArrayList<String>();
			BufferedReader br = null;
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			//Skip past irrelevant information
			while((sCurrentLine = br.readLine()) != null){
				if(sCurrentLine.startsWith("*Parameter") == true){
					break;
				}	
			}
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
					 while (st.hasMoreTokens()) {
						String n = st.nextToken();	 
						if(isInteger(n)){
						//	System.out.println(v);
							 rowAttributes.add(v); 
							 v = "";
						}
						else{
							 v += " ";
							 v += n;
						}
					 }
				}
			}
			br.close();
			return rowAttributes;
		}
		
	/**
	 * isInteger
	 * 
	 * This function checks to see if a string is an integer.
	 * 
	 * @param s		string passed in to check if integer
	 * @return true
	 */
	public static boolean isInteger(String s) {
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
	 * toPairListAttributesData
	 * <p>
	 * Given the name of a file which contains sensor information and readings,
	 * the function parses through the file and stores the attributes for the sensor readings along with
	 * the reading for the specific attribute. It stores these in an array list of pairs
	 * 
	 * @param fileName
	 * @param a				ArrayList of Attributes parsed from the file
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList<Pair<String, String> > toPairListAttributesData(ArrayList<String> a, String fileName) throws IOException{	
		BufferedReader br = null;
		String sCurrentLine;
		ArrayList<Pair<String, String> > elements = new ArrayList<>();
		br = new BufferedReader(new FileReader(fileName));
		int i = 1;
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.startsWith("*") == false){
				//System.out.print(i + ":\t" + sCurrentLine);
				i++; //Index number for entry
				//System.in.read(); //Waits for user to press enter (TESTING)
				StringTokenizer st = new StringTokenizer(sCurrentLine);
				int x = 0;
			    while (st.hasMoreTokens()) {
			    	String n = st.nextToken();
			    	Pair pair = new Pair(a.get(x), n);
			    	pair.setFirst(a.get(x));
			    	pair.setSecond(n);
					elements.add(pair);
					//System.out.println("<"+ a.get(x) + "," + n + " >");
			        x++;
			    }
			}
		}
		
		br.close();
		return elements;
	}
	
	
	
	/**
	 * toListDataType
	 * <p>
	 * Given the name of a file which contains sensor information and readings,
	 * the function parses through the file and stores the attribute's units (data types) in an array list.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList<String> toListDataType(String fileName) throws IOException{
	    ArrayList<String> rowAttributes = new ArrayList<String>();
		BufferedReader br = null;
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		//Skip past irrelevant information
		while((sCurrentLine = br.readLine()) != null){
			if(sCurrentLine.startsWith("*Parameter") == true){
				break;
			}	
		}
		//Read attribute information
		while((sCurrentLine = br.readLine()) != null){
			//End of attributes reached. Break loop, no more info to read
			if(sCurrentLine.startsWith("*END*")){
				break; 
			}
			else{
				sCurrentLine = sCurrentLine.substring(1);
				String v = "";
				String[] parts = sCurrentLine.split(" ");
				String lastWord = parts[parts.length - 2];
				if(!lastWord.equals(" ")){
					v = lastWord;
					v += " ";
					v += parts[parts.length - 1];
				}
				else{
					v = parts[parts.length - 1];
				}
				rowAttributes.add(v);
				//System.out.println("v:" + v);
			}
		}
		br.close();
		return rowAttributes;
	}
	
	
	/**
	 * toPairListAttributesDataType
	 * <p>
	 * Given the name of a file which contains sensor information and readings,
	 * the function parses through the file and stores the attributes for the sensor readings along with
	 * it's specific unit. It stores these in an array list of pairs
	 * 
	 * @param a				ArrayList of Attributes parsed from the file
	 * @param b				ArrayList of Units for Attributes parsed from the file
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList<Pair<String, String> > toPairListAttributesDataType(ArrayList<String> a, ArrayList<String> b) throws IOException{	
		ArrayList<Pair<String, String> > elements = new ArrayList<>();
		for (int i = 0; i < a.size(); i++){
			Pair pair = new Pair(a.get(i), b.get(i));
	    	pair.setFirst(a.get(i));
	    	pair.setSecond(b.get(i));
			elements.add(pair);
		}
		return elements;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}