package framework.pages.catalog;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static framework.constants.Endpoints.API_PRODUCTS;
import static framework.constants.Endpoints.API_PRODUCT_SEARCH;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    public void searchBy(String keyword) {
        page.waitForResponse("**" + API_PRODUCT_SEARCH + "?**", () -> {
            page.getByPlaceholder("Search").fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }

    public void clearSearch() {
        page.waitForResponse("**" + API_PRODUCT_SEARCH + "**", () -> {
            page.getByTestId("search-reset").click();
        });
    }

    public void filterBy(String filterName) {
        page.waitForResponse("**" + API_PRODUCTS + "?**by_category=**", () -> {
            page.getByLabel(filterName).click();
        });
    }

    public void sortBy(String sortFilter) {
        page.waitForResponse("**" + API_PRODUCTS + "?sort=**", () -> {
            page.getByTestId("sort").selectOption(sortFilter);
        });
        page.waitForTimeout(250);
    }
}