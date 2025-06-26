package com.serenitydojo.playwright.toolshop.tests.purchase;

import com.serenitydojo.playwright.toolshop.fixtures.BaseTest;
import com.serenitydojo.playwright.toolshop.pages.catalog.*;
import com.serenitydojo.playwright.toolshop.pages.catalog.workflow.Authentication;
import com.serenitydojo.playwright.toolshop.pages.catalog.workflow.Purchase;
import com.serenitydojo.playwright.toolshop.api.models.User;
import com.serenitydojo.playwright.toolshop.fixtures.FinalScreenshot;
import com.serenitydojo.playwright.toolshop.fixtures.TracingManager;
import com.serenitydojo.playwright.toolshop.pages.common.NavBar;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Purchase")
@Feature("Purchase")
public class PurchaseTest extends BaseTest implements TracingManager {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    ShoppingCart shoppingCart;

    Authentication authentication;
    Purchase purchases;

    @BeforeEach
    void openHomePage() {
        navBar.openHomePage();
    }

    @BeforeEach
    void setUp() {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        shoppingCart = new ShoppingCart(page);

        authentication = new Authentication(page);
        purchases = new Purchase(page);


    }

    @Story("Purchase items")
    @Nested
    class WhenPurchasingItems {

        @Test
        @DisplayName("Purchasing a number of items after logging on")
        void whenPurchasingANumberOfItemsAfterLoggingOn() {
            // Given Sharon has an account
            User sharon = authentication.registerUserCalled("Sharon");
            // And Sharon has logged on
            authentication.loginAs(sharon);

            // When she adds 2 items to the cart
            purchases.addProductToCart("Combination Pliers", 2);
            purchases.addProductToCart("Claw Hammer with Fiberglass Handle", 1);

            // And she checks out
            purchases.checkOutCart();
            purchases.proceedToCheckoutAfterAuthentication();
            purchases.confirmAddress();

            // And she completes the purchase
            purchases.choosePaymentMethod("Cash on Delivery");

            // Then she should receive a thank you message
            Assertions.assertThat(purchases.confirmationMessage()).contains("Thanks for your order!");
        }

        @Test
        @DisplayName("Logging on during the purchase process")
        void whenLoggingOnDuringThePurchaseProcess() {
            // Given Sharon has an account
            User sharon = authentication.registerUserCalled("Sharon");

            // When she adds 2 items to the cart
            purchases.addProductToCart("Combination Pliers", 2);
            purchases.addProductToCart("Claw Hammer with Fiberglass Handle", 1);

            // And she checks out
            purchases.checkOutCart();

            // And she logs on
            authentication.loginAs(sharon);
            purchases.checkOutCart(); // again

            purchases.proceedToCheckoutAfterAuthentication();
            purchases.confirmAddress();

            // And she completes the purchase
            purchases.choosePaymentMethod("Cash on Delivery");

            // Then she should receive a thank you message
            Assertions.assertThat(purchases.confirmationMessage()).contains("Thanks for your order!");
        }

    }
}