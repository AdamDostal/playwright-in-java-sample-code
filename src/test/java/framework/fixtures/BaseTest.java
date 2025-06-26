package framework.fixtures;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import framework.config.BrowserFactory;
import framework.config.ConfigManager;
import org.junit.jupiter.api.*;

public abstract class BaseTest {

    protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(Playwright::create);

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            BrowserFactory.createBrowser(playwright.get())
    );

    protected BrowserContext browserContext;
    protected Page page;

    @BeforeAll
    static void globalSetup() {
        ConfigManager.logTestConfiguration();
        playwright.get().selectors().setTestIdAttribute("data-test");
    }

    @BeforeEach
    void setUpBrowserContext() {
        browserContext = BrowserFactory.createBrowserContext(browser.get());
        page = browserContext.newPage();

        if (ConfigManager.isTracingEnabled()) {
            TracingManager.startTracing(browserContext);
        }
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        if (ConfigManager.isTracingEnabled()) {
            TracingManager.stopTracing(browserContext, testInfo);
        }

        if (ConfigManager.isScreenshotEnabled()) {
            ScreenshotManager.takeScreenshot(page, "Test completed");
        }

        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }
}