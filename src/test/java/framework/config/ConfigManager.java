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

/**
 * Configuration manager to load and provide access to test properties
 * Only includes methods for properties actually defined in test.properties
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

        } catch (IOException e) {
            logger.error("Error loading properties file: {}", propertiesFile, e);
        }
    }

    // Browser Configuration Methods - only for properties that exist in test.properties
    public static BrowserType getBrowserType() {
        String browserName = getProperty("browser.type", "chrome");
        logger.info("Browser: {}", browserName);
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

    public static int getBrowserSlowMotion() {
        int slowMotion = Integer.parseInt(getProperty("browser.slow.motion", "0"));
        logger.info("Browser slow motion: {}ms", slowMotion);
        return slowMotion;
    }

    public static boolean isBrowserVideoEnabled() {
        boolean video = Boolean.parseBoolean(getProperty("browser.video", "false"));
        logger.info("Browser video recording: {}", video);
        return video;
    }

    public static List<String> getBrowserArgs() {
        String args = getProperty("browser.args", "");
        if (args.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(args.split("\\s*,\\s*"));
    }

    // Tracing Configuration Methods - only for properties that exist
    public static boolean isTracingEnabled() {
        boolean enabled = Boolean.parseBoolean(getProperty("playwright.tracing.enabled", "false"));
        logger.info("Playwright tracing enabled: {}", enabled);
        return enabled;
    }

    // Screenshot Configuration Methods - only for properties that exist
    public static boolean isScreenshotEnabled() {
        boolean enabled = Boolean.parseBoolean(getProperty("screenshot.enabled", "true"));
        logger.info("Screenshots enabled: {}", enabled);
        return enabled;
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
}