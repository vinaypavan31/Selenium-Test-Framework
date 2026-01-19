package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger =BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int time = Integer.parseInt(BaseClass.getProp().getProperty("ExplicitWait"));

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(time));
		logger.info("WebDriver instance is created");
	}

	// Method to click an element
	public void click(By by) {
		
		String elementDescription=getElementDescription(by);

		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element : "+elementDescription);
			logger.info("clicked an element -->"+elementDescription);
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("Unable to click element" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unaable to click element: ", elementDescription+"_unable to click");
			logger.error("unable to click element");
		}
	}

	// Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable - " + e.getMessage());
		}
	}

	// Wait for element to be visible

	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible - " + e.getMessage());
		}
	}

	// method to enter text into the input field

	public void enterText(By by, String value) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text : "+getElementDescription(by)+" "+value);
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to enter text - " + e.getMessage());
		}
	}

	// Method to get Text from input field
	public String getText(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to get the text - " + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by,"green");
				logger.info("Text are matcing : " + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text Verified Successfully! "+actualText+" equals "+expectedText);
				return true;
			} else {
				applyBorder(by,"red");
				logger.error("Text are not matcing : " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparision Failed!", "Text comparasion failed ! "+actualText+" not equals "+expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("unable to compare texts - " + e.getMessage());
		}
		return false;
	}

	// method to check if an element is displayed
//	public boolean isDisplayed(By by) {
//		try {
//			waitForElementToBeVisible(by);
//			boolean isDisplayed = driver.findElement(by).isDisplayed();
//			if (isDisplayed) {
//				System.out.println("Element is visible");
//				return isDisplayed;
//			} else {
//				return isDisplayed;
//			}
//		} catch (Exception e) {
//			System.out.println("element is not displayed - " + e.getMessage());
//			return false;
//		}
//	}
	// simplified method
	public boolean isDisplayed(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
			logger.info("Element is displayed : "+getElementDescription(by));
			ExtentManager.logStep("Element is displayed : "+getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed : ", "Element is displayed : "+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
			
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Element is not displayed = " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed : ", "Element is not displayed : "+getElementDescription(by));
			return false;
		}
	}

	// method to scroll to element
	public void scrollToElement(By by) {
		try {
			applyBorder(by,"green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to locate element - " + e.getMessage());
		}

	}

	// wait for the page to load

	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception : " + e.getMessage());
		}

	}
	
	//method to get the description of the element using By locator
	public String getElementDescription(By locator) {
		//check for null driver or locator to avoid NullPointerException
		if(driver==null) {
			return "driver is null";
		}
		if(locator==null) {
			return "locator is null";
		}
		try {
			//find element using locator
			WebElement element=driver.findElement(locator);
			//get element attributes
			String name= element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text=element.getText();
			String className=element.getDomAttribute("class");
			String placeHolder=element.getDomAttribute("placehoder");
			
			//Return the description based on element attributes
			if(isNotEmpty(name)) {
				return "Element with name : "+name;
			}
			else if (isNotEmpty(id)) {
				return "Element with id : "+id;
			}
			else if (isNotEmpty(text)) {
				return "Element with id : "+truncate(text,50);
			}
			else if (isNotEmpty(className)) {
				return "Element with id : "+className;
			}
			else if (isNotEmpty(placeHolder)) {
				return "Element with id : "+placeHolder;
			}
		} catch (Exception e) {
			logger.error("unable to describe the element -"+e.getMessage());
		}
		return "unable to describe the element";
		
		
	}
	//Utility method to check the string is not NULL or empty
	private boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();
		
	}
	
	//Utility method to truncate long string 
	private String truncate(String value, int maxLength) {
		if(value==null || value.length()<=maxLength) {
			return value;
		}
		return value.substring(0, maxLength)+"...";
	}
	
	//Utility method to border an element 
	public void applyBorder(By by,String color) {
		try {
			//Locate the element
			WebElement element= driver.findElement(by);
			//Apply the border
			String script="arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js= (JavascriptExecutor)driver;
			js.executeScript(script,element);
			logger.info("Applied the border with color "+color+" to element "+getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border toa an element : "+getElementDescription(by),e);
		}
	}
	

}
