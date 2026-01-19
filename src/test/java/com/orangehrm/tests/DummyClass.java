package com.orangehrm.tests;



import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass {
	@Test
	public void  dummyTest() {
	//	ExtentManager.startTest("Dummy Test1 Test");
	String actualTitle=getDriver().getTitle();
	ExtentManager.logStep("verifying the title");
	assert actualTitle.equals("OrangeHRM"):"Test Failed- Title is not matching";
	
	System.out.println("Test Passed - Title is Mathcing");
	//ExtentManager.logSkip("This case is skipped");
	throw new SkipException("Skipping the test as part of testing");
	}
}
