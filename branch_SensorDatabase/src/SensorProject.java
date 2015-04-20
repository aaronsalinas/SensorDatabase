import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class SensorProject {
	public static void main(String [] args){
		String fileName;
		Scanner in = new Scanner(System.in);
		fileName = in.nextLine();
		
		System.out.println("Input: " + fileName);
		System.out.println("\n\n");
		
		readFileData(fileName);	
	}
	
	/**
	 * Read instrument data from specified file
	 * @param fileName
	 */
	static void readFileData(String fileName){	 
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
			String inputString;
			
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("*") == false){
					System.out.println(sCurrentLine + "\tnumber: " + i);
					i++; //Index number for entry
					System.in.read(); //Waits for user to press enter (TESTING)
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
	}
}


/**
 * Struct type object to store data attribute when creating my 
 * instrument into DB
 * @author salinasaa
 */
/*
class DataAttribute{
	String attribute; //Stores the Attribute Name
	String unitType; //unit type of attribute
	String Description; //General Description of attribute
}
*/