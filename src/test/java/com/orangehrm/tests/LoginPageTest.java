package com.orangehrm.tests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

 

public class LoginPageTest extends BaseClass{
	//new comment added--2nd comment
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void verifyValidLoginTest(String username,String password) {
		//ExtentManager.startTest("Valid Login Test");
		ExtentManager.logStep("Navigating login page , entering username and password");
		loginPage.login(username,password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		staticWait(2);
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");
		ExtentManager.logStep("Validation Successful");
		homePage.logout();
		ExtentManager.logStep("Logged out successfully");
		staticWait(2);
	}
	@Test(dataProvider="invalidLoginData", dataProviderClass=DataProviders.class)
	public void verifyInvalidLogintest(String username,String password) {
		//ExtentManager.startTest("Invalid Login Test");
		ExtentManager.logStep("Navigating login page , entering username and password");
		loginPage.login(username, password);
		String expectedErrorMessage="Invalid credentials";
		
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test failed :Invalid Error message");
		ExtentManager.logStep("Validation Successful");
	}
}
