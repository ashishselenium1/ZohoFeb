package com.zoho.util;

import java.util.Map;

public class Temp {

	public static void main(String[] args) {
    Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")+"/data/Data.xlsx");
    Map<String,String> data = new DataUtil().getTestCaseData(3, "CreateLeadTest", xls);
    System.out.println(data);
	}

}
