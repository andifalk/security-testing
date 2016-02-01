
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.BasketPage;
import info.novatec.test.ui.pages.ProductPage;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Verifies adding products to basket.
 */
@RunWith( WebTesterJUnitRunner.class )
public class BasketTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( BasketTest.class );

	private static final String SITE = "http://localhost:8080/bodgeit/";

	private StartPage startPage;

	@Resource
	@CreateUsing(value=FirefoxFactory.class, proxy = info.novatec.test.ui.config.ProxyConfig.class)
	@EntryPoint( SITE )
	private Browser browser;

	@Before
	public void initStartPage () throws ClientApiException {
		LOGGER.info ( "Init start page" );
		startPage = browser.create(StartPage.class);
	}

	@Test
	public void verifyAddProductsToBasket() {
		LOGGER.info ( "Add products to basket" );

        assertThat ( "Should have a list of products on start page", startPage.getNumberOfProducts (), is ( greaterThan ( 0 ) ) );

        ProductPage productPage = startPage.selectProduct ( 1 );
        BasketPage basketPage = productPage.addToBasket ( 5 );

        assertThat ( basketPage.getNumberOfBasketEntries (), is ( greaterThan ( 0 ) ) );
	}

}
