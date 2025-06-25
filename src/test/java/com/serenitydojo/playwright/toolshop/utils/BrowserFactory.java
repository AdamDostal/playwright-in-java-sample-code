package com.serenitydojo.playwright.toolshop.utils;

import com.microsoft.playwright.*;
import com.serenitydojo.playwright.toolshop.enums.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Factory class for creating browsers based on configuration
 */
public class BrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);

    /**
     * Create a browser instance based on configuration
     * @param playwright Playwright instance
     * @return Browser instance
     */
    public static Browser createBrowser(Playwright playwright) {
        BrowserType browserType = ConfigManager.getBrowserType();
        boolean headless = ConfigManager.isBrowserHeadless();
        int timeout = ConfigManager.getBrowserTimeout();
        List<String> browserArgs = ConfigManager.getBrowserArgs();

        logger.info("Creating {} browser (headless: {}, timeout: {}ms, args: {})",
                browserType, headless, timeout, browserArgs);

        com.microsoft.playwright.BrowserType.LaunchOptions launchOptions = new com.microsoft.playwright.BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setTimeout(timeout);

        if (!browserArgs.isEmpty()) {
            launchOptions.setArgs(browserArgs);
        }

        Browser browser = switch (browserType) {
            case CHROME -> {
                logger.info("Launching Chrome browser");
                yield playwright.chromium().launch(launchOptions);
            }
            case FIREFOX -> {
                logger.info("Launching Firefox browser");
                yield playwright.firefox().launch(launchOptions);
            }
            case SAFARI -> {
                logger.info("Launching Safari browser");
                if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                    logger.warn("Safari is only supported on macOS. Falling back to Chrome.");
                    yield playwright.chromium().launch(launchOptions);
                }
                yield playwright.webkit().launch(launchOptions);
            }
            case EDGE -> {
                logger.info("Launching Edge browser");
                // Edge uses Chromium engine
                yield playwright.chromium().launch(launchOptions.setChannel("msedge"));
            }
        };

        logger.info("Browser created successfully: {}", browserType);
        return browser;
    }

    /**
     * Create browser context with default options
     * @param browser Browser instance
     * @return BrowserContext instance
     */
    public static BrowserContext createBrowserContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        BrowserContext context = browser.newContext(contextOptions);

        logger.debug("Browser context created successfully");
        return context;
    }
}