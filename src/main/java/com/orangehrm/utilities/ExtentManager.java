package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	
	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test=new ThreadLocal<>();
	private static Map<Long,WebDriver> driverMap =new HashMap<>();
	
	//Initialize the Extent Report 
	
	public synchronized static ExtentReports getReporter() {
		if(extent==null) {
			String reportPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReports/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("Orange HRM Report");
			spark.config().setTheme(Theme.DARK);
			
			extent= new ExtentReports();
			extent.attachReporter(spark);
			//Adding system information 
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}
	
	//Start the Test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest=getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}
	
	//End the Test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	//Get current threads test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}
	
	//Method to get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest=getTest();
		if(currentTest != null) {
			return currentTest.getModel().getName();
		}
		else {
			return "No Test is currently active for this thresd";
		}
	}
	
	//log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}
	
	//log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage,String screenShotMessage) {
		getTest().pass(logMessage);
		//screenshot method
		attachScreenshot(driver,screenShotMessage);
	}
	
	//log a step validation for API
		public static void logStepValidationForAPI(String logMessage) {
			getTest().pass(logMessage);
			
		}
	
	//log a failure
	public static void logFailure(WebDriver driver, String logMessage,String screenShotMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		//screenshot method
		attachScreenshot(driver,screenShotMessage);
	}
	
	//log a failure for API 
		public static void logFailureAPI(String logMessage) {
			String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
			
		}
	
	//log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
		
	}
	
	//take screenshot wit date and time in a file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName)  {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src= ts.getScreenshotAs(OutputType.FILE);
		//format date and time for filename
		
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		
		//Saving the screenshot to a file
		String desPath = System.getProperty("user.dir")+"\\src\\test\\resources\\screenshots\\"+screenshotName+"_"+timeStamp+".png";
		
		File finalPath =new File(desPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//convert screenshot to BASE_64 for embedding in the report
		String base64Format=convertToBase64(src);
		return base64Format;
	}
	
	//convert screenshot to Base64 format
	public static String convertToBase64(File screenShotFile) {
		String base64Format="";
		//read the file content into byteArray
		//byte[] fileContent;
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//convert the byte array to base64 string
		
		return base64Format;
		
	}
	
	//Attach screenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver,String message) {
		try {
			String screenShotBase64=takeScreenshot(driver,getTestName());
			getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot : "+e.getMessage());
		}
		
	}
	
	
	//Register WebDriver for Current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(),driver);
	}

}
