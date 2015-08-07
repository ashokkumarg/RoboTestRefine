package executionEngine;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import appium.AppiumConfiguration;
import config.ActionKeywords;
import config.Constants;
import config.DB_Constants;
import utility.ExcelUtils;


public class DriverScript{

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		AppiumConfiguration.stopAppiumServer();
		System.out.println("Appium server is starting");
		AppiumConfiguration.appiumstartup();
	}
	
	public static void testExecutor(int testID,String fileDefPath,String metaFileName,String fileDataPath, String testFileName,AndroidDriver driver ) throws InterruptedException, Exception{
		System.out.println("Test case is started Running");
		Logger log = Logger.getLogger("devpinoyLogger");
		log.info("Test case is started Running");
		WebDriver screenDriver = new Augmenter().augment(driver);
	   
		String[] screenNames=null;
	    
		int colsNum=0;
		String executionIndicator=null;
		String type=null;
		String fieldName=null;
		String fieldId=null;
		String xpath=null;
		String variable=null;
		String vars=null;
		
		System.out.println("LOG");
		Constants.numberOfTestCase=ExcelUtils.getNumberOfRows(fileDataPath,testFileName,Constants.testSheetName);
		System.out.println(Constants.numberOfTestCase);
		Constants.numberOfTestCaseCols=ExcelUtils.getNumberOfCols(fileDataPath,testFileName,Constants.testSheetName);
		System.out.println(Constants.numberOfTestCaseCols);
		for(int i=1;i<=Constants.numberOfTestCase;i++){
			log.debug("entering TestCase");
			executionIndicator=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.testSheetName, i,0);
			log.info("Execution Indicator for "+" test case "+ i + " is " + executionIndicator);
		
			if(executionIndicator.equals("Y")){
					try{
						System.out.println("Test Case number"+i);
						Constants.testCaseId=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.testSheetName, i,1);
						Constants.testCaseFlow=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.testSheetName, i,2);
						log.info("Test Case ID and Test case Flow are "+ Constants.testCaseId + " and " + Constants.testCaseFlow);
					    screenNames=Constants.testCaseFlow.split("\\,");
			
						for(int j=0;j<screenNames.length;j++){
							System.out.println("Screen number"+j+1);
							Constants.screenName=screenNames[j];
							System.out.println(Constants.screenName);
						
							if(Constants.screenName.equals("Completed")){
								ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,"Test Case Completed Successfully",i,4);
								ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,screenNames[j-1].toString()+"Data",i,7);
								File file  = ((TakesScreenshot)screenDriver).getScreenshotAs(OutputType.FILE);
								File dir = new File("D:\\MyWorkspace\\RobotTest_Refine\\Completed_rec_screenshot\\ScreenshotFiles_"+DB_Constants.testID);
								dir.mkdirs();
								String fileName= "D:\\MyWorkspace\\RobotTest_Refine\\Completed_rec_screenshot\\ScreenshotFiles_"+DB_Constants.testID+"\\"+Constants.testCaseId+".jpg";
								FileUtils.copyFile(file, new File(fileName));
							}
							else if(Constants.screenName.equals("Restart")){
								ActionKeywords.alertCheck(driver);
								ActionKeywords.alertCheck(driver);
								ActionKeywords.cancelCheck(driver);	
							}
							else{
								int rowsNum=ExcelUtils.getNumberOfRows(fileDataPath,testFileName,Constants.screenName+"Data");
								System.out.println(rowsNum);
								int defRowsNum=ExcelUtils.getNumberOfRows(fileDefPath,metaFileName,Constants.screenName+"Def");					
							
								for(int k=1;k<=rowsNum;k++){
									System.out.println("Row number "+k+" in "+Constants.screenName+"Data");
									variable=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.screenName+"Data",k,0);
									System.out.println(variable);
									System.out.println(Constants.testCaseId);
								
									if(variable.equalsIgnoreCase(Constants.testCaseId)){
										System.out.println(variable+" and "+Constants.testCaseId+" matches");
										colsNum=ExcelUtils.getNumberOfCols(fileDataPath,testFileName,Constants.screenName+"Data");
									
										for(int l=1;l<colsNum;l++){
											System.out.println("Column number "+l+"in "+Constants.screenName+"Data");
											Constants.testData=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.screenName+"Data", k,l);
											fieldName=ExcelUtils.readExcel(fileDataPath,testFileName,Constants.screenName+"Data", 0, l);
											System.out.println(Constants.testData+" and "+fieldName);
										
											if(Constants.testData.equals("X")){
												System.out.println("i switch........");
											}
											else{
												System.out.println(Constants.testData+" is valid execution "+fieldName);
											
												for(int m=1;m<=defRowsNum;m++){
													vars=ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.screenName+"Def", m,0);
												
													if(fieldName.equalsIgnoreCase(vars)){
														fieldId=ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.screenName+"Def",m,2);
														Constants.action=ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.screenName+"Def", m, 1);
														xpath=ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.screenName+"Def",m,3);
														type=ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.screenName+"Def", m, 1);
														switch(type){
														case "enterText":
															ActionKeywords.enterText(fieldId,Constants.testData,xpath,driver);
															break;
															
														case "clickOnButton":
															ActionKeywords.clickOnButton(fieldId,xpath,driver);
															break;
															
														case "calendar":
															String calendar= ExcelUtils.readExcel(fileDefPath,metaFileName,Constants.executionSheet,1,8); 
															ActionKeywords.calendar(driver);
															break;
														}
													}
												else{
														System.out.println("The field name is not matched in File Def path and File Data Path");
													}
												}
											}
										}
									}
								}
							}							
						}
					}
					catch (Exception e)
					{
						try 
						{
							log.warn("Entered in to first catch block");
							System.out.println("Entered in to catch one");
							File file  = ((TakesScreenshot)screenDriver).getScreenshotAs(OutputType.FILE);
							File dir = new File("D:\\MyWorkspace\\RobotTest_Refine\\Failure_rec_screenshot\\ScreenshotFiles_"+DB_Constants.testID);
							dir.mkdirs();
							
							String fileName= "D:\\MyWorkspace\\RobotTest_Refine\\Failure_rec_screenshot\\ScreenshotFiles_"+DB_Constants.testID+"\\"+Constants.testCaseId+".jpg";
							FileUtils.copyFile(file, new File(fileName));
							WebElement E = driver.findElement(By.id("android:id/message"));			
							ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,E.getText(),i,4);
							ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,Constants.screenName+"Data",i,7);
						} catch (Exception e1) {
							log.warn("Entered in to Second Catch Block");
							ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,e1.getMessage(),i,4);
							ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,Constants.screenName+"Data",i,7);
						}
					}
					String ActResult= ExcelUtils.readExcel(fileDataPath,testFileName,Constants.testSheetName,i,4);
					String ExpResult= ExcelUtils.readExcel(fileDataPath,testFileName,Constants.testSheetName,i,3);
					if(ActResult.contains(ExpResult))
					{
						log.info("Test case "+ i + " is passed and writing in the result");
						ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,"Passed",i,5);
						
					}
					else
					{	
						log.info("Test case "+ i + " is failed and writing in the result");
						ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,"Failed",i,5);
					}
					ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,"Run",i,6);
				}
				else
				{
					ExcelUtils.writeExcel(fileDataPath,testFileName,Constants.testSheetName,"SKIPPED",i,6);	
				}
			}
	}	
}
