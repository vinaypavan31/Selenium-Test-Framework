package com.orangehrm.tests;



import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {
	@Test
	public void  dummyTest() {
		//ExtentManager.startTest("Dummy Test1 Test");
	String actualTitle=getDriver().getTitle();
	ExtentManager.logStep("verifying the title");
	assert actualTitle.equals("OrangeHRM"):"Test Failed- Title is not matching";
	
	System.out.println("Test Passed - Title is Mathcing");
	ExtentManager.logStep("Validation Successful");
	}
}
