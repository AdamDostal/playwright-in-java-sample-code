package framework.tests.login;

import framework.api.clients.UserAPIClient;
import framework.api.models.User;
import framework.fixtures.BaseTest;
import framework.pages.login.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginRegisteredUserTest extends BaseTest {

    @Test
    @DisplayName("Should be able to login with a registered user")
    void should_login_with_registered_user() {
        // Register a user via the API
        User user = User.randomUser().withFirstName("Reg");
        ;
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        // Login via the login page
        Login login = new Login(page);
        login.open();
        login.loginAs(user);

        // Check that we are on the right account page
        assertThat(login.title()).isEqualTo("My account");
    }

    @Test
    @DisplayName("Should reject a user if they provide a wrong password")
    void should_reject_user_with_invalid_password() {
        User user = User.randomUser().withFirstName("Reg");
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        Login login = new Login(page);
        login.open();
        login.loginAs(user.withPassword("wrong-password"));

        assertThat(login.loginErrorMessage()).isEqualTo("Invalid email or password");
    }
}
