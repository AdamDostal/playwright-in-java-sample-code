package com.serenitydojo.playwright.toolshop.tests.catalog;

import com.serenitydojo.playwright.toolshop.constants.UrlPaths;
import com.serenitydojo.playwright.toolshop.fixtures.BaseTest;
import com.serenitydojo.playwright.toolshop.fixtures.TakesFinalScreenshot;
import com.serenitydojo.playwright.toolshop.fixtures.TracingManager;
import com.serenitydojo.playwright.toolshop.pages.catalog.ProductList;
import com.serenitydojo.playwright.toolshop.pages.catalog.SearchComponent;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Searching for products")
@Feature("Product Catalog")
public class SearchForProductsTest extends BaseTest implements TakesFinalScreenshot, TracingManager {

    @BeforeEach
    void openHomePage() {
        page.navigate(UrlPaths.SUT_URL);
    }

    @Nested
    @DisplayName("Searching by keyword")
    @Story("Searching by keyword")
    class SearchingByKeyword {

        @Test
        @DisplayName("When there are matching results")
        void whenSearchingByKeyword() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);

            searchComponent.searchBy("tape");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
        }

        @Test
        @DisplayName("When there are no matching results")
        void whenThereIsNoMatchingProduct() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("unknown");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).isEmpty();
            Assertions.assertThat(productList.getSearchCompletedMessage()).contains("There are no products found.");
        }

        @Test
        @DisplayName("When the user clears a previous search results")
        void clearingTheSearchResults() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("saw");

            var matchingFilteredProducts = productList.getProductNames();
            Assertions.assertThat(matchingFilteredProducts).hasSize(2);

            searchComponent.clearSearch();

            var matchingProducts = productList.getProductNames();
            Assertions.assertThat(matchingProducts).hasSize(9);
        }
    }
}