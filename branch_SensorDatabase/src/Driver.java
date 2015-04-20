import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a test driver for the program
 * @author aaronsalinas
 */
public class Driver implements DatabaseInformation{
	public static void main(String [] args) {
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
}
