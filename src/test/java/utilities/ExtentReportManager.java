package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.net.URL;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testCases.BaseClass;


public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) {   //when i execute this method it will capture the text context(which test method we got executed,that details)
		
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");  //date format
		Date dt=new Date();  //representing the current date and time
		String currentdatetimestamp=df.format(dt);*/
		
		//if you hardcoded name we can't maintain the history of the reports. Suppose yesterday i have done one round of execution and again today,
		//so i want to verify the test results yetsterdays and today's execution then it is not possible.
		//so if you maintain the history of the reports then you can compare them.
		//Whenever you compare a report if you create the report name with the current timestamp then we can maintain the report
		//if you give the static name that will replace the old report, becaus ename is same
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// creating time stamp
		repName = "Test-Report-" + timeStamp + ".html";  //appending timestamp to the test-report(name of the report) //.html - extension odf the report
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
		sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));  //user.name - return current system name
		extent.setSystemInfo("Environemnt", "QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os");  //testContext - this particular test is executed by which xml filed i am getting that xml file, from that xml file we are getting the oarameter 'os' // by help of these variable current xml test 
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		//we are getting all the groups in the List collection
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();  //getIncludedGroups() - suppose if you run the grouping.xml file we will have some included groups, it will capture the groups which we have specified in the include section and display them in the report.
		if(!includedGroups.isEmpty()) {   //not empty 
		extent.setSystemInfo("Groups", includedGroups.toString());  //this method will add all the group names to the report. Before adding we need to check group names available or not 
		}
	}

	public void onTestSuccess(ITestResult result) {   //whenever this method got triggered, the result will capture the all the result information from the test method 
		//createTest - create a new entry in the testcase, from the result we are getting the which class we have executed,
		//and from that class we are getting the name of that class,with that class name i am creating the new entry in that report
		test = extent.createTest(result.getTestClass().getName()); 
		
		// from that result whicever test method got executed getting that method and whichever group the test method got assigned
		//i am also getting that group and adding to the test
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report 
		test.log(Status.PASS,result.getName()+" got successfully executed");
		
	}

	public void onTestFailure(ITestResult result) {  //result - contins the name of the name whichever is failed
		test = extent.createTest(result.getTestClass().getName());  //from the result it is getting the class and from tehclass it is getting the name of the class
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());  //getting the name of the method from result and passing to the captureScreen() method
			test.addScreenCaptureFromPath(imgPath);        //whenever you create a new object for base class, BaseClass will have a another object even that object also having a new driver - two drivers will be creating
			                                              //there are two different drivers are there so there will be a conflict. We need to make webdriver as static, then the same driver will be refered in the object also
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext testContext) {
		
		extent.flush();    //this method will consolidate all the information from the report and finally it will generated
		
		//reports will be generated under reports folder and if we want to open the report, wee need to go to the report folder and we need to right click on the report file open in the browser - we are doing it manually
		//instead of doing it manually, as soon as my report is generated immediately it will automatically open. 
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());  //open the report on the browser automatically 
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		/*  try {
			  URL url = new  URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
		  
		  // Create the email message 
		  ImageHtmlEmail email = new ImageHtmlEmail();
		  email.setDataSourceResolver(new DataSourceUrlResolver(url));
		  email.setHostName("smtp.googlemail.com"); 
		  email.setSmtpPort(465);
		  email.setAuthenticator(new DefaultAuthenticator("pavanoltraining@gmail.com","password")); 
		  email.setSSLOnConnect(true);
		  email.setFrom("pavanoltraining@gmail.com"); //Sender
		  email.setSubject("Test Results");
		  email.setMsg("Please find Attached Report....");
		  email.addTo("pavankumar.busyqa@gmail.com"); //Receiver 
		  email.attach(url, "extent report", "please check report..."); 
		  email.send(); // send the email 
		  }
		  catch(Exception e) 
		  { 
			  e.printStackTrace(); 
			  }
		 */ 
		 
	}

}
