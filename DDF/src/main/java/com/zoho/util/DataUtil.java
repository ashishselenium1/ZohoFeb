package com.zoho.util;

import java.util.Hashtable;
import java.util.Map;

public class DataUtil {
	
	//public static void main(String[] a) {
	
	// read data from any sheet of any any xls file
	public Object[][] getTestData(String sheet,Xls_Reader xls){
		//String sheet="DeleteLeadTest";
		//Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")+"//data//Data.xlsx");
		int cols = xls.getColumnCount(sheet);
		int rows = xls.getRowCount(sheet);
		System.out.println("cols-"+cols);
		System.out.println("rows-"+rows);
		Object data[][] = new Object[rows-1][cols];
		
		for(int rNum=2;rNum<=rows;rNum++) {
			for(int cNum=0;cNum<cols;cNum++) {
				String colName = xls.getCellData(sheet, cNum, 1);
				String sheetData = xls.getCellData(sheet, cNum, rNum);
				// 00  01  02 
				// 10  11  12
				data[rNum-2][cNum]=sheetData;
				System.out.println(colName+" - "+sheetData);
			}
			System.out.println("--------------");
		}
		
		return data;
		
		
	}
	
	public Map<String,String> getTestCaseData(int row, String sheet,Xls_Reader xls){
		Map<String,String> dataMap = new Hashtable<String,String>();
		for(int cNum=0;cNum<xls.getColumnCount(sheet);cNum++) {
			String colName=xls.getCellData(sheet, cNum, 1);
			String cellVal=xls.getCellData(sheet, cNum, row);
			dataMap.put(colName,cellVal);
		}
		return dataMap;
	}
		

}
