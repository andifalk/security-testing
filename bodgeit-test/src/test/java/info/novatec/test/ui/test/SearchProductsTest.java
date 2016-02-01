
package info.novatec.test.ui.test;

import info.novatec.test.ui.pages.SearchPage;
import info.novatec.test.ui.pages.SearchResultsPage;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Verifies searching products.
 */
@RunWith( WebTesterJUnitRunner.class )
public class SearchProductsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger ( SearchProductsTest.class );

	private static final String SITE = "http://localhost:8080/bodgeit/";

	private StartPage startPage;

	@Resource
	@CreateUsing( FirefoxFactory.class )
	@EntryPoint( SITE )
	private Browser browser;

	@Before
	public void initStartPage () throws ClientApiException {
		LOGGER.info ( "Init start page" );
		startPage = browser.create(StartPage.class);
	}

	@Test
	public void verifySearch() {
		SearchPage searchPage = startPage.clickSearch();
		SearchResultsPage searchResultsPage = searchPage.performSearch("doo");
		List<String> searchResults = searchResultsPage.getSearchResults ();
		assertThat("Should contain expected number of search results", searchResults.size(), is(2));
		assertThat("Should contain expected results", searchResults, hasItems ( "Zip a dee doo dah", "Bonzo dog doo dah" ));
	}

	@Test
	public void verifyNoSearchResults() {
		SearchPage searchPage = startPage.clickSearch();
		SearchResultsPage searchResultsPage = searchPage.performSearch("dummy");
		String noSearchResultsMessage = searchResultsPage.getNoSearchResult ();
		assertThat("Should contain expected message for no results", noSearchResultsMessage, containsString ( "No Results Found" ));
	}

	@Test
	public void verifyNoSearchWithEmptyInput() {
		SearchPage searchPage = startPage.clickSearch();
		SearchResultsPage searchResultsPage = searchPage.performSearch("");
		List<String> searchResults = searchResultsPage.getSearchResults ();
		assertThat("Should contain expected number of search results", searchResults.size (), greaterThanOrEqualTo ( 32 ));
	}

}
