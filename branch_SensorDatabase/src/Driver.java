import java.sql.*;

/**
 * This class is a test driver for the program
 * @author aaronsalinas
 */
public class Driver implements DatabaseInformation{
	public static void main(String [] args) {
		Database DB = null;
		
		//Database.showTables();
		System.out.println(Database.checkIfAdminExists("Kelsey_Caraballo"));
		
	}
}
