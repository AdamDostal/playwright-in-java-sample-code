package framework.constants;

public final class Endpoints {

    public static final String UI_SUT_URL = System.getProperty("SUT_URL");
    public static final String API_SUT_URL = System.getProperty("SUT_API_URL");

    public static final String UI_CONTACT_PATH = UI_SUT_URL + "/contact";
    public static final String UI_LOGIN_PATH = UI_SUT_URL + "/auth/login";

    public static final String API_PRODUCTS = "/products";
    public static final String API_PRODUCT_SEARCH = API_PRODUCTS + "/search";
    public static final String API_USERS = "/users";
    public static final String API_USER_LOGIN = API_USERS + "/login";
    public static final String API_USER_REGISTER = API_USERS + "/register";

}