package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Paths;

public interface TracingManager {

    @BeforeEach
    default void setupTrace() {
        ((BaseTest) this).browserContext.tracing().start(
                new com.microsoft.playwright.Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @AfterEach
    default void recordTrace(TestInfo testInfo) {
        String traceName = testInfo.getDisplayName().replace(" ","-").toLowerCase();
        ((BaseTest) this).browserContext.tracing().stop(
                new com.microsoft.playwright.Tracing.StopOptions()
                        .setPath(java.nio.file.Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }

}
