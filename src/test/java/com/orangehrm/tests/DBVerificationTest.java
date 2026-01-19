package com.orangehrm.tests;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;


public class DBVerificationTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages(){
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
		
	}
	
	@Test(dataProvider="empVerification",dataProviderClass=DataProviders.class)
	public void verifyEmployeeNameFromDB(String empID,String empName) {
		
		SoftAssert softAssert=getSoftAssert();
		
		ExtentManager.logStep("Logging with admin credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtentManager.logStep("click on PIM tab");
		homePage.clickToOpenPIMTab();
		
		ExtentManager.logStep("Search for employee");
		homePage.employeeSearch(empName);
		
		ExtentManager.logStep("Get the employeename from database");
		String employeeId=empID;
		
		//fetch the data into a map
		Map<String,String> employeeDetails= DBConnection.getEmployeeDetails(employeeId);
		
		String empFirstName=employeeDetails.get("firstName");
		String empMiddleName=employeeDetails.get("middleName");
		String empLastName=employeeDetails.get("lastName");
		
		String empFirstAndMiddleName= (empFirstName+" "+empMiddleName).trim();
		//validation for first and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName),"First and Middle name are not matching");
		//validation for lastname
		ExtentManager.logStep("Verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(empLastName));
		
		ExtentManager.logStep("DB validtion completed");
		softAssert.assertAll();
	}
}
