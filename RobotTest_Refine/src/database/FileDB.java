package database;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

import config.Constants;
import config.DB_Constants;
import appium.AppiumConfiguration;

public class FileDB {

 public static Connection conn = null;
 
 private static PreparedStatement pstmt =null;
 
 public static String fileDefPath=null;
 public static String fileDataPath=null;
 
 public static String fileDefFullPath=null;
 public static String fileDataFullPath=null;
 
 public static String metadataFileName=null;
 public static String testdataFileName=null;
 
 public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {

	//Connection to database
	dBconnection();
    excelDownload();
}
 
 protected static void dBconnection() throws SQLException, ClassNotFoundException{
	
	Class.forName(DB_Constants.JDBC_DRIVER);
    System.out.println("Connecting to a selected database...");
    
    conn = DriverManager.getConnection(DB_Constants.DB_URL,DB_Constants.USER,DB_Constants.PASS);
    
    System.out.println("Connected to the database successfully..."); 
 }
 
 
 private static void excelDownload(){
	 
	 try{

		System.out.println("Downloading the excel files from the database...\n");
	    //Creating the object for statement for sending the sql statements to the dB
		//stmt = conn.createStatement();
	    
	    String sql = "SELECT * FROM testdetails WHERE status='New' ORDER BY testID ASC";
	    
	    //Creating the object for prepared statement for sending the parameterized sql statements to the dB
		pstmt = conn.prepareStatement(sql);
		ResultSet result = pstmt.executeQuery();
		
		while(result.next())
		{
		
			DB_Constants.testID = result.getInt("testID");
			System.out.println("******Current running test case ID is : "+DB_Constants.testID);
			
			Blob metablob = result.getBlob("metadata");
			Blob testdatablob = result.getBlob("testcasendata");
			
			InputStream metaInStream = metablob.getBinaryStream();
			InputStream testInStream = testdatablob.getBinaryStream();
	
			File dir = new File("C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID);
			dir.mkdirs();
			//Validate .xls or .xlsx (Need to work on)
			metadataFileName="Metadata"+DB_Constants.testID+".xls";
			testdataFileName="Testdata"+DB_Constants.testID+".xls";
			
			fileDefPath="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID+"\\";
			fileDataPath="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID+"\\";
			
			 fileDefFullPath = fileDefPath+metadataFileName;
			 fileDataFullPath= fileDataPath+testdataFileName;
			
			OutputStream metaOutStream = new FileOutputStream(fileDefFullPath);
			OutputStream testOutStream = new FileOutputStream(fileDataFullPath);
			
			int bytesRead = -1;
			byte[] metaBuffer = new byte[DB_Constants.META_BUFFER_SIZE];
			byte[] testBuffer = new byte[DB_Constants.TEST_BUFFER_SIZE];
			
			//Reads the Metadata file until it is having the data
			while ((bytesRead = metaInStream.read(metaBuffer)) != -1) {
				//Write the metadata file
				metaOutStream.write(metaBuffer, 0, bytesRead);
			}
			//Reads the Testcase data file until it is having the data
			while ((bytesRead = testInStream.read(testBuffer)) != -1) {
				testOutStream.write(testBuffer, 0, bytesRead);	
			}
			
			metaInStream.close();
			testInStream.close();
		
			metaOutStream.close();
			testOutStream.close();
			
			System.out.println("The metadata and testdata files are downloaded!");	
			Thread.sleep(5000);
			
			System.out.println("Appium server is starting");
			AppiumConfiguration.appiumstartup(metadataFileName,testdataFileName,fileDefPath,fileDataPath);
			excelUpload(DB_Constants.testID);
			}  
		}
		 catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		 }
		 catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		 }
		 finally{
		    //finally block used to close resources
		    try
		    {
		       if(conn!=null)
		          conn.close();
		    }
		    catch(SQLException se){
		       se.printStackTrace();
		    }
		 }
	 	System.out.println("\nConnection Closed!");
	 }
 

 
 /*This method is used to Upload the table records in database**/
private static void excelUpload(int testID) throws SQLException{
		
	try{
		System.out.println("Uploading the completed excel file into database\n");
		
		//Upload the file and change the status
 		String testcasedata="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+testID+"\\"+testdataFileName;
 		String update ="UPDATE testdetails SET testresults=?,status=? WHERE testID="+testID;
 		
 		pstmt = conn.prepareStatement(update);
 		InputStream testcaseStream = new FileInputStream(new File(testcasedata));
 		
 		pstmt.setBlob(1,testcaseStream);
 		pstmt.setString(2,"Completed");
 		pstmt.executeUpdate();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}




