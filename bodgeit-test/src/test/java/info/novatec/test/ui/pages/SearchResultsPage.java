package info.novatec.test.ui.pages;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.PageObject;
import info.novatec.testit.webtester.pageobjects.Table;
import info.novatec.testit.webtester.pageobjects.TableRow;
import info.novatec.testit.webtester.pageobjects.TextField;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchResultsPage extends AbstractStartPage {

    @IdentifyUsing ( "searchInput" )
    TextField searchInput;

    @IdentifyUsing ( "noresult" )
    PageObject search;
    
    @IdentifyUsing("results")
	private Table results;
    
	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/search.jsp?q="));
	}
	
	public String getNoSearchResult() {
		return search.getVisibleText();
	}

	public List<String> getSearchResults() {
		List<String> resultList = new ArrayList<> ();
		for (int i=1; i < results.getNumberOfRows(); i++) {
			TableRow tableRow = results.getRow(i);
			resultList.add(tableRow.getField(0).getVisibleText());
		}
		return resultList;
	}

}
