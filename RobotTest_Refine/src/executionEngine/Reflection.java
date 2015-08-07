package executionEngine;

import io.appium.java_client.android.AndroidDriver;

import java.lang.reflect.Method;

import config.ActionKeywords;
import config.Constants;

public class Reflection {

	public static ActionKeywords actionKeywords;
	public static Method arrayMethod[];
	public static Method method;

public static void execute_Actions(String fieldID,String data, String action, String xpath, AndroidDriver driver) throws Exception {
	
	
	actionKeywords = new ActionKeywords();
	//This will load all the methods of the class 'ActionKeywords' in it.
    //It will be like array of method, use the break point here and do the watch
	arrayMethod= actionKeywords.getClass().getMethods();
	/*This is a loop which will run for the number of actions in the Action Keyword class 
	method variable contain all the method and method.length returns the total number of methods*/
	
	for(int i = 0;i < arrayMethod.length;i++){
		//This is now comparing the method name with the ActionKeyword value got from excel
		if(arrayMethod[i].getName().equals(action)){
			System.out.println(arrayMethod[i].getName());
			
			//In case of match found, it will execute the matched method6
			//arrayMethod[i].invoke(actionKeywords,fieldID,Constants.testData,Constants.action,xpath,driver);
			//no paramater
			//Class noparams[] = {};
				
			//String parameter
			Class[] paramString = new Class[1];	
			paramString[0] = String.class;
						
	        //load the AppTest at runtime
			Class<?> cls = Class.forName("config.ActionKeywords");
			Object obj = cls.newInstance();
				
			//call the printIt method
			/*Method method = cls.getDeclaredMethod("printIt", noparams);
			method.invoke(obj, null);*/
				
			//call the printItString method, pass a String param 
			method = cls.getDeclaredMethod("enterText", paramString);
			method.invoke(obj, new String(fieldID),new String(data), new String(action),new String(xpath),driver);
				
				//Once any method is executed, this break statement will take the flow outside of for loop
			break;
			}
		}
	}

}
