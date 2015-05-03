public interface DatabaseInformation {
	
	/* Database Connection Credentials */
	final static String DBPATH = "jdbc:mysql://localhost:3306/Sensor Database"; //Path to database
	final static String DBUSER = "root";			// Name of the user
	final static String DBPASSWORD = "password";	// Password of the user
	final static String DBNAME = "Sensor Database";	// Name of the database
	
	/* Default Failure Messages */
	static final String DBCONN_ERROR = "Error in Connection to Database!";
	static final String DBSTATEMENT_ERROR = "Error in Statement!";
	static final String DBQUERY_ERROR = "Error in Query to Database!";
	
}
