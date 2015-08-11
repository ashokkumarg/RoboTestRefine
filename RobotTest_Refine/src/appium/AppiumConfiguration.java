/**
 * 
 */
package appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import config.Constants;
import database.FileDB;
import executionEngine.DriverScript;
import utility.ExcelUtils;

/**
 * @author ashokkumarg
 *
 */
public class AppiumConfiguration {

	public static WebDriver driver;	
	static int testID=0;
	
	public static void appiumstartup(String metadataFileName,String testdataFileName,String fileDefPath,String fileDataPath) throws IOException, Exception{

		CommandLine command = new CommandLine("cmd");
	    command.addArgument("/c");
	    command.addArgument(Constants.appiumNode);
	    command.addArgument(Constants.appiumPath);
	    command.addArgument("--address", true);  
	    command.addArgument("127.0.0.1");  
	    command.addArgument("--port", false);  
	    command.addArgument("4725");
	    command.addArgument("--full-reset", true);
	    //command.addArgument("--log");
	    //command.addArgument("D:\\AppiumConsole.txt");

	    DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();  
	    DefaultExecutor executor = new DefaultExecutor();  
	    executor.setExitValue(1);
	    executor.execute(command, resultHandler);
	    
	    Thread.sleep(15000);
	    
	    System.out.println("Appium settings completed and ready to launch!");
	    String platform= ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,1,8); 
	    
		//Desired capabilities setup for Android
		if(platform.equals("Android")){
			System.out.println("Android Desired Capabilities Setup is loading...");
			DesiredCapabilities androidCapabilities = new DesiredCapabilities();
			androidCapabilities.setCapability("avd","AutoInsurance");
			androidCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,7,2));	
			androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,8,2));
			androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,9,2));
			androidCapabilities.setCapability(MobileCapabilityType.APP,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,10,2));
			driver = new AndroidDriver(new URL(ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,11,2)), androidCapabilities);	
			System.out.println("Appium SetUp for Android is successful and Appium Driver is launched successfully");
			Thread.sleep(30000);
			DriverScript.testExecutor(fileDefPath, metadataFileName,fileDataPath,testdataFileName,(AndroidDriver)driver);
		}
		
		//Desired capabilities setup for ios
		else{
			
			DesiredCapabilities iosCapabilities = new DesiredCapabilities();
			iosCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,7,3));	
			iosCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,8,3));
			iosCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,9,3));
			iosCapabilities.setCapability(MobileCapabilityType.APP,ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,10,3));
			driver = new IOSDriver(new URL(ExcelUtils.readExcel(fileDefPath,metadataFileName,Constants.executionSheet,11,3)), iosCapabilities);
			
			System.out.println("Appium SetUp for iOS is successful and Appium Driver is launched successfully");
			Thread.sleep(30000);
			//DriverScript.testExecutor(testID, fileDefPath, FileDB.fileDataPath,driver);
		}
	}
	
	
public static  void stopAppiumServer() throws IOException, Exception { 
		
	    CommandLine command = new CommandLine("cmd");  
	    command.addArgument("/c");  
	    command.addArgument(Constants.taskkill);  
	    DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();  
	    DefaultExecutor executor = new DefaultExecutor();  
	    executor.setExitValue(1);  
	    executor.execute(command, resultHandler);
	    Thread.sleep(10000);
	    
	}
}
