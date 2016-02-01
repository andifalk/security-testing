package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import info.novatec.testit.webtester.pageobjects.PageObject;

import javax.annotation.PostConstruct;

public class AdminPage extends PageObject {

	@PostConstruct
	void assertThatCorrectPageIsDisplayed() {
		assertThat(getBrowser().getUrl (),
                containsString("/admin.jsp"));
	}

}
