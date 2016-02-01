package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Link;
import info.novatec.testit.webtester.pageobjects.Table;
import info.novatec.testit.webtester.pageobjects.TableRow;
import org.openqa.selenium.By;

import javax.annotation.PostConstruct;
import java.util.List;

public class StartPage extends AbstractStartPage {

	@IdentifyUsing ("login")
	protected Link login;

	@IdentifyUsing("products")
	private Table products;

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(login.isVisible(), is(true));
	}
	
	public StartPage clickHome() {
		this.home.click ();
		return create(StartPage.class);
	}

	public LoginPage clickLogin() {
		this.login.click();
		return create(LoginPage.class);
	}

    public ProductPage selectProduct ( int rowIndex ) {
        TableRow productRow = getProductRow ( rowIndex );

        assert productRow != null;
        assert productRow.getFields ().size () > 0;

        productRow.getFields ().get ( 0 ).getWebElement ().findElement ( By.tagName ( "a" ) ).click ();
        return create ( ProductPage.class );
    }

	public int getNumberOfProducts() {
		return products.getNumberOfRows();
	}

	public List<TableRow> getProducts () {
		return products.getRows ();
	}

	private TableRow getProductRow ( int rowIndex ) {
		return products.getRow ( rowIndex );
	}

}
