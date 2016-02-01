package info.novatec.test.ui.pages;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Button;
import info.novatec.testit.webtester.pageobjects.TextField;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchPage extends AbstractStartPage {

    @IdentifyUsing ( "searchInput" )
    TextField searchInput;

    @IdentifyUsing ( "submit" )
    Button search;
    
	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/search.jsp"));
	}
	
	public SearchResultsPage performSearch(String searchTerm) {
		searchInput.setText(searchTerm);
		search.click();
        return create(SearchResultsPage.class);
	}
}
