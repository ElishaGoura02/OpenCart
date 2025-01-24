package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;	

public class TC001_AccountRegistrationTest extends BaseClass{

	@Test
	public void verify_account_registration() throws InterruptedException {

		logger.info("********* TC001_AccountRegistrationTest");
		try {
		HomePage hp = new HomePage(driver); // constructor is trying invoke at the runtime
		hp.clickMyAccount(); // driver - created in the setUp method
		logger.info("clicked on MuAccount link");
		hp.clickRegister();
		logger.info("clicked on Register link");

		AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		logger.info("providing customer details");
		//regpage.setFirstName("Jhon");
		regpage.setFirstName(randomString().toUpperCase());   //dynamically passing
		regpage.setLastName("David");
		// regpage.setEmail("abcjhon@gmail.com");
		regpage.setEmail(randomString() + "@gmail.com"); // randomly generated the email
		//regpage.setTelephone("9565832565");
		regpage.setTelephone(randomNumber());

		//regpage.setPassword("david123");
		
		String password = randomAlphaNumeric();   //calling method and storing into variable and passing that variable
		
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);

		regpage.setPrivacyPolicy();
		regpage.clickContinue();

		String confmsg = regpage.getConfirmationMsg();
		Assert.assertEquals(confmsg, "Your Account Has Been Created!");
		//driver.navigate().back();
		
		}
		catch(Exception e) {
			logger.error("Test Failed");
			logger.debug("debug logs");
			Assert.fail();
		}
	}
}

