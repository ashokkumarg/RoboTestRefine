package config;

/***Constant class contains the variables which is used throughout the project**/

public class Constants {

	
	public static final String fileDefPath="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\src\\dataEngine\\";
	public static final String fileDataPath="C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\src\\dataEngine\\";
	/*public static final String fileDefPath="D:\\MyWorkspace\\RobotTest_Refine\\src\\dataEngine\\";
	public static final String fileDataPath="D:\\MyWorkspace\\RobotTest_Refine\\src\\dataEngine\\";*/
	public static final String executionSheet="Execution_Config";
	public static final String testSheetName="TestCase";
	public static final String metadataFileName="AutoInsurance_Metadata.xls";
	public static final String testdataFileName="AutoInusrance_Testcase_Testdata.xls";
	
	public static final String metadata = fileDefPath+metadataFileName;
	public static final String testdata = fileDataPath+testdataFileName;
	
	public static String testCaseId;
	public static String testCaseFlow;
	public static String screenName;
	public static String action;
	
	public static final String appiumNode="D:\\programfiles\\node.exe";
	public static final String appiumPath="D:\\programfiles\\node_modules\\appium\\bin\\appium.js";
	public static final String taskkill = "Taskkill /F /IM node.exe";
	
	public static int numberOfTestCaseCols;
	public static int numberOfTestCase;
	public static String testData;
	
	/*******************************Android_iOS_Comparison***********************************/
	public static String mismatchedPath= "C:\\Users\\ashokkumarg\\Documents\\GitHub\\RobotTest_Refine\\RobotTest_Refine\\src\\dataEngine\\comparison_output\\";
    
	public static String androidFileName="DemoTestcaseAndData_Android.xls";
	public static String iosFileName="DemoTestcaseAndData_iOS.xls";
	public static String mismatchFileName="MisMatchedResults.xls";
	
	public static String mismatchFilePath=mismatchedPath+mismatchFileName;
	public static String androidFilePath=mismatchedPath+androidFileName;
	public static String iosFilePath=mismatchedPath+iosFileName;
	
	
	public static String androidSheetName="TestCase";
	public static String iosSheetName="TestCase";
	public static String mismatchSheetName="mismatchSheet";
	
	public static int rowToWrite=1;
	
}
