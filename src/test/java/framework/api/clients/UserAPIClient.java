package framework.api.clients;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import framework.api.models.User;

import static framework.constants.Endpoints.API_USER_REGISTER;

public class UserAPIClient {
    private final Page page;

    private static final String REGISTER_USER_PATH = API_USER_REGISTER;

    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
        var response = page.request().post(
                REGISTER_USER_PATH,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setIgnoreHTTPSErrors(true));
        if (response.status() != 201) {
            throw new IllegalStateException("Could not create user: " + response.text());
        }
    }
}
