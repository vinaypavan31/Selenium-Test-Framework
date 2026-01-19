package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	@Test
	public void verifyGetUserAPI() {
		SoftAssert softAssert = new SoftAssert();
		
		// step 1: define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);

		// step 2: send GET request
		ExtentManager.logStep("Sending GET request to the API");
		Response response = APIUtility.sendGetRequest(endPoint);

		// step 3: validate the status code
		ExtentManager.logStep("Validating API response status code");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}

		// step 4: validate username
		ExtentManager.logStep("Validating response body for username");
		String username = APIUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);
		softAssert.assertTrue(isUserNameValid, "Username is Invalid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Username Validation Failed!");
		}

		// step 5: validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = APIUtility.getJsonValue(response, "email");
		boolean isUserEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isUserEmailValid, "Username is Invalid");
		if (isUserEmailValid) {
			ExtentManager.logStepValidationForAPI("User Email Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("User Email Validation Failed!");
		}
		softAssert.assertAll();
	}
}
