package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	//Define locators using By class
	
	private By userNameFeild=By.name("username");
	private By passwordFeild= By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[normalize-space()='Login']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	
	// Initialize the Actiondriver object by passing Webdriver instance
//	public LoginPage(WebDriver driver) {
//		this.actionDriver= new ActionDriver(driver);
//	}
	
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	//Method to perform Login
	public void login(String username, String password) {
		actionDriver.enterText(userNameFeild, username);
		actionDriver.enterText(passwordFeild, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//Method to get the text from error message
	
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	//Verify if error is correct or not
	
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}
}
