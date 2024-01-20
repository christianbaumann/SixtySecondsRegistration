package dev.christianbaumann;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class SixtySecondsRegistration {

    private static final Logger logger = LoggerFactory.getLogger(SixtySecondsRegistration.class);

    private static void navigateToRegistrationPage(Page page, JSONObject config) {
        page.navigate(config.getString("url"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Auswahl bestätigen")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Jetzt registrieren!")).click();
    }

    private static void fillRegistrationForm(Page page, JSONObject userData) {
        page.getByPlaceholder("Vorname*").fill(userData.getString("vorname"));
        page.getByPlaceholder("Nachname*").fill(userData.getString("nachname"));
        page.getByPlaceholder("Telefon*").fill(userData.getString("telefon"));
        page.getByPlaceholder("E-Mail*").fill(userData.getString("email"));
        page.getByPlaceholder("Wohnort*").fill(userData.getString("wohnort"));
        page.getByText("Hiermit stimme ich der Ü").click();
    }

    private static void submitForm(Page page) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Formular absenden!")).click();
    }

    private static void verifySubmission(Page page) {
        Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Vielen Dank für die Teilnahme!"));
        if (heading != null && heading.isVisible()) {
            logger.info("Erfolgreich teilgenommen.");
        } else {
            logger.error("Etwas ist schief gelaufen");
        }
    }

    private static void waitForUserInput() {
        System.out.println("ENTER drücken um fortzusetzen...");
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
        }
    }

    public static void run(JSONObject userData, JSONObject config) {
        Browser browser = null;
        try (Playwright playwright = Playwright.create()) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(config.getBoolean("headless")));
            Page page = browser.newPage();

            try {
                navigateToRegistrationPage(page, config);
                fillRegistrationForm(page, userData);
                submitForm(page);
                verifySubmission(page);
            } catch (PlaywrightException e) {
                logger.error("Error interacting with web elements: {}", e.getMessage());
            }

            waitForUserInput();

        } catch (PlaywrightException e) {
            logger.error("Error interacting with web elements: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error during automation: {}", e.getMessage());
        } finally {
            if (browser != null) {
                browser.close();
            }
        }
    }

    public static JSONObject readJsonFile(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(jsonContent);
    }

    public static void main(String[] args) {
        try {
            JSONObject userData = readJsonFile("src/main/resources/data.json");
            JSONObject config = readJsonFile("src/main/resources/config.json");
            run(userData, config);
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage(), e);
        }
    }

}
