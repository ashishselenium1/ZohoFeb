package com.zoho.testcases;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.zoho.reports.ExtentManager;
import com.zoho.util.DataUtil;
import com.zoho.util.Xls_Reader;

public class Base {
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	SoftAssert softAssert;
	Properties prop;
	// name of the xls file
	// test data row number from the xls
	// browser
	
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext context) throws IOException {
		 System.out.println("-------------@BeforeTest--------------");
		 // init prop
	     prop= new Properties();
	     String path = "D:\\Whizdom-Trainings\\Online Training Workspace\\March2021Night\\DDF//src//test//resources//project.properties";
	     FileInputStream fs = new FileInputStream(path);
	     prop.load(fs);
	     context.setAttribute("prop", prop);
	     
		 
		 
		 context.setAttribute("stop", "0");
		 String browserName=(String)context.getCurrentXmlTest().getParameter("browser");
		 String data_file=(String)context.getCurrentXmlTest().getParameter("data_file");
		 String row_num=(String)context.getCurrentXmlTest().getParameter("row_num");
		 String sheetName=(String)context.getCurrentXmlTest().getParameter("dataFlag");
		 
		 System.out.println(browserName+" -- "+data_file+" -- "+row_num+" --- "+sheetName);
		 Xls_Reader xls = new Xls_Reader("D:\\Whizdom-Trainings\\Online Training Workspace\\March2021Night\\DDF/data/"+data_file);
		 Map<String,String> testCaseData = new DataUtil().getTestCaseData(Integer.parseInt(row_num), "CreateLeadTest", xls);
		 context.setAttribute("testCaseData", testCaseData);
		 if(prop.get("grid_run").equals("Y")) {
			 // 100 test cases
			 // selenium grid - RemoteWebDriver
			 // hit the hub
			 
			 DesiredCapabilities cap=null;
	        	
				if(browserName.equals("Mozilla")){
					cap = DesiredCapabilities.firefox();
					cap.setBrowserName("firefox");
					cap.setJavascriptEnabled(true);
					
					cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
					
				}else if(browserName.equals("Chrome")){
					 cap = DesiredCapabilities.chrome();
					 cap.setBrowserName("chrome");
					 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				}
				
				try {
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
		 }else {
				    
			 if(browserName.equals("Chrome")) {
			 // System.setProperty("webdriver.chrome.driver","D:/chromedriver.exe");
		        driver = new ChromeDriver();		
			 }else if(browserName.equals("Mozilla")) {
				 driver = new FirefoxDriver();
			 }
		 }
		 
		 
		 driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	//     context.setAttribute("webdriver", driver);
		 
	     // init reports
	     report = ExtentManager.getReports();
	     test=report.createTest(context.getCurrentXmlTest().getName());
	     log("Starting Test "+context.getCurrentXmlTest().getName());
	     log("Test Data => "+ testCaseData);
	     context.setAttribute("test", test);
	     
	     if(testCaseData.get("Runmode").equals("N")) {
			 // skip in the extent reports
	    	 test.log(Status.SKIP, "Skipping the test as runmode is N");
	    	 // skip in testNG
	    	 throw new SkipException("Skipping the test case");
		 }

	     // init softAssert
	     softAssert=new SoftAssert();
	     context.setAttribute("softAssert", softAssert);
	     
	     
	     
	}
	
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext context) {
		System.out.println("-------------@AfterTest--------------");
		 driver = (WebDriver) context.getAttribute("webdriver");
		 
		 if(driver!=null)
			 driver.quit();
            		 
		 if(report!=null)
		  report.flush();
 

	 }

	@BeforeMethod(alwaysRun = true)
	public void beforeMeth(ITestContext context) {
		
		System.out.println("-------------@BeforeMethod--------------");
		String stop  = (String)context.getAttribute("stop");
		if(stop.equals("1")) {
			throw new SkipException("Skipping the test");
		}
		 driver = (WebDriver) context.getAttribute("webdriver");
		 test = (ExtentTest) context.getAttribute("test");
		 softAssert = (SoftAssert) context.getAttribute("softAssert");
		 prop= (Properties) context.getAttribute("prop");
	}
	
	
	public void log(String msg) {
		System.out.println(msg);
		test.log(Status.INFO, msg);
	}
	
	public void logFailure(String failureMsg, boolean stopOnCriticalFailure) {
		System.out.println(failureMsg);
		// fail in extent
		test.log(Status.FAIL, failureMsg);
		// fail in test testng
		softAssert.fail(failureMsg);		
		// screenshot- selenium
		
		
		if(stopOnCriticalFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("stop", "1");
			softAssert.assertAll();
		}
	}
	
	public WebElement findElement(String locatorKey) {
		WebElement e =null;
		By locator = getLocator(locatorKey);
		try {
		 // WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
		  WebDriverWait wait = new WebDriverWait(driver,20);
		  wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		  wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		  e = driver.findElement(locator);
		}catch (Exception ex) {
			logFailure("Element not found", true);
		}
		return e;
	}
	
	public By getLocator(String locatorKey) {
		
		if(locatorKey.endsWith("_id"))
			return By.id(prop.getProperty(locatorKey));
	    else if(locatorKey.endsWith("_name"))
			return By.name(prop.getProperty(locatorKey));
	    else if(locatorKey.endsWith("_css"))
			return By.cssSelector(prop.getProperty(locatorKey));
	    else
	    	return By.xpath(prop.getProperty(locatorKey));
	}
	
	public String getData(String key) {
		Map<String,String> testCaseData = (Map<String,String>)Reporter.getCurrentTestResult().getTestContext().getAttribute("testCaseData");
		return testCaseData.get(key);

	}
}
