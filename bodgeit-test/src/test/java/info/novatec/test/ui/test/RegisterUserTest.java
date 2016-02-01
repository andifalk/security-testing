
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.AuthenticatedStartPage;
import info.novatec.test.ui.pages.LoginPage;
import info.novatec.test.ui.pages.RegisterPage;
import info.novatec.test.ui.pages.StartPage;
import info.novatec.testit.junit.ZapSecurityScan;
import info.novatec.testit.webtester.api.browser.Browser;
import info.novatec.testit.webtester.browser.factories.FirefoxFactory;
import info.novatec.testit.webtester.junit.annotations.CreateUsing;
import info.novatec.testit.webtester.junit.annotations.EntryPoint;
import info.novatec.testit.webtester.junit.runner.WebTesterJUnitRunner;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.ClientApiException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Verifies registering users.
 */
@RunWith( WebTesterJUnitRunner.class )
public class RegisterUserTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( RegisterUserTest.class );
	private static final String SITE = "http://localhost:8080/bodgeit/";

	protected StartPage startPage;

	@Rule
	public ZapSecurityScan zapSecurityScan = new ZapSecurityScan ( "afa", "localhost", "8085", "" );

	@Resource
	@CreateUsing( FirefoxFactory.class )
	@EntryPoint( SITE )
	protected Browser browser;

	@Before
	public void initStartPage () throws ClientApiException {
		LOGGER.info ( "Init start page" );
		startPage = browser.create(StartPage.class);
	}

	@Test
	public void verifyRegisterUser() {
		// Create random username so we can rerun test
		String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
		LoginPage loginPage = startPage.clickLogin();
		RegisterPage registerPage = loginPage.clickRegisterUser();
		AuthenticatedStartPage authenticatedStartPage = registerPage.registerUser(randomUser, "password");
		authenticatedStartPage.clickLogout();
	}

    @Test
    public void verifyRegisterAndLoginUser() {

        // Create random username so we can rerun test
        String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
        LoginPage loginPage = startPage.clickLogin();
        RegisterPage registerPage = loginPage.clickRegisterUser ();
        AuthenticatedStartPage authenticatedStartPage = registerPage.registerUser ( randomUser, "password" );
        StartPage startPage = authenticatedStartPage.clickLogout ();
        loginPage = startPage.clickLogin();
        authenticatedStartPage = loginPage.login(randomUser, "password");
        assertThat ( "Should have logged in successfully",
                browser.getWebDriver ().getPageSource ().indexOf ( "You have logged in successfully:" ),
                is ( greaterThan ( 0 ) ) );
        authenticatedStartPage.clickLogout ();
    }

	@Test
	public void verifyRegisterUserInvalidUserName() {
		LoginPage loginPage = startPage.clickLogin ();
		RegisterPage registerPage = loginPage.clickRegisterUser ();
		registerPage = registerPage.registerUserExpectError("invalid", "password", "password");
		assertThat ( "Got expected error message", registerPage.getErrorMessage (), containsString ( "supply a valid email address" ) );
	}

	@Test
	public void verifyRegisterUserPasswordsNotMatching() {
		String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
		LoginPage loginPage = startPage.clickLogin();
		RegisterPage registerPage = loginPage.clickRegisterUser ();
		registerPage = registerPage.registerUserExpectError(randomUser, "password1", "password2");
		assertThat("Got expected error message", registerPage.getErrorMessage (), containsString("passwords you have supplied are different"));
	}

}
