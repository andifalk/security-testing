package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Link;

import javax.annotation.PostConstruct;

public class AuthenticatedStartPage extends AbstractStartPage {

	@IdentifyUsing ("logout")
	private Link logout;

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(logout.isVisible(), is(true));
	}
	
	@Override
	public AuthenticatedStartPage clickHome() {
		this.home.click();
		return create(AuthenticatedStartPage.class);
	}

	public StartPage clickLogout() {
		this.logout.click();
		return create(StartPage.class);
	}

}
