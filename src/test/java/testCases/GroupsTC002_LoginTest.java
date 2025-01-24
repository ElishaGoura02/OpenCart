package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;

public class GroupsTC002_LoginTest extends BaseClass {
	//master - Everyhting comes under the master, when you dividing the groups,
	//sometimes we want to execute all the testcases in that case we will select the master group
	@Test(groups = {"Sanity", "Master"})    
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
