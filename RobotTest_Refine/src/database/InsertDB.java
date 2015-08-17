package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertDB {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		FileDB.dBconnection();
		// TODO Auto-generated method stub
		try{
			
			for(int i=51;i<=52;i++){
				
				String insert= "INSERT INTO testdetails (testID,metadata,testcasendata,status) VALUES (?,?,?,?)";
				PreparedStatement statement = FileDB.conn.prepareStatement(insert);
				
				String metadata="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\src\\dataEngine\\AutoInsurance_Metadata.xls";
				String testdata="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\src\\dataEngine\\AutoInusrance_Testcase_Testdata.xls";
				//String testdata="https://s3-ap-southeast-1.amazonaws.com/exotelrecordings/htcindia/abd0b236b23da81da414ab46cfaffbcc.mp3 ";
				statement.setInt(1,i);
				InputStream metadataStream = new FileInputStream(new File(metadata));
				InputStream testcaseStream = new FileInputStream(new File(testdata));
				
				statement.setBlob(2,metadataStream);
				statement.setBlob(3,testcaseStream);
				
				statement.setString(4,"New");
				statement.executeUpdate();
				
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			finally{
				try
			    {
			       if(FileDB.conn!=null)
			          FileDB.conn.close();
			    }
			    catch(SQLException se){
			       se.printStackTrace();
			    }
			}
		}
}
