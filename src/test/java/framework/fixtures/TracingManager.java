package framework.fixtures;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Paths.get;

public class TracingManager {

    private static final Logger logger = LoggerFactory.getLogger(TracingManager.class);

    public static void startTracing(BrowserContext browserContext) {
        try {
            browserContext.tracing().start(
                    new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true)
                            .setSources(true)
            );
        } catch (Exception e) {
            logger.error("Failed to start tracing", e);
        }
    }

    public static void stopTracing(BrowserContext browserContext, TestInfo testInfo) {
        try {
            String traceName = testInfo.getDisplayName().replace(" ", "-").toLowerCase();
            String traceDir = "target/traces";
            String tracePath = String.format("%s/trace-%s.zip", traceDir, traceName);

            Path traceDirectory = get(traceDir);
            if (!exists(traceDirectory)) {
                createDirectories(traceDirectory);
            }

            browserContext.tracing().stop(
                    new Tracing.StopOptions()
                            .setPath(get(tracePath))
            );
        } catch (Exception e) {
            logger.error("Failed to stop tracing for test: {}", testInfo.getDisplayName(), e);
        }
    }
}