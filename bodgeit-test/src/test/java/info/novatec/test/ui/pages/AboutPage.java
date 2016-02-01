package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import javax.annotation.PostConstruct;

public class AboutPage extends AbstractStartPage {

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/about.jsp"));
	}
}
