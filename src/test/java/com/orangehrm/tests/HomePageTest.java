package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	@BeforeMethod
	public void setupPages() {
		this.loginPage=new LoginPage(getDriver());
		this.homePage=new HomePage(getDriver());
		
	}
	@Test(dataProvider="validLoginData",dataProviderClass=DataProviders.class)
	public void verifyOrangeHRMLogo(String username,String password) {
		//ExtentManager.startTest("Home Page Verify Logo Test");
		ExtentManager.logStep("Navigating to login page and entering username and password");
		loginPage.login(username,password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visible");
		ExtentManager.logStep("Validation is successful");
		ExtentManager.logStep("Logged out successfully!");
		
	}

}
