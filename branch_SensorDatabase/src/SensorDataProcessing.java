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

public class SensorDataProcessing {
	private String filename;
	private String instrument;
	private String serial;
	private String timeStamp;
	private ArrayList<Pair<String, String> > ADCPCurrentData;
	private ArrayList<Pair<String, String> > AttributeDataTypeList;
	
	public SensorDataProcessing(String file){
		filename = file;
		instrument = "";
		serial = "";
		timeStamp= "";
		try{
			if(fileExists(filename)){
				storeInformation(filename);
			}
		}catch(IOException exc){
			System.out.println("File Not Opened!");
		}
	}
	
	public Boolean fileExists(String filename){
		Boolean isFile = true;
		File f = new File(filename);
		if(f.exists() && !f.isDirectory()) { 
			return true;
		}else{
			return false;
		}
	}
	
	public void storeInformation(String fileName) throws IOException{
		BufferedReader br = null;
		String sCurrentLine;
		br = new BufferedReader(new FileReader(fileName));
		
		ArrayList<String> rowAttributes = new ArrayList<String>();
		ArrayList<String> rowAttributeUnits = new ArrayList<String>();
		while ((sCurrentLine = br.readLine()) != null) {
			//Skip past irrelevant information
			while((sCurrentLine = br.readLine()) != null){
				if(sCurrentLine.startsWith("*ADCP Current Data") == true){
					break;
				}	
			}
			//Read attribute information
			ADCPCurrentData = new ArrayList<>();
			AttributeDataTypeList = new ArrayList<>();
					
			while((sCurrentLine = br.readLine()) != null){
				//End of current data reached. Break loop, no more info to read
				if(sCurrentLine.startsWith("*") && sCurrentLine.length() == 1){
					break; 
				}
				else{
					//Format: 'attribute : value'
					sCurrentLine = sCurrentLine.substring(1).trim();
					//System.out.println("sCurrentLine:" + sCurrentLine);
					String tempPart1 = "";
					String tempPart2 = ""; 
					tempPart1 = tempPart1 + sCurrentLine;
					String[] temp;
					temp = tempPart1.split(":", 2);
					tempPart1 = temp[0];
					tempPart2 = temp[1];
					//System.out.println("part1:" + tempPart1);
					//System.out.println("part2:" + tempPart2);
					Pair<String, String> a = new Pair(tempPart1.trim(), tempPart2.trim());
					Pair<String, String> attrType = new Pair(tempPart1.trim(), DatabaseUnits.DOUBLE_UNIT);
					ADCPCurrentData.add(a);
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
					AttributeDataTypeList.add(attrType);			
				}
			}
			
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
					//System.out.println(sCurrentLine);				
					String v = "";
					StringTokenizer st = new StringTokenizer(sCurrentLine);
					 String x = "";
					 String[] parts = sCurrentLine.split(" ");
					String lastWord = parts[parts.length - 2];
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
					if(!lastWord.equals(" ")){
						x = lastWord;
						x += " ";
						x += parts[parts.length - 1];
					}
					else{
						x = parts[parts.length - 1];
					}
					rowAttributeUnits.add(x);
				}
			}
			ArrayList<ArrayList<Pair<String, String> > > sensorReadValues = new ArrayList<>();
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("*") == false){
					ArrayList<Pair<String, String> > elements = new ArrayList<>();
					//System.out.print(i + ":\t" + sCurrentLine);
					//System.in.read(); //Waits for user to press enter (TESTING)
					StringTokenizer st = new StringTokenizer(sCurrentLine);
					int x = 0;
					Boolean good = false;
					Boolean keepGoing = false;
					String date = "";
				    while (st.hasMoreTokens()) {
				    	String n = st.nextToken();
				    	if(rowAttributes.get(x).equals(" Year")){
							date = date + n + "/";
						}
						else if(rowAttributes.get(x).equals(" Day")){
							if(n.length() == 1){
								date += "0" + n + " ";
							}
							else{
								date += n + "/";
							}
						}
						else if(rowAttributes.get(x).equals(" Month")){
							if(n.length() == 1){
								date += "0" + n + "/";
							}
							else{
								date += n + " ";
							}
						}
						else if(rowAttributes.get(x).equals(" Hour")){
							if(n.length() == 1){
								date +=  "0" + n + ":";
							}else{
								date += n + ":";
							}
						}
						else if(rowAttributes.get(x).equals(" Minute")){
							if(n.length() == 1){
								date = date + "0" + n + ":";
							}else{
								date = date + n + ":";
							}
						}
						else if(rowAttributes.get(x).equals(" Second")){
							if(n.length() == 1){
								date = date + "0" + n;
							}
							else{
								date = date + n;
							}
						//	System.out.println("date: " + date);
							good = true;
						}
				        if(good == true && rowAttributes.get(x).equals(" east vel")){
				        	Pair pair = new Pair("ReadTime", date);
							elements.add(pair);
							date = "";
							keepGoing = true;
				        }
				        if(keepGoing == true){
				        	Pair pair = new Pair(rowAttributes.get(x), n);
							elements.add(pair);
							if(rowAttributes.get(x) == " ADCP depth"){
								good = false;
								keepGoing = false;
							}
				        }
						x++;
				    }
				    sensorReadValues.add(elements);
				 /*   for (int i = 0; i < elements.size(); i++){
						System.out.println(i + ":"  + elements.get(i).getFirst() + "," + elements.get(i).getSecond());
					}*/
				    elements.clear();
				}
			}
			
			ArrayList<Pair<String, String> > rowAttributesUnitsPair = new ArrayList<>();
			for (int i = 0; i < rowAttributes.size(); i++){
				Pair pair = new Pair(rowAttributes.get(i), rowAttributeUnits.get(i));
				rowAttributesUnitsPair.add(pair);
			}
			
		/*System.out.println("------------ADCPCurrentData-----------------");
		for (int i = 0; i < ADCPCurrentData.size(); i++){
			System.out.println(i + ":"  + ADCPCurrentData.get(i).getFirst() + "," + ADCPCurrentData.get(i).getSecond());
		}
		System.out.println("--------------------------------------------");
		System.out.println("--------------rowAttributes------------------");
		for (int i = 0; i < rowAttributes.size(); i++){
			System.out.println(i + ":" + rowAttributes.get(i));
		}
		System.out.println("---------------------------------------------");
		System.out.println("-----------rowAttributeUnits-----------------");
		for (int i = 0; i < rowAttributeUnits.size(); i++){
			System.out.println(i + ":" + rowAttributeUnits.get(i));
		}
		System.out.println("---------------------------------------------");	
		
		System.out.println("------------rowAttributesUnitsPair-----------------");
		for (int i = 0; i < rowAttributesUnitsPair.size(); i++){
			System.out.println(i + ":"  + rowAttributesUnitsPair.get(i).getFirst() + "," + rowAttributesUnitsPair.get(i).getSecond());
		}
		System.out.println("---------------------------------------------");
		System.out.println("-----------sensorReadValues-----------------");
		for (int i = 0; i < 1; i++){
			for(int j = 0; j < sensorReadValues.get(i).size(); j++){
				System.out.println(i + ":"  + sensorReadValues.get(i).get(j).getFirst() + "," 
			+ sensorReadValues.get(i).get(j).getSecond());
			}
		}
		System.out.println("---------------------------------------------");

		System.out.println("------------AttributeDataTypesList-----------------");
		for (int i = 0; i < AttributeDataTypesList.size(); i++){
			System.out.println(i + ":"  + AttributeDataTypesList.get(i).getFirst() + "," + AttributeDataTypesList.get(i).getSecond());
		}
		System.out.println("---------------------------------------------");
		*/
		}
		
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

	/**
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
	
	
	
	
}



