import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseUnits {
		
	/* ******************************* /
	*	Default Attribute Data types   /
	***********************************/
	/*Primitive Units*/
	static public final String INT_UNIT 			= "INT";
	static public final String DOUBLE_UNIT 			= "DOUBLE";
	static public final String VARCHAR45_UNIT		= "VARCHAR(45)";
	static public final String BOOL_UNIT			= "BOOL";
	static public final String CHARACTER_UNIT		= "CHARACTER";
	static public final String DATETIME_UNIT 		= "DATETIME";
	static public final String STRING_UNIT			= VARCHAR45_UNIT;
	
	/*Measurement Units*/
	static public final String CM_PER_S_UNIT 		= "DOUBLE"; 	// cm/s
	static public final String DEG_CELSIUS_UNIT 	= "DOUBLE"; 	// Degrees Celsius - Temperature
	static public final String DEG_METERS_UNIT 		= "DOUBLE";		// Degree Meter - Compass Heading
	static public final String DEGREE_UNIT 			= "DOUBLE"; 	// Degree (Angular)
	static public final String METERS_UNIT 			= "DOUBLE"; 	// Meters (Length) 
	static public final String PSU_UNIT 			= "DOUBLE";		// PSU (Practical Salinity Units) - Salinity
	static public final String MPA_UNIT 			= "DOUBLE";		// MPa (Mega Pascal) - Pressure
	
	/*Time Units (GMT Default)*/
	static public final String GMT_YEAR 	= "CHAR(4)";	// Year
	static public final String GMT_MONTH 	= "CHAR(2)";	// Month
	static public final String GMT_DAY 		= "CHAR(2)";	// Day
	static public final String GMT_HOUR		= "CHAR(2)";	// Hour
	static public final String GMT_MINUTE 	= "CHAR(2)";	// Minute
	static public final String GMT_SECOND	= "CHAR(2)";	// Second
	
	/*Location Units*/
	static public final String LATITUDE_UNIT 	= "DOUBLE"; // Latitude
	static public final String LONGITUDE_UNIT 	= "DOUBLE"; // Longitude
	
	/* Miscellaneous Units */
	/**
	 * This function returns the string representation of the default CHAR mySql data type.
	 * An int x is passed into the function which acts as the length of the CHAR.
	 * i.e. x = 5 returns "CHAR(5)" - A CHAR of size 5
	 * @author Aaron D. Salinas
	 * @param x - length of char data type 
	 * @return  String - "CHAR(x)"
	 */
	static public String CHAR_UNIT(int x){
		return "CHAR(" + x + ")";
	}
	
	/**
	 * This function returns the string representation of the default VARCHAR mySql data type.
	 * An int x is passed into the function which acts as the initial length of the VARCHAR.
	 * i.e. x = 5 returns "VARCHAR(5)" - A VARCHAR of size 5
	 * @author Aaron D. Salinas
	 * @param x - length of char data type 
	 * @return  String - "VARCHAR(x)"
	 */
	static public String VARCHAR_UNIT(int x){
		return "VARCHAR(" + x + ")";
	}
	
	static private List<String> primitiveUnits = new ArrayList<>(Arrays.asList(INT_UNIT, DOUBLE_UNIT, BOOL_UNIT, CHARACTER_UNIT, DATETIME_UNIT));

	static public boolean validDatabaseDatatype(String dataType){
		
		for(int i = 0; i < primitiveUnits.size(); i++){
			if(dataType.toUpperCase().matches(primitiveUnits.get(i))){
				return true;
			}
		}
		
		if (dataType.toUpperCase().startsWith("VARCHAR(") && dataType.toUpperCase().endsWith(")")){
		    return true;
		}
		
		if (dataType.toUpperCase().startsWith("CHAR(") && dataType.toUpperCase().endsWith(")")){
		    return true;
		}
		
		return false;
	}
		
}
