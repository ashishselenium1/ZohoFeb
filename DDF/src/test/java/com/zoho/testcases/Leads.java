package com.zoho.testcases;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zoho.util.DataUtil;
import com.zoho.util.Xls_Reader;

public class Leads extends Base{

	@Test
	public void createLead(ITestContext context) {
		log("Creating a Lead");	
		String firstName = getData("FirstName");
		String lastName = getData("LastName");
		String email = getData("Email");
		String company = getData("Company");
		System.out.println(firstName +" -- "+ lastName +" --- "+ email +" -- "+ company);
	}
	
	@Test
	public void verifyLeadCreation(ITestContext context) {
		log("Verifying a lead");
		softAssert.assertAll();
	}
	
	
}
