package framework.pages.catalog.workflow;

import com.microsoft.playwright.Page;
import framework.api.clients.UserAPIClient;
import framework.api.models.User;
import framework.pages.login.Login;

public class Authentication {

    private final UserAPIClient userAPI;
    private final Login login;

    public Authentication(Page page) {
        this.userAPI = new UserAPIClient(page);
        this.login = new Login(page);
    }

    public User registerUserCalled(String firstName) {
        User someUser = User.randomUser().withFirstName(firstName);
        userAPI.registerUser(someUser);
        return someUser;
    }

    public void loginAs(User user) {
        login.open();
        login.loginAs(user);
    }
}
