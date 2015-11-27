/**
 * 
 */
package appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/*import org.apache.bcel.classfile.Constant;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.commons.exec.ExecuteException;*/
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


import com.github.genium_framework.appium.support.server.AppiumServer;
import com.github.genium_framework.server.ServerArguments;

import config.Constants;
import executionEngine.DriverScript;
import utility.ExcelUtils;

/**
 * @author ashokkumarg
 *
 */
public class AppiumConfiguration {

	public static WebDriver driver;

	static int testID = 0;

	public static void appiumstartup(String metadataFileName,
			String testdataFileName, String fileDefPath, String fileDataPath)
			throws IOException, Exception {

		
		 File file = new File("D:/programfiles");// Appium path
		 
		 ServerArguments serverArguments = new ServerArguments();
		 
		 serverArguments.setArgument("--address","127.0.0.1");
		 serverArguments.setArgument("--chromedriver-port", 9516);
		 serverArguments.setArgument("--port", 4725);
		 serverArguments.setArgument("--no-reset", true);
		 serverArguments.setArgument("--local-timezone", true);
		 
		 AppiumServer appiumServer = new AppiumServer(file, serverArguments);
		 appiumServer.stopServer(); 
		 Thread.sleep(5000);
		 appiumServer.startServer();
		 Thread.sleep(5000);
		 
		 System.out.println("Appium settings completed and ready to launch!");
		 String platform = ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 1, 8);
		 Thread.sleep(3000);
		
		// Desired capabilities setup for Android
		if (platform.equals("Android")) {
			System.out
					.println("Android Desired Capabilities Setup is loading...");
			DesiredCapabilities androidCapabilities = new DesiredCapabilities();
			androidCapabilities.setCapability("avd", "AutoInsurance");
			
			androidCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 7, 2));
			androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 8, 2));
			androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 9, 2));
			androidCapabilities.setCapability(MobileCapabilityType.APP,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 10, 2));
			
			/* androidCapabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT,"180");
			  androidCapabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, "300000");*/
			 
			driver = new AndroidDriver(new URL(ExcelUtils.readExcel(
					fileDefPath, metadataFileName, Constants.executionSheet,11, 2)), androidCapabilities);
			System.out.println("Appium SetUp for Android is successful and Appium Driver is launched successfully");
			Thread.sleep(5000);
			DriverScript.testExecutor(fileDefPath, metadataFileName,fileDataPath, testdataFileName, (AndroidDriver) driver);
		}
		// Desired capabilities setup for ios
		else {

			DesiredCapabilities iosCapabilities = new DesiredCapabilities();
			iosCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 7, 3));
			iosCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 8, 3));
			iosCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 9, 3));
			iosCapabilities.setCapability(MobileCapabilityType.APP,
					ExcelUtils.readExcel(fileDefPath, metadataFileName,Constants.executionSheet, 10, 3));
			driver = new IOSDriver(new URL(ExcelUtils.readExcel
					(fileDefPath,metadataFileName, Constants.executionSheet, 11, 3)),iosCapabilities);

			System.out.println("Appium SetUp for iOS is successful and Appium Driver is launched successfully");
			Thread.sleep(15000);
			DriverScript.testExecutor(fileDefPath, metadataFileName,fileDataPath, testdataFileName, (IOSDriver) driver);

		}
	}
}
