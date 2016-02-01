package info.novatec.test.ui.pages;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Button;
import info.novatec.testit.webtester.pageobjects.Link;
import info.novatec.testit.webtester.pageobjects.PageObject;
import info.novatec.testit.webtester.pageobjects.PasswordField;
import info.novatec.testit.webtester.pageobjects.TextField;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginPage extends AbstractStartPage {
	@IdentifyUsing ( "username" )
    TextField username;

    @IdentifyUsing ( "password" )
    PasswordField password;

    @IdentifyUsing ( "submit" )
    Button login;

    @IdentifyUsing ( "error" )
    PageObject errorMessage;
    
    @IdentifyUsing ( "register" )
	Link register;

    @PostConstruct
    void assertThatCorrectPageIsDisplayed () {
        assertThat(getBrowser().getUrl (),
                containsString("/login.jsp"));
    }

    /* workflows */

    public AuthenticatedStartPage login (String username, String password) {
        return setUsername(username).setPassword(password).clickLogin();
    }

    public LoginPage loginExpectingError (String username, String password) {
        return setUsername(username).setPassword(password).clickExpectingError();
    }
    
    /* actions */

    public RegisterPage clickRegisterUser() {
		this.register.click();
        return create(RegisterPage.class);
	}

    /* getter */

    public String getErrorMessage () {
        return errorMessage.getVisibleText();
    }
    
    /* private */
    
    private LoginPage setUsername (String username) {
        this.username.setText(username);
        return this;
    }

    private LoginPage setPassword (String password) {
        this.password.setText(password);
        return this;
    }

    private AuthenticatedStartPage clickLogin () {
        this.login.click();
        return create(AuthenticatedStartPage.class);
    }

    private LoginPage clickExpectingError () {
        this.login.click();
        return create(LoginPage.class);
    }

}
