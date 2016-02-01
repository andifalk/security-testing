
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.AuthenticatedStartPage;
import info.novatec.test.ui.pages.LoginPage;
import info.novatec.test.ui.pages.RegisterPage;
import info.novatec.test.ui.pages.SearchPage;
import info.novatec.test.ui.pages.SearchResultsPage;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Verifies one workflow for showcase.
 */
@RunWith( WebTesterJUnitRunner.class )
public class ShowCaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( ShowCaseTest.class );
	private static final String SITE = "http://localhost:8080/bodgeit/";

	protected StartPage startPage;

	@Rule
	public ZapSecurityScan zapSecurityScan = new ZapSecurityScan ( "afa", "localhost", "8085", "injections" );

    @CreateUsing(value=FirefoxFactory.class, proxy = info.novatec.test.ui.config.ProxyConfig.class)
	@Resource
	@EntryPoint( SITE )
	protected Browser browser;

	@Before
	public void initStartPage () throws ClientApiException {
		LOGGER.info ( "Init start page" );
		startPage = browser.create(StartPage.class);
	}

    @Test
    public void verifyRegisterAndLoginUserAndSearch() {

        // Create random username so we can rerun test
        String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";

        //Navigate to login page
        LoginPage loginPage = startPage.clickLogin();

        //Navigate to register new user page
        RegisterPage registerPage = loginPage.clickRegisterUser ();
        AuthenticatedStartPage authenticatedStartPage = registerPage.registerUser ( randomUser, "password" );
        StartPage startPage = authenticatedStartPage.clickLogout ();

        //login with newly created user
        loginPage = startPage.clickLogin();
        authenticatedStartPage = loginPage.login(randomUser, "password");

        assertThat ( "Should have logged in successfully",
                browser.getWebDriver ().getPageSource ().indexOf ( "You have logged in successfully:" ),
                is ( greaterThan ( 0 ) ) );

        //search for some stuff
        SearchPage searchPage = authenticatedStartPage.clickSearch();
        SearchResultsPage searchResultsPage = searchPage.performSearch ( "doo" );
        List<String> searchResults = searchResultsPage.getSearchResults ();

        assertThat("Should contain expected number of search results", searchResults.size(), is(2));
        assertThat("Should contain expected results",
                searchResults, hasItems ( "Zip a dee doo dah", "Bonzo dog doo dah" ));

    }

}
