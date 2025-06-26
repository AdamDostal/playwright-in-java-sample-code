package framework.enums;

/**
 * Supported browser types for test execution
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    SAFARI("safari"),
    EDGE("edge");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    /**
     * Get BrowserType from string value
     *
     * @param browserName the browser name string
     * @return BrowserType enum value
     * @throws IllegalArgumentException if browser is not supported
     */
    public static BrowserType fromString(String browserName) {
        if (browserName == null || browserName.trim().isEmpty()) {
            return CHROME; // Default to Chrome
        }

        for (BrowserType type : BrowserType.values()) {
            if (type.browserName.equalsIgnoreCase(browserName.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unsupported browser: " + browserName +
                ". Supported browsers are: chrome, firefox, safari, edge");
    }

    @Override
    public String toString() {
        return browserName;
    }
}