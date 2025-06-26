package framework.pages.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import framework.api.models.User;

import static framework.constants.Endpoints.API_USER_LOGIN;
import static framework.constants.Endpoints.UI_LOGIN_PATH;

public class Login {
    private final Page page;

    public Login(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate(UI_LOGIN_PATH);
    }

    public void loginAs(User user) {
        page.getByPlaceholder("Your email").fill(user.email());
        page.getByPlaceholder("Your password").fill(user.password());

        page.waitForResponse("**" + API_USER_LOGIN + "**", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        });
    }

    public String title() {
        return page.getByTestId("page-title").textContent();
    }

    public String loginErrorMessage() {
        return page.getByTestId("login-error").textContent();
    }
}
