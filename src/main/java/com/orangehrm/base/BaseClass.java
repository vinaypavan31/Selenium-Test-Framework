package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	protected static ThreadLocal<SoftAssert> softAssert= ThreadLocal.withInitial(SoftAssert :: new );
	
	//getter method for softAssert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		logger.info("WedDriver initiliazed and Browser maximized");
		logger.trace("trace message");
		logger.error("errror message");
		logger.debug("debug messaage");
		logger.fatal("fatal message");
		logger.warn("warn message");
		// initialize the actinDriver only once
//		if(actionDriver==null) {
//		actionDriver=new ActionDriver(driver);
//		logger.info("ActionDriver Instance is created."+Thread.currentThread().getId());

		// initialize actionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("Action Driver is initialized for thread : " + Thread.currentThread().getId());

	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the Configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
		
		//start the extentreport 
		//ExtentManager.getReporter(); -- this has been implemented in testlistener

	}

	// Initialize the WebDriver based on browser defined in config.properties file
	private synchronized void launchBrowser(String browser) {
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL=prop.getProperty("gridURL");
		//String browser = prop.getProperty("browser");
		
		if(seleniumGrid) {
			try {
				if(browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options=new ChromeOptions();
					options.addArguments("--headless","--disable-gpu","--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL),options));
				}
				else if(browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options=new FirefoxOptions();
					options.addArguments("--headless","--disable-gpu","--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL),options));
				}
				else if (browser.equalsIgnoreCase("edge")) {
				    EdgeOptions options = new EdgeOptions();
				    options.addArguments("--headless=new", "--disable-gpu","--no-sandbox","--disable-dev-shm-usage");
				    driver.set(new RemoteWebDriver(new URL(gridURL), options));
				} else {
				    throw new IllegalArgumentException("Browser Not Supported: " + browser);
				}
				logger.info("RemoteWebDriver instance created for Grid in headless mode");
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Grid URL", e);
			}
		}
		else {

		if (browser.equalsIgnoreCase("chrome")) {
			
			//Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");//Run chrome in headless mode
//			options.addArguments("--disable-gpu");//Disable GPU for headless mode
//			//options.addArguments("--window-size=1920,1080");//set window size
//			options.addArguments("--disable-notifications");//disable browser notifications
//			options.addArguments("--no-sandbox");//Required for some CI environments 
//			options.addArguments("--disable -dev-shm-usage");//resolve issues in resource
			
			
			
			
			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(options));//new changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			//Create ChromeOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");//Run firefox in headless mode
//			options.addArguments("--disable-gpu");//Disable GPU for headless mode
//			//options.addArguments("--window-size=1920,1080");//set window size
//			options.addArguments("--disable-notifications");//disable browser notifications
//			options.addArguments("--no-sandbox");//Required for some CI environments 
//			options.addArguments("--disable -dev-shm-usage");//resolve issues in resource
			
			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created");

		} else if (browser.equalsIgnoreCase("edge")) {
			//Create ChromeOptions
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless");//Run edge in headless mode
//			options.addArguments("--disable-gpu");//Disable GPU for headless mode
//			//options.addArguments("--window-size=1920,1080");//set window size
//			options.addArguments("--disable-notifications");//disable browser notifications
//			options.addArguments("--no-sandbox");//Required for some CI environments 
//			options.addArguments("--disable -dev-shm-usage");//resolve issues in resource
			
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created");
		}

		else {
			throw new IllegalArgumentException("Browser Not Supported" + browser);
		}}
	}

	// Configure browser settings - implicit wait, maximize, navigate to url
	private  void configureBrowser() {
		// Implicit Wait- Global Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser

		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL - " + e.getMessage());
		}
	}

	// quit tests-> close=closes active tab, quit-closes the entire session
	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver - " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
		//ExtentManager.endTest();-- this has been implemented in Testlistener
	}

	// getter method for prop
	public static Properties getProp() {
		return prop;
	}

//	//Driver getter method
//	public WebDriver getDriver() {
//		return driver;
//	}
//	//Driver setter method
//	public void setDriver(WebDriver driver) {
//		this.driver=driver;
//	}

	// getter method for webdriver

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("webdriver is not initialized");
		}
		return driver.get();

	}
	//driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver=driver;
	}

	// getter method for actiondriver

	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("Actiondriver is not initialized");
		}
		return actionDriver.get();

	}

	// static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
