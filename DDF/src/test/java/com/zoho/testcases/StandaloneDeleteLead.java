package com.zoho.testcases;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.zoho.util.DataUtil;
import com.zoho.util.Xls_Reader;

public class StandaloneDeleteLead extends Base{
// build dataprovider	
	
	@Test(dataProvider = "getData")
	public void deleteLead(String runmode, String browser, String firstName) {
		if(runmode.equals("N")) {
			test.log(Status.SKIP, "Runmode is N");
			throw new SkipException("Runmode is N");
		}
		
		
		log("Starting Delete Lead Test");
		log(browser +" --- "+firstName);
	}

	
	@DataProvider
	public Object[][] getData(){
		Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")+"//data//Data.xlsx");
		return new DataUtil().getTestData("DeleteLeadTest", xls);
		
	}
	
}
