import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a test driver for the program
 * @author aaronsalinas
 */
public class TestDriver implements DatabaseInformation{
	public static void main(String [] args) {
		
		//testAdminDatabaseAccess();
		//testSensorDatabaseAccess();
		testSensorDataProcessing();

	}
	
	static void testAdminDatabaseAccess(){
		List<String> adminList = new ArrayList<String>();
		boolean flag;
		
		//adminList = AdminDatabaseAccess.listAllAdministrators();
		
	//	System.out.println(adminList.toString());
		
//		AdminDatabaseAccess.addAdministrator("aaron_salinas", "password", "Aaron",
//				"Salinas", "aaron_salinas@baylor.edu");
		
	//	adminList = AdminDatabaseAccess.listAllAdministrators();
	//	System.out.println(adminList.toString());
		
	//	AdminDatabaseAccess.removeAdministrator("aaron_salinas");
		
		//adminList = AdminDatabaseAccess.listAllAdministrators();
		//System.out.println(adminList.toString());
		
		System.out.println(AdminDatabaseAccess.retrieveUserPassword("aaron_salinas"));
		
		System.out.println(AdminDatabaseAccess.changePassword("aaron_salinas", "password"));
		
		System.out.println(AdminDatabaseAccess.retrieveUserPassword("aaron_salinas"));
	}
	
	static void testSensorDatabaseAccess(){
		//System.out.println(SensorDatabaseAccess.toListAllInstruments().toString());
		
		//System.out.println(SensorDatabaseAccess.addInstrument("Instrument 2"));
		
		//System.out.println(SensorDatabaseAccess.toListAllInstruments().toString());
		
		//SensorDatabaseAccess.addInstrumentSerial("Instrument 1", "1A");
		
		//SensorDatabaseAccess.addInstrumentSerial("Instrument 2", "2A");
		
		//System.out.println(SensorDatabaseAccess.removeInstrument("Instrument 1"));

		System.out.println(SensorDatabaseAccess.toListAllInstruments().toString());
		
		System.out.println(SensorDatabaseAccess.toListAllInstrumentSerials().toString());
		
	}
	
	static void testSensorDataProcessing(){
		SensorDataProcessing dataProcess = new SensorDataProcessing();
		String fileName = "Y1_M1_ADCP_01.asc";
		
		//System.out.println(dataProcess.toListFileDataReadings(fileName).toString());
		
		//System.out.println(dataProcess.toListFileDataAttributes(fileName).toString());
		
		System.out.println(dataProcess.toListADCPCurrentData(fileName).toString());
		
	}
	
}
