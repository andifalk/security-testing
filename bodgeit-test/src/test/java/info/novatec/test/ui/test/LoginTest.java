
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.AuthenticatedStartPage;
import info.novatec.test.ui.pages.LoginPage;
import info.novatec.test.ui.pages.StartPage;
import info.novatec.testit.webtester.api.browser.Browser;
import info.novatec.testit.webtester.browser.factories.FirefoxFactory;
import info.novatec.testit.webtester.junit.annotations.CreateUsing;
import info.novatec.testit.webtester.junit.annotations.EntryPoint;
import info.novatec.testit.webtester.junit.runner.WebTesterJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.ClientApiException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/*
 * Acceptance test to verify correct login of users with additional automatic security test for
 * vulnerabilities.
 */
@RunWith( WebTesterJUnitRunner.class )
public class LoginTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( LoginTest.class );

	private static final String SITE = "http://localhost:8080/bodgeit/";

	private StartPage startPage;

	@Resource
	@CreateUsing ( FirefoxFactory.class )
	@EntryPoint( SITE )
	private Browser browser;

    @Before
    public void initStartPage () throws ClientApiException {
        LOGGER.info ( "Init start page" );
        startPage = browser.create(StartPage.class);
    }
    
	@Test
	public void verifyLoginUser() throws ClientApiException {
        LOGGER.info ( "Verify login user" );

		LoginPage loginPage = startPage.clickLogin();
		AuthenticatedStartPage authenticatedStartPage = loginPage.login ( "test@thebodgeitstore.com", "password" );
		authenticatedStartPage.clickLogout ();
	}

	
	@Test
	public void verifyLoginInvalidUser() throws ClientApiException {
        LOGGER.info ( "Verify login invalid user" );

		LoginPage loginPage = startPage.clickLogin ();
		loginPage = loginPage.loginExpectingError("test@example.com", "dummy");
		assertThat ( "Got expected error message", loginPage.getErrorMessage (), containsString ( "invalid name or password" ) );

	}

}
