package info.novatec.test.ui.pages;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Button;
import info.novatec.testit.webtester.pageobjects.Link;
import info.novatec.testit.webtester.pageobjects.Table;
import info.novatec.testit.webtester.pageobjects.TextField;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Page object for product.jsp.
 */
public class ProductPage extends AbstractStartPage {

	@IdentifyUsing ( "product" )
	Table productTable;

	@IdentifyUsing ( "quantity" )
    TextField quantityInput;

	@IdentifyUsing ( "increase" )
	Link increaseQuantity;

	@IdentifyUsing ( "decrease" )
	Link decreaseQuantity;

	@IdentifyUsing ( "submit" )
    Button addToBasket;

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/product.jsp"));
	}
	
	public BasketPage addToBasket(int quantity) {

		for ( int i = 2; i < quantity; i++ ) {
			increaseQuantity.click ();

		}
		addToBasket.click ();
        return create(BasketPage.class);
	}
}
