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

    public static void run(JSONObject userData) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            try {
                page.navigate("https://www.rockland.de/aktionen/60-seconds-spezial.html");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Auswahl bestätigen")).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Jetzt registrieren!")).click();
                page.getByPlaceholder("Vorname*").click();
                page.getByPlaceholder("Vorname*").fill(userData.getString("vorname"));
                page.getByPlaceholder("Nachname*").click();
                page.getByPlaceholder("Nachname*").fill(userData.getString("nachname"));
                page.getByPlaceholder("Telefon*").click();
                page.getByPlaceholder("Telefon*").fill(userData.getString("telefon"));
                page.getByPlaceholder("E-Mail*").click();
                page.getByPlaceholder("E-Mail*").fill(userData.getString("email"));
                page.getByPlaceholder("Wohnort*").click();
                page.getByPlaceholder("Wohnort*").fill(userData.getString("wohnort"));
                page.getByText("Hiermit stimme ich der Ü").click();
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Formular absenden!")).click();

                // Use getByRole to find the heading with the specified name
                Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Vielen Dank für die Teilnahme!"));
                // Perform checks or interactions with the heading element
                if (heading != null && heading.isVisible()) {
                    System.out.println("Erfolgreich teilgenommen.");
                } else {
                    System.out.println("Etwas ist schief gelaufen");
                }
                logger.info("Form successfully filled and submitted.");
            } catch (PlaywrightException e) {
                logger.error("Error interacting with web elements: {}", e.getMessage());
            }

            // Wait for the user to press Enter
            System.out.println("ENTER drücken um fortzusetzen...");
            try (Scanner scanner = new Scanner(System.in)) {
                scanner.nextLine();
            }

            // Clean up
            browser.close();
        } catch (Exception e) {
            logger.error("Error during automation: {}", e.getMessage());
        }
    }

    public static JSONObject readJsonFile(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(jsonContent);
    }

    public static void main(String[] args) {
        try {
            JSONObject userData = readJsonFile("src/main/resources/data.json");
            run(userData);
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage(), e);
        }
    }

}
