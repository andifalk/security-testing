package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Table;
import info.novatec.testit.webtester.pageobjects.TableRow;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Page object for basket.jsp.
 */
public class BasketPage extends AbstractStartPage {

	@IdentifyUsing ( "basket_entries" )
	private Table basketEntries;

    @PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/basket.jsp"));
	}

    public Integer getNumberOfBasketEntries () {
        return basketEntries.getNumberOfRows ();
    }

    public List<TableRow> getBasketEntries () {
        return basketEntries.getRows ();
    }

    public TableRow getBasketEntry ( int rowIndex ) {
        return basketEntries.getRow ( rowIndex );
    }
}
