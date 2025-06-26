package framework.config;

import framework.enums.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static final String DEFAULT_PROPERTIES_FILE = "test.properties";
    private static final AtomicBoolean configurationLogged = new AtomicBoolean(false);

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

        } catch (IOException e) {
            logger.error("Error loading properties file: {}", propertiesFile, e);
        }
    }

    /**
     * Log the complete test configuration setup - only once across all test classes
     */
    public static void logTestConfiguration() {
        // Use AtomicBoolean to ensure this runs only once even in parallel execution
        if (configurationLogged.compareAndSet(false, true)) {
            logger.info("=== Test Configuration Setup ===");
            logger.info("Browser Type: {}", getBrowserType());
            logger.info("Headless Mode: {}", isBrowserHeadless());
            logger.info("Page Timeout: {}ms", getPageTimeout());
            logger.info("Slow Motion: {}ms", getBrowserSlowMotion());
            logger.info("Browser Args: {}", getBrowserArgs().isEmpty() ? "none" : getBrowserArgs());
            logger.info("Tracing Enabled: {}", isTracingEnabled());
            logger.info("Screenshots Enabled: {}", isScreenshotEnabled());
            logger.info("================================");
        }
    }

    public static BrowserType getBrowserType() {
        String browserName = getProperty("browser.type", "chrome");
        return BrowserType.fromString(browserName);
    }

    public static boolean isBrowserHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    /**
     * Get page action timeout (clicks, waits, etc.)
     */
    public static int getPageTimeout() {
        return Integer.parseInt(getProperty("browser.page.timeout", "30000"));
    }

    public static int getBrowserSlowMotion() {
        return Integer.parseInt(getProperty("browser.slow.motion", "0"));
    }

    public static List<String> getBrowserArgs() {
        String args = getProperty("browser.args", "");
        if (args.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(args.split("\\s*,\\s*"));
    }

    public static boolean isTracingEnabled() {
        return Boolean.parseBoolean(getProperty("tracing.enabled", "false"));
    }

    public static boolean isScreenshotEnabled() {
        return Boolean.parseBoolean(getProperty("screenshot.enabled", "true"));
    }

    private static String getProperty(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }

        return properties.getProperty(key, defaultValue);
    }
}