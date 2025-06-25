package com.serenitydojo.playwright.toolshop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager to load and provide access to test properties
 * Supports different environments and easy configuration changes
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static final String DEFAULT_PROPERTIES_FILE = "test.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String propertiesFile = System.getProperty("test.properties", DEFAULT_PROPERTIES_FILE);

        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {

            if (inputStream == null) {
                logger.warn("Properties file '{}' not found, using default values", propertiesFile);
                return;
            }

            properties.load(inputStream);
            logger.info("Loaded properties from: {}", propertiesFile);

        } catch (IOException e) {
            logger.error("Error loading properties file: {}", propertiesFile, e);
        }
    }

    public static String getBaseUrl() {
        return getProperty("base.url", "https://practicesoftwaretesting.com");
    }

    public static String getApiBaseUrl() {
        return getProperty("api.base.url", getBaseUrl() + "/api");
    }

    public static String getLoginUrl() {
        return getProperty("login.url", getBaseUrl() + "/auth/login");
    }

    public static String getRegisterUrl() {
        return getProperty("register.url", getBaseUrl() + "/auth/register");
    }

    public static String getContactUrl() {
        return getProperty("contact.url", getBaseUrl() + "/contact");
    }

    public static String getProductsUrl() {
        return getProperty("products.url", getBaseUrl() + "/products");
    }

    public static String getCartUrl() {
        return getProperty("cart.url", getBaseUrl() + "/checkout");
    }

    public static String getApiUsersUrl() {
        return getProperty("api.users.url", getApiBaseUrl() + "/users");
    }

    public static String getApiProductsUrl() {
        return getProperty("api.products.url", getApiBaseUrl() + "/products");
    }

    public static String getApiLoginUrl() {
        return getProperty("api.login.url", getApiBaseUrl() + "/users/login");
    }

    public static boolean isBrowserHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    public static int getBrowserTimeout() {
        return Integer.parseInt(getProperty("browser.timeout", "30000"));
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    private static String getProperty(String key, String defaultValue) {
        // Check system properties first (allows runtime override)
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }

        // Then check loaded properties file
        return properties.getProperty(key, defaultValue);
    }

    // Private constructor to prevent instantiation
    private ConfigManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}