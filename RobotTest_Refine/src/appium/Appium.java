package appium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;

import com.github.genium_framework.appium.support.server.AppiumServer;
import com.github.genium_framework.server.ServerArguments;

import config.Constants;

public class Appium {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		File file = new File("D:/programfiles");
		
		ServerArguments serverArguments = new ServerArguments();
		
		serverArguments.setArgument("--address","127.0.0.1");
		serverArguments.setArgument("--chromedriver-port", 9516);
		serverArguments.setArgument("--port", 4725);
		serverArguments.setArgument("--no-reset", true);
		serverArguments.setArgument("--local-timezone", true);
		
		AppiumServer appiumServer = new AppiumServer(file, serverArguments);
		appiumServer.stopServer();
		System.out.println("Stopped");
		Thread.sleep(5000);
		appiumServer.startServer();
		System.out.println("started");
	}

	/*String deviceName = "emulator-5554";
	System.out.println("SetUp is started");
	String cmd = "C:\\Program Files\\Genymobile\\Genymotion\\player --vm-name \""+deviceName+"\"";
	Runtime rt = Runtime.getRuntime();
	Process p = rt.exec(cmd);*/
	
	/*		CommandLine command = new CommandLine("cmd");
	command.addArgument("/c");
	command.addArgument(Constants.appiumNode);
	command.addArgument(Constants.appiumPath);
	command.addArgument("--address", true);
	command.addArgument("127.0.0.1");
	command.addArgument("--port", false);
	command.addArgument("4725");
	command.addArgument("--full-reset", true);
	command.addArgument("--device-ready-timeout");
	command.addArgument("180");
	// command.addArgument("--log");
	// command.addArgument("D:\\AppiumConsole.txt");

	DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
	DefaultExecutor executor = new DefaultExecutor();
	executor.setExitValue(1);
	executor.execute(command, resultHandler);
*/
	
	
	
	
	/*public static void stopAppiumServer() throws IOException, Exception {

		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument(Constants.taskkill);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.execute(command, resultHandler);
		Thread.sleep(5000);

	}*/
}
