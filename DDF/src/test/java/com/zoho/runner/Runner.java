package com.zoho.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.zoho.util.Xls_Reader;

// mvn exec:java -Dexec.mainClass="com.zoho.runner.Runner"  

public class Runner {
	TestNGRunner testng;
	boolean parallelTests=false;
	public Runner() {
		testng = new TestNGRunner(1);
	}

	
	public void buildSuite(String suiteFileName) throws FileNotFoundException, IOException, ParseException {
		String path="D:\\Whizdom-Trainings\\Online Training Workspace\\March2021Night\\DDF/src/test/resources/jsons/"+suiteFileName;
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(path)));
		String suiteName = (String)json.get("suiitename");
		String dataXLS = (String)json.get("data_file");
		JSONArray browsers = (JSONArray)json.get("browsers");
		System.out.println("Suite Name "+suiteName);
		
		// create testNG suite
		testng.createSuite(suiteName, parallelTests);
		
		System.out.println("Data File Name "+dataXLS);
		Xls_Reader xls = new Xls_Reader("D:\\Whizdom-Trainings\\Online Training Workspace\\March2021Night\\DDF/data/"+dataXLS);
		System.out.println("Browsers "+browsers);
		
		for(int bId=0;bId<browsers.size();bId++) {// loop for browsers
			String browserName = (String)browsers.get(bId);
			
			JSONArray testcases = (JSONArray)json.get("testcases");
			for(int testCaseId=0;testCaseId<testcases.size();testCaseId++) {// loop for testcases
				
				JSONObject testCase=(JSONObject)testcases.get(testCaseId);
				String testName = (String)testCase.get("testname");
				String dataFlag = (String)testCase.get("dataflag");
				int rowsOfData=xls.getRowCount(dataFlag)-1;// later read from XLS
				System.out.println("Total Executions of test "+rowsOfData);

				for(int dRow=2;dRow<=xls.getRowCount(dataFlag);dRow++) {// iterating the rows of data
					System.out.println("---------------------------------------");
					System.out.println("Test Name: "+testName );
					System.out.println("Browser: "+browserName);
					System.out.println("Data Flag: "+dataFlag );
					System.out.println("Row Number for data : "+dRow );
					// add the test to the testng
					testng.addTest(testName+" ["+browserName+"] "+(dRow-1));
					// add the test browser parameter
					testng.addTestParameter("browser", browserName);
					// add test parameter - data_file
					testng.addTestParameter("data_file", dataXLS);
					// add test parameter - data row number
					testng.addTestParameter("row_num", String.valueOf(dRow));
					// sheet name- dataFlag
					testng.addTestParameter("dataFlag", dataFlag);
					
					
					
					JSONArray classes = (JSONArray)testCase.get("classes");
					for(int classId=0;classId<classes.size();classId++) {// loop for classes
						JSONObject classDetails=(JSONObject)classes.get(classId);
						String className=(String)classDetails.get("name");
						System.out.print(className);
						JSONArray methods=(JSONArray)classDetails.get("methods");
						
						// create a blank list for testng method names
						// add the class with blank method names
						List<String> names = new ArrayList<String>();
						testng.addTestClass(className, names);
						
						// fill the names array for testng
						for(int methodId=0;methodId<methods.size();methodId++) {// loop for methods
							String methodName = (String)methods.get(methodId);
							System.out.print(" - "+ methodName);
							names.add(methodName);
						}// loop for methods
						System.out.println();
					}// loop for classes
				}// iterating the rows of data
			}// loop for testcases
		}// loop for browsers

	}
	
	public void run() {
		testng.run();
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		
		Runner runner = new Runner();
		runner.buildSuite("leadsuite.json");
		
		runner.run();
			
	}
	
	

}
