package info.novatec.test.ui.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Button;
import info.novatec.testit.webtester.pageobjects.PageObject;
import info.novatec.testit.webtester.pageobjects.PasswordField;
import info.novatec.testit.webtester.pageobjects.TextField;

import javax.annotation.PostConstruct;

public class RegisterPage extends AbstractStartPage {

	@IdentifyUsing ( "username" )
    TextField username;

    @IdentifyUsing ( "password1" )
    PasswordField password1;

    @IdentifyUsing ( "password2" )
    PasswordField password2;

    @IdentifyUsing ( "submit" )
    Button register;

    @IdentifyUsing ( "error" )
    PageObject errorMessage;
    
    @PostConstruct
    public void assertThatCorrectPageIsDisplayed () {
        assertThat(getBrowser().getUrl (), containsString("/register.jsp"));
    }
    
	public AuthenticatedStartPage registerUser(String user, String password) {
		return setUsername(user).setPassword1(password).setPassword2(password).clickRegister();
	}
	
	public RegisterPage registerUserExpectError(String user, String password1, String password2) {
		return setUsername(user).setPassword1(password1).setPassword2(password2).clickRegisterExpectingError();
	}
	
    /* getter */

    public String getErrorMessage () {
        return errorMessage.getVisibleText();
    }
    
    /* private */
    
	private RegisterPage setUsername (String username) {
        this.username.setText(username);
        return this;
    }

    private RegisterPage setPassword1 (String password) {
        this.password1.setText(password);
        return this;
    }

    private RegisterPage setPassword2 (String password) {
        this.password2.setText(password);
        return this;
    }

    private AuthenticatedStartPage clickRegister () {
        this.register.click();
        return create(AuthenticatedStartPage.class);
    }

    private RegisterPage clickRegisterExpectingError () {
        this.register.click();
        return create(RegisterPage.class);
    }

}
