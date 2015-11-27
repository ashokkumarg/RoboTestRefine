/**
 * 
 */
package config;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utility.ExcelUtils;

/**
 * @author ashokkumarg
 *
 */
public class ActionKeywords {

	public static WebElement element = null;
	
	public static void enterText(String ElementID,String data, String xpath,String metadataFileName, String fileDefPath, WebDriver driver) throws InterruptedException
	{
		try
		{
			explicitWait(ElementID,driver);
			//driver.findElement(By.id(ElementID)).clear();
			System.out.println("ZIPCODE CLEARED");
			driver.findElement(By.id(ElementID)).sendKeys(data);
			System.out.println("Into entertext");
		}
		catch(Exception e)
		{
			//driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(data);
			System.out.println("Into entertext xpath");
		}
	}
	
	//For clicking the button
	public static void clickOnButton(String ElementID, String data,String xpath,String metadataFileName, String fileDefPath, WebDriver driver)
	{
		try
		{
			explicitWait(ElementID,driver);
			driver.findElement(By.id(ElementID)).click();
		}
		catch(Exception e)
		{
			try{
				System.out.println("before click");
				driver.findElement(By.name(xpath)).click();
				System.out.println("after click");   	
			}
			catch(Exception ex){
			System.out.println("before click");
			driver.findElement(By.xpath(xpath)).click();
			System.out.println("after click");
			}
		}
	}
	//Verify the element is present or not.
	public static boolean verifyElement(String ElementID, String data,String xpath,String metadataFileName, String fileDefPath, WebDriver driver){
		
		boolean element=false;
		try{
			
			if(driver.findElements(By.id(ElementID)).size() != 0){
				WebElement E = driver.findElement(By.id(ElementID));
				E.isDisplayed();
				System.out.println("Element is verified");
				element= true;
			}
			else{
				System.out.println("Element not found on the screen");
				element=false;
			}
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return element;
	}
	
	//Get the text from the field/alert
	public static String getText(String ElementID, String data,String xpath,String metadataFileName, String fileDefPath, WebDriver driver){
		String message=null;
		try{
			
			if(driver.findElements(By.id(ElementID)).size() != 0){
				WebElement E = driver.findElement(By.id(ElementID));
				message=E.getText();
			}
			else{
				message="No element found on the screen";
			}
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return message;
	}
	
	public static WebElement btnCancel(WebDriver driver){
		element = driver.findElement(By.id("com.htcindia.autoinsurance:id/textView1"));		
		return element;
    }

	public static WebElement popOverbtnOK(WebDriver driver){
		element = driver.findElement(By.id("android:id/button1"));
	 	return element;
	}
	
	//Alert check box
	public static void alertCheck(WebDriver driver)
	{		
		try {
			popOverbtnOK(driver).click();
			System.out.println("Alert Present...Accepting and Proceeding with Execution");
			} catch (Exception e) {
				System.out.println("No Alert Present... Proceeding with Execution");
			}
	}
	
	//For clicking cancel button
	public static void cancelCheck(WebDriver driver)
	{	
		try {
			btnCancel(driver).click();
			System.out.println("Clicking cancel to go to Home page... Proceeding with Execution");
			} catch (Exception e) {
				System.out.println("Cancel button not present... Proceeding with Execution");
			}		
	}
	
	//Wait time until the element is located
	public static void explicitWait(String ID, WebDriver driver)
	{
		WebElement E = (new WebDriverWait(driver, 15))
					  .until(ExpectedConditions.presenceOfElementLocated(By.id(ID)));			
	}
	
	//Extended wait time
	public static void explicitlongWait(String ID,WebDriver driver)
	{
		WebElement E = (new WebDriverWait(driver, 400))
					  .until(ExpectedConditions.presenceOfElementLocated(By.id(ID)));			
	}
	
	//Tapping the element with coordinates
	public static void tapWithCoordinates(Double tapCount,Double x,Double y,Double touchCount,Double duration,WebDriver driver) throws InterruptedException
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		tapObject.put("tapCount",tapCount);
		tapObject.put("x",x); // in pixels from left
		tapObject.put("y",y); // in pixels from top
		tapObject.put("touchCount",touchCount);
		tapObject.put("duration",duration);
		js.executeScript("mobile: tap", tapObject);	
	}
	
	public static void calendar(String ElementID, String data,String xpath,String fileDefPath, String metaDataFileName, WebDriver driver) throws Exception{
		String calendar= ExcelUtils.readExcel(fileDefPath,metaDataFileName,Constants.executionSheet,1,8); 
		
		if(calendar.equals("Android")){
			int YEAR=Integer.parseInt(data);
			System.out.println("YEAR value is"+YEAR);
			ActionKeywords.calendarAndroid(01,03,YEAR,driver);
		}
		else if(calendar.equals("I")){
			int YEAR=Integer.parseInt(data);
			System.out.println("YEAR value is"+YEAR);
			ActionKeywords.calendarios(01,"May",YEAR,driver);
		}
	}
	
	//For selecting a calendar with a given date in Android
	public static void calendarAndroid(int date,int month,int year,WebDriver driver)
	{
		HashMap<String,Integer> pick=calendarCalculate(date,month,year);
        WebElement e;
        for(Map.Entry ele:pick.entrySet()){
            String text=ele.getKey().toString();
            int value=(Integer) ele.getValue();
            switch(text){

            case "IncreaseYr":for(int i=0;i<value;i++){
            	WebElement IncreaseYr = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Increase year']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Increase year']"));
            	e.click();
                                }
                                break;
            case "DecreaseYr":for(int i=0;i<value;i++){
            	WebElement DecreaseYr = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Decrease year']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Decrease year']"));
                                    e.click();
                                }
                                break;
            case "IncreaseMon":for(int i=0;i<value;i++){
            	WebElement IncreaseMon = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Increase month']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Increase month']"));
                                    e.click();
                                }
                                break;
            case "DecreaseMon":for(int i=0;i<value;i++){
            	WebElement DecreaseMon = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Decrease month']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Decrease month']"));
                                    e.click();
                                }
                                break;
            case "IncreaseDay":for(int i=0;i<value;i++){
            	WebElement IncreaseDay = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Increase day']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Increase day']"));
                                    e.click();
                                }
                                break;
            case "DecreaseDay":for(int i=0;i<value;i++){
            	WebElement DecreaseDay = (new WebDriverWait(driver, 100))
						  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Decrease day']")));
e=driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Decrease day']"));
                                    e.click();
                                }
                                break;
            }

        }
    }
	
	//For selecting a calendar with a given date in ios
	public static void calendarios(int date, String month, int year,WebDriver driver){
    	driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAPopover[1]/UIAPicker[1]/UIAPickerWheel[2]")).sendKeys(String.valueOf(date));
    	driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAPopover[1]/UIAPicker[1]/UIAPickerWheel[1]")).sendKeys(month);
    	driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAPopover[1]/UIAPicker[1]/UIAPickerWheel[3]")).sendKeys(String.valueOf(year));
    }
	
	//Method call in calendar for date picker
    public static HashMap<String, Integer> calendarCalculate(int date,int month,int year){
        System.out.println("Entered Calender");
        String valid="No";
        HashMap<String,Integer> calPick=new HashMap<String,Integer>();


        Calendar cal=Calendar.getInstance(TimeZone.getDefault());
        int currentYear=cal.get(Calendar.YEAR);
        System.out.println(currentYear);
        int currentMonth=cal.get(Calendar.MONTH)+1;

        System.out.println(currentMonth);
        int currentDay=cal.get(Calendar.DATE);
        System.out.println(currentDay);
        if(date>0 && date<=31){
            if(month>0 && month<=12){
                if(year>0){
                    valid="yes";
                    System.out.println("Valid data");

                }
            }
        }
        if(valid=="yes"){
            //Month
            if(currentMonth==month){
                calPick.put("IncreaseMon",0);
                calPick.put("DecreaseMon",0);
            }
            else if(currentMonth<month){
                System.out.println("Entered Month");
                calPick.put("IncreaseMon",month-currentMonth);
            }
            else{
                calPick.put("DecreaseMon",currentMonth-month);
            }
        }
        //Date
        if(currentDay==date){
        	calPick.put("IncreaseDay",0);
        	calPick.put("DecreaseDay",0);
        }
        else if(currentDay<date){
            System.out.println("Entered Date");
            calPick.put("IncreaseDay",date-currentDay);
            calPick.put("DecreaseDay",0);
        }
        else{
            calPick.put("IncreaseDay",0);
            calPick.put("DecreaseDay",currentDay-date);
        }
        //Year
        if(currentYear==year){
            calPick.put("IncreaseYr",0);
            calPick.put("DecreaseYr",0);
        }
        else if(year<currentYear){
            System.out.println("Entered Year");
            calPick.put("IncreaseYr",0);
            calPick.put("DecreaseYr",currentYear-year);
        }
        else{
            calPick.put("IncreaseYr",year-currentYear);
            calPick.put("DecreaseYr",0);
        }
        return calPick;
    }	
    
}
