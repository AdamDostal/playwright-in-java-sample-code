package framework.pages.common;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static framework.constants.Endpoints.UI_CONTACT_PATH;
import static framework.constants.Endpoints.UI_SUT_URL;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Step("Open the home page")
    public void openHomePage() {
        page.navigate(UI_SUT_URL);
    }

    @Step("Open the Contact page")
    public void toTheContactPage() {
        page.navigate(UI_CONTACT_PATH);
    }
}
