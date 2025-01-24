package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	//DataProvider 1
	
	@DataProvider(name="LoginData")
	public String [][] getData() throws IOException
	{
		//String path=".\\testData\\Opencart_LoginData.xlsx";//taking xl file from testData
	    String path ="C:\\Users\\goura\\eclipse-Java\\OpenCart\\testData\\Opencart_LoginData.xlsx";
		ExcelUtility xlutil=new ExcelUtility(path);//creating an object for XLUtility
		
		int totalrows=xlutil.getRowCount("Sheet1");	
		int totalcols=xlutil.getCellCount("Sheet1",1);   // 1 means row number
				
		String logindata[][]=new String[totalrows][totalcols];//created for two dimension array which can store the data user and password
		
		for(int i=1;i<=totalrows;i++)  //1   //read the data from xl storing in two deminsional array
		{		
			for(int j=0;j<totalcols;j++)  //0    i is rows j is col
			{
				logindata[i-1][j]= xlutil.getCellData("Sheet1",i, j);  //1,0 from the excel getting the cell data by passing row(i) and column(j) number and store that in 2 dimensional array()
			}  //i-1 means array index will start from zero.
		}
	return logindata;//returning two dimension array
				
	}
	
	//DataProvider 2
	
	//DataProvider 3
	
	//DataProvider 4
}
