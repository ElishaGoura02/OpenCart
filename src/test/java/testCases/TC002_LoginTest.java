package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;

public class TC002_LoginTest extends BaseClass {
	@Test
	public void verify_login() {
		logger.info("starting TC002_LoginTest.........");
		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();

			// Login page
			LoginPage lp = new LoginPage(driver);
			lp.setEmail(p.getProperty("email"));
			lp.setPassword(p.getProperty("password"));
			lp.clickLogin();

			// MyAccount
			MyAccountPage myAcc = new MyAccountPage(driver);
			boolean targetPage = myAcc.isMyAccountPageExists();

			Assert.assertEquals(targetPage, true, "login failed");
			// Assert.assertTrue(targetPage);

			logger.info("finished TC002_LoginTest........");

		} catch (Exception e) {
			Assert.fail();
		}
	}

}
