package framework.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;
import framework.enums.BrowserType;
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
     *
     * @param playwright Playwright instance
     * @return Browser instance
     */
    public static Browser createBrowser(Playwright playwright) {
        BrowserType browserType = ConfigManager.getBrowserType();
        boolean headless = ConfigManager.isBrowserHeadless();
        int timeout = ConfigManager.getBrowserTimeout();
        int slowMotion = ConfigManager.getBrowserSlowMotion();
        List<String> browserArgs = ConfigManager.getBrowserArgs();

        logger.info("Creating {} browser (headless: {}, timeout: {}ms, slowMotion: {}ms, args: {})",
                browserType, headless, timeout, slowMotion, browserArgs);

        com.microsoft.playwright.BrowserType.LaunchOptions launchOptions = new com.microsoft.playwright.BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setTimeout(timeout)
                .setSlowMo(slowMotion);

        if (!browserArgs.isEmpty()) {
            launchOptions.setArgs(browserArgs);
        }

        Browser browser = switch (browserType) {
            case CHROME -> {
                logger.info("Launching Chrome browser with timeout: {}ms, slowMotion: {}ms", timeout, slowMotion);
                yield playwright.chromium().launch(launchOptions);
            }
            case FIREFOX -> {
                logger.info("Launching Firefox browser with timeout: {}ms, slowMotion: {}ms", timeout, slowMotion);
                yield playwright.firefox().launch(launchOptions);
            }
            case SAFARI -> {
                logger.info("Launching Safari browser with timeout: {}ms, slowMotion: {}ms", timeout, slowMotion);
                if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                    logger.warn("Safari is only supported on macOS. Falling back to Chrome.");
                    yield playwright.chromium().launch(launchOptions);
                }
                yield playwright.webkit().launch(launchOptions);
            }
            case EDGE -> {
                logger.info("Launching Edge browser with timeout: {}ms, slowMotion: {}ms", timeout, slowMotion);
                // Edge uses Chromium engine
                yield playwright.chromium().launch(launchOptions.setChannel("msedge"));
            }
        };

        logger.info("Browser created successfully: {} with timeout: {}ms, slowMotion: {}ms", browserType, timeout, slowMotion);
        return browser;
    }

    /**
     * Create browser context with default options including video recording if enabled
     *
     * @param browser Browser instance
     * @return BrowserContext instance
     */
    public static BrowserContext createBrowserContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        // Add video recording if enabled
        if (ConfigManager.isBrowserVideoEnabled()) {
            contextOptions.setRecordVideoDir(java.nio.file.Paths.get("target/videos"));
            logger.info("Video recording enabled - videos will be saved to target/videos");
        }

        BrowserContext context = browser.newContext(contextOptions);

        logger.debug("Browser context created successfully (video: {})", ConfigManager.isBrowserVideoEnabled());
        return context;
    }
}