package info.novatec.test.ui.pages;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPage extends AbstractStartPage {

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/contact.jsp"));
	}
	
}
