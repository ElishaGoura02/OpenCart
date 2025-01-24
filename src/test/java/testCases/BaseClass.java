package testCases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
	public static WebDriver driver;
	public Logger logger;
	public Properties p;

	// make evrything as public because we are working with multiple packages so we
	// sould able to access them throughout the project
	@BeforeClass(groups = { "Sanity", "Regression", "Master" })
	@Parameters({ "os", "browser" })
	public void setUp(String os, String br) throws IOException { // initiating the driver

		// Loading config.properties file
		FileReader file = new FileReader(
				"C:\\Users\\goura\\eclipse-Java\\OpenCart\\src\\test\\resources\\config.properties");
		p = new Properties(); // loading the file p - properties file created in resources
		p.load(file);

		// this - this is always representing the class
		// this method will get the class name and for that particular class it will get
		// the logger and store into the variable
		logger = LogManager.getLogger(this.getClass()); // we have to specify the class name - we can run multiple
														// classes so at runtime dynamically it should take class
														// whichever class we are running

		/*if (p.getProperty("execution_env").equalsIgnoreCase("remote"))
		// The URL will be IP Address of Hub Machine + Hub Port + /wd/hub
		// http://192.168.13.1:4444/wd/hub or http://localhost:4444/wd/hub
		{
			DesiredCapabilities capabilities = new DesiredCapabilities();

			// os
			if (os.equalsIgnoreCase("windows")) {
				capabilities.setPlatform(Platform.WIN11);
			} else if (os.equalsIgnoreCase("mac")) {
				capabilities.setPlatform(Platform.MAC);
			} else {
				System.out.println("No matching os");
				return;
			}

			// browser
			switch (br.toLowerCase()) {
			case "chrome":
				capabilities.setBrowserName("chrome");
				break;
			case "edge":
				capabilities.setBrowserName("MicrosoftEdge");
				break;
			default:
				System.out.println("No matching browser");
				return;
			}

			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
			// driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
			// capabilities); // whatever huburl we
			// have captured that url we have to convert into the URL Object format
		}

		if (p.getProperty("execution_env").equalsIgnoreCase("local")) {*/

			switch (br.toLowerCase()) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				System.setProperty("webdriver.chrome.driver",
						"C:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
			case "edge":
				driver = new EdgeDriver();
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			default:
				System.out.println("Invalid browser name..");
				return;
			}
		

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(p.getProperty("appURL")); // reading url from properties file.
		// driver.get("https://tutorialsninja.com/demo/");
		driver.manage().window().maximize();
	}



	// user defined methods
	public String randomString() { // return randon string
		String generatedString = RandomStringUtils.randomAlphabetic(5); // generate the random string when i call this
																		// method
		return generatedString;
	}

	public String randomNumber() { // return randon number
		String generatedNumber = RandomStringUtils.randomNumeric(10);
		return generatedNumber;
	}

	public String randomAlphaNumeric() { // return randon alphanumeric
		String generatedAlphabetc = RandomStringUtils.randomAlphabetic(3);
		String generatedNumber = RandomStringUtils.randomNumeric(3);
		return (generatedAlphabetc + "!@" + generatedNumber);
	}

	public String captureScreen(String tname) throws IOException { // tname - name of the test method

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

		String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile = new File(targetFilePath);

		sourceFile.renameTo(targetFile);

		return targetFilePath; // attach screeshot to the report - path of the screenshot

	}

	@AfterClass(groups = { "Sanity", "Regression", "Master" })
	public void tearDown() {
		driver.quit();
	}

}
