package com.zoho.testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

public class Session extends Base{
	@Test
	public void doLogin(ITestContext context) {
		    log("Logging In");
		    //driver.get(prop.getProperty("url"));
		    //findElement("loginlink_css").click();
			//findElement("username_id").sendKeys("se.le.ni.umtraining10@gmail.com");
			//findElement("nextbutton_id").click();
			//findElement("password_xpath").sendKeys("Zoho@123");
			//findElement("nextbutton_id").click();
		}

	
	@Test
	public void doLogout(ITestContext context) {
		log("Logging out");
	}
}
