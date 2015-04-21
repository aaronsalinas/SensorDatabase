import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to process Sensor information/data retrived from specified text files
 * provided by the user.
 * 
 * @author aaronsalinas
 */
public class SensorDataProcessing {
	
	/**
	 * readFileData
	 * <p>
	 * Given a file name, this function opens the file and extracts the sensor readings from the file
	 * and stores each reading in list of strings, each string pertaining to information for one single 
	 * sensor read
	 * @param fileName - name of file to extract sensor readings
	 */
	public List<String> toListFileDataReadings(String fileName){	 
		BufferedReader br = null;
		List<String> sensorReadings = new ArrayList<String>();
		
		try {
			String sCurrentLine;
			String inputString;
			
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("*") == false){
					sensorReadings.add(sCurrentLine);
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return sensorReadings; 
	}


	/**
	 * toListFileDataAttributes
	 * <p>
	 * Given the name of a file which contains sensor information and readings,
	 * the function parses through the file and stores the attributes for the sensor readings.
	 * These attributes correspond to the rows of readings that the sensor takes and stores in the file.
	 * 
	 * @param fileName
	 * @return
	 */
	public List<String> toListFileDataAttributes(String fileName){
		List<String> rowAttributes = new ArrayList<String>();
		BufferedReader br = null;

		try {
			String sCurrentLine;
			String inputString;
			
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			
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
					rowAttributes.add(sCurrentLine);
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
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
	 */
	public List<String> toListADCPCurrentData(String fileName){
		List<String> ADCPCurrentData = new ArrayList<String>();
		BufferedReader br = null;

		try {
			String sCurrentLine;
			String inputString;
			
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			
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
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		return ADCPCurrentData;
	}
}