package com.serenitydojo.playwright.toolshop.constants;

/**
 * URL path constants for the application
 * Base URLs come from Maven profiles, paths are defined here
 */
public final class Urls {

    public static final String SUT_URL = System.getProperty("SUT_URL");
    public static final String LOGIN_PATH = SUT_URL + "/auth/login";
    public static final String REGISTER_PATH = SUT_URL + "/auth/register";
    public static final String CONTACT_PATH = SUT_URL + "/contact";
    public static final String PRODUCTS_PATH = SUT_URL + "/products";
    public static final String CART_PATH = SUT_URL + "/checkout";
    public static final String PROFILE_PATH = SUT_URL + "/account/profile";

    public static final String SUT_API_URL = System.getProperty("SUT_API_URL");
    public static final String API_USERS_PATH = SUT_API_URL + "/users";
    public static final String API_PRODUCTS_PATH = SUT_API_URL + "/products";
    public static final String API_LOGIN_PATH = SUT_API_URL + "/users/login";
    public static final String API_REGISTER_PATH = SUT_API_URL + "/users/register";
    public static final String API_CART_PATH = SUT_API_URL + "/carts";

}