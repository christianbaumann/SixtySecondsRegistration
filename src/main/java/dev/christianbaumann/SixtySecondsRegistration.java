package dev.christianbaumann;

import com.microsoft.playwright.*;

public class SixtySecondsRegistration {

    public static void run() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://example.com");

            // Perform automation tasks here
            System.out.println("Page title: " + page.title());

            // Clean up
            browser.close();
        }
    }

    public static void main(String[] args) {
        run();
    }

}
