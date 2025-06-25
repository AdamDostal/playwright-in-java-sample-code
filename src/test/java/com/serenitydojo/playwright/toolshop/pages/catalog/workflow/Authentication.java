package com.serenitydojo.playwright.toolshop.pages.catalog.workflow;

import com.microsoft.playwright.Page;
import com.serenitydojo.playwright.toolshop.api.clients.UserAPIClient;
import com.serenitydojo.playwright.toolshop.api.models.User;
import com.serenitydojo.playwright.toolshop.pages.login.LoginPage;

public class Authentication {

    private final UserAPIClient userAPI;
    private final LoginPage loginPage;

    public Authentication(Page page) {
        this.userAPI = new UserAPIClient(page);
        this.loginPage = new LoginPage(page);
    }

    public User registerUserCalled(String firstName) {
        User someUser = User.randomUserNamed(firstName);
        userAPI.registerUser(someUser);
        return someUser;
    }

    public void loginAs(User user) {
        loginPage.open();
        loginPage.loginAs(user);
    }
}
