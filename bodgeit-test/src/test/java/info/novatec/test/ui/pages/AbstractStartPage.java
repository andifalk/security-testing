package info.novatec.test.ui.pages;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.pageobjects.Link;
import info.novatec.testit.webtester.pageobjects.PageObject;
import info.novatec.testit.webtester.pageobjects.Table;
import info.novatec.testit.webtester.pageobjects.TableRow;

import java.util.List;

public abstract class AbstractStartPage extends PageObject {

	@IdentifyUsing( "home")
	protected Link home;

	@IdentifyUsing("about")
	private Link about;

	@IdentifyUsing("contact")
	private Link contact;

	@IdentifyUsing("comments")
	private Link comments;

	@IdentifyUsing("basket")
	private Link basket;

	@IdentifyUsing("search")
	private Link search;



	public AbstractStartPage clickHome() {
		this.home.click();
		return create(StartPage.class);
	}
	
	public AboutPage clickAbout() {
		this.about.click();
		return create(AboutPage.class);
	}

	public ContactPage clickContact() {
		this.contact.click();
		return create(ContactPage.class);
	}
	
	public BasketPage clickBasket() {
		this.basket.click();
		return create(BasketPage.class);
	}

	public SearchPage clickSearch() {
		this.search.click();
		return create(SearchPage.class);
	}


}
