package framework.config;

import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import framework.enums.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);

    public static Browser createBrowser(Playwright playwright) {
        BrowserType browserType = ConfigManager.getBrowserType();
        boolean headless = ConfigManager.isBrowserHeadless();
        int slowMotion = ConfigManager.getBrowserSlowMotion();
        List<String> browserArgs = ConfigManager.getBrowserArgs();

        LaunchOptions launchOptions = new LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMotion);

        if (!browserArgs.isEmpty()) {
            launchOptions.setArgs(browserArgs);
        }

        return switch (browserType) {
            case CHROME -> playwright.chromium().launch(launchOptions);
            case FIREFOX -> playwright.firefox().launch(launchOptions);
            case SAFARI -> {
                if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                    logger.warn("Safari is only supported on macOS. Falling back to Chrome.");
                    yield playwright.chromium().launch(launchOptions);
                }
                yield playwright.webkit().launch(launchOptions);
            }
            case EDGE -> playwright.chromium().launch(launchOptions.setChannel("msedge"));
            default -> throw new IllegalArgumentException("Unsupported browser type: " + browserType); //handles null and incorrect values
        };
    }

    public static BrowserContext createBrowserContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(ConfigManager.getPageTimeout());

        return context;
    }
}