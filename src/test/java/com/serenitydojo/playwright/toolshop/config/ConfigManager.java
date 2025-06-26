package com.serenitydojo.playwright.toolshop.config;

import com.serenitydojo.playwright.toolshop.enums.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    // Browser Configuration Methods
    public static BrowserType getBrowserType() {
        String browserName = getProperty("browser.type", "chrome");
        logger.info("Browser type configured: {}", browserName);
        return BrowserType.fromString(browserName);
    }

    public static boolean isBrowserHeadless() {
        boolean headless = Boolean.parseBoolean(getProperty("browser.headless", "false"));
        logger.info("Browser headless mode: {}", headless);
        return headless;
    }

    public static int getBrowserTimeout() {
        int timeout = Integer.parseInt(getProperty("browser.timeout", "30000"));
        logger.info("Browser timeout: {}ms", timeout);
        return timeout;
    }

    public static int getViewportWidth() {
        return Integer.parseInt(getProperty("browser.viewport.width", "1920"));
    }

    public static int getViewportHeight() {
        return Integer.parseInt(getProperty("browser.viewport.height", "1080"));
    }

    public static List<String> getBrowserArgs() {
        String args = getProperty("browser.args", "");
        if (args.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(args.split("\\s*,\\s*"));
    }

    // Test Configuration Methods
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    public static boolean isScreenshotOnSuccess() {
        return Boolean.parseBoolean(getProperty("screenshot.on.success", "false"));
    }

    public static int getTestRetryCount() {
        return Integer.parseInt(getProperty("test.retry.count", "1"));
    }

    // Logging Configuration Methods
    public static String getLogLevel() {
        return getProperty("log.level", "INFO");
    }

    public static boolean isLogBrowserConsole() {
        return Boolean.parseBoolean(getProperty("log.browser.console", "true"));
    }

    private static String getProperty(String key, String defaultValue) {
        // Check system properties first (allows runtime override)
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            logger.debug("Using system property for {}: {}", key, systemProperty);
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