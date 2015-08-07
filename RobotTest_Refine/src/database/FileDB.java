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

 private static Connection conn = null;
 private static Statement stmt = null;
 private static PreparedStatement pstmt =null;
 
 public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {

	Class.forName(DB_Constants.JDBC_DRIVER);
    System.out.println("Connecting to a selected database...");
    conn = DriverManager.getConnection(DB_Constants.DB_URL,DB_Constants.USER,DB_Constants.PASS);
    System.out.println("Connected to the database successfully...");
    //insert();
    download();
}
 
 public static void download(){
	 
	 try{

		System.out.println("Downloading the excel files from the database...\n");
	    stmt = conn.createStatement();
	    
	    String sql = "SELECT * FROM testdetails WHERE status='New' ORDER BY testID ASC";
	    
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
			
	
			
			File dir = new File("D:\\MyWorkspace\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID);
			dir.mkdirs();
			
			String fileDefPath = "D:\\MyWorkspace\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID+"\\AutoInsurance_Metadata_"+DB_Constants.testID+".xls";
			String fileDataPath= "D:\\MyWorkspace\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+DB_Constants.testID+"\\AutoInusrance_Testcase_Testdata_"+DB_Constants.testID+".xls";
			
			OutputStream metaOutStream = new FileOutputStream(fileDefPath);
			OutputStream testOutStream = new FileOutputStream(fileDataPath);
			
			int bytesRead = -1;
			byte[] metaBuffer = new byte[DB_Constants.META_BUFFER_SIZE];
			byte[] testBuffer = new byte[DB_Constants.TEST_BUFFER_SIZE];
			
			//Metadata
			while ((bytesRead = metaInStream.read(metaBuffer)) != -1) {
				metaOutStream.write(metaBuffer, 0, bytesRead);
			}
			//Testcase data
			while ((bytesRead = testInStream.read(testBuffer)) != -1) {
				testOutStream.write(testBuffer, 0, bytesRead);	
			}
			
			metaInStream.close();
			testInStream.close();
		
			metaOutStream.close();
			testOutStream.close();
			
			System.out.println("The metadata and testdata files are downloaded!");	
			Thread.sleep(5000);
			
			AppiumConfiguration.stopAppiumServer();
			System.out.println("Appium server is starting");
			AppiumConfiguration.appiumstartup();
			upload(DB_Constants.testID);
			AppiumConfiguration.stopAppiumServer();
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
		    try{
		       if(stmt!=null)
		          conn.close();
		    }
		    catch(SQLException se){
		    	se.printStackTrace();
		    }
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
private static void upload(int testID) throws SQLException{
		
	try{
		System.out.println("Uploading the completed excel file into database\n");
	    stmt = conn.createStatement();
	    
	    //Upload the file and change the status
	    
 		String testcasedata="D:\\MyWorkspace\\RobotTest_Refine\\DB_downloadfiles\\ExecutableFiles_"+testID+"\\AutoInusrance_Testcase_Testdata_"+DB_Constants.testID+".xls";
 		
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



private static void insert() throws SQLException, FileNotFoundException {
	// TODO Auto-generated method stub
	
	stmt=conn.createStatement();
	for(int i=21;i<=25;i++){
		
		String insert= "INSERT INTO testdetails (testID,metadata,testcasendata,status) VALUES (?,?,?,?)";
		PreparedStatement statement = conn.prepareStatement(insert);
		
		statement.setInt(1,i);
		InputStream metadataStream = new FileInputStream(new File(Constants.metadata));
		InputStream testcaseStream = new FileInputStream(new File(Constants.testdata));
		
		statement.setBlob(2,metadataStream);
		statement.setBlob(3,testcaseStream);
		
		statement.setString(4,"New");
		statement.executeUpdate();
		
		System.out.println("Record inserted successfully");
		}
	}
 }




