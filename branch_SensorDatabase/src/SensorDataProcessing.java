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
	public List<String> toListFileData(String fileName){	 
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
}
