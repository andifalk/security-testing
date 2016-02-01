
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.AboutPage;
import info.novatec.test.ui.pages.AbstractStartPage;
import info.novatec.test.ui.pages.BasketPage;
import info.novatec.test.ui.pages.ContactPage;
import info.novatec.test.ui.pages.LoginPage;
import info.novatec.test.ui.pages.SearchPage;
import info.novatec.test.ui.pages.StartPage;
import info.novatec.test.ui.security.ZapScanner;
import info.novatec.testit.webtester.api.browser.Browser;
import info.novatec.testit.webtester.browser.factories.FirefoxFactory;
import info.novatec.testit.webtester.junit.annotations.CreateUsing;
import info.novatec.testit.webtester.junit.annotations.EntryPoint;
import info.novatec.testit.webtester.junit.runner.WebTesterJUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ClientApiException;

import javax.annotation.Resource;
import java.util.List;

import static info.novatec.test.ui.security.AlertMatchers.containsNoHighRiskAlerts;
import static org.junit.Assert.assertThat;

/*
 * Note that this is an example of how to use ZAP with Selenium tests,
 * not a good example of how to write good Selenium tests!
 */
@RunWith( WebTesterJUnitRunner.class )
public class MenuLinksTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( LoginTest.class );

	private static final String SITE = "http://localhost:8080/bodgeit/";

	private StartPage startPage;

	@Resource
	@CreateUsing( FirefoxFactory.class )
	@EntryPoint( SITE )
	private Browser browser;

	private ZapScanner zapScanner;

	@Before
	public void initStartPage () throws ClientApiException {
		LOGGER.info ( "Init scanner" );
		zapScanner = new ZapScanner ( "afa", "", "localhost", "8085" );
		startPage = browser.create(StartPage.class);
	}

	@Test
	public void verifyMenuLinks() {

		AboutPage aboutPage = startPage.clickAbout ();
		ContactPage contactPage = aboutPage.clickContact ();
		BasketPage basketPage = contactPage.clickBasket ();
		SearchPage searchPage = basketPage.clickSearch ();
		AbstractStartPage startPage  = searchPage.clickHome ();
		LoginPage loginPage = ((StartPage) startPage).clickLogin ();
		loginPage.clickHome ();
	}

	@After
	public void securityScan() {
		LOGGER.info ( "Performing security scan" );

		List<Alert> alerts = zapScanner.completeScan ( "http://localhost:8080", "" );
		assertThat ( "Has only minor risk alerts", alerts, containsNoHighRiskAlerts () );
	}

}
