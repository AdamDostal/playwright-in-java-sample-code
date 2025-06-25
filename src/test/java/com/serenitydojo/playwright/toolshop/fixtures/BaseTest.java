package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.serenitydojo.playwright.toolshop.utils.BrowserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(Playwright::create);

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            BrowserFactory.createBrowser(playwright.get())
    );

    protected BrowserContext browserContext;
    protected Page page;

    @BeforeEach
    void setUpBrowserContext() {
        browserContext = BrowserFactory.createBrowserContext(browser.get());
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
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
