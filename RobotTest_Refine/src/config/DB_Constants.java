package config;

/***The DB_Constant class contains the variables which is used for database configuration**/

public class DB_Constants {
	
	 // JDBC driver name and database URL
	 public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 public static final String DB_URL = "jdbc:mysql://172.16.10.249/testing";

	 //  Database credentials
	 public static final String USER = "ashokkumarg";
	 public static final String PASS = "123Welcome";
	 
	 public static final int META_BUFFER_SIZE = 4096;
	 public static final int TEST_BUFFER_SIZE = 4096;
	 
	 public static int testID=0;
}
