package com.serenitydojo.playwright.toolshop.fixtures;

import org.junit.jupiter.api.AfterEach;

public interface FinalScreenshot {

    @AfterEach
    default void takeScreenshot()  {
        // 'this' is the test class, which extends BaseTest and has 'page'
        ScreenshotManager.takeScreenshot(((BaseTest) this).page, "Final screenshot");
    }
}
