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

public class SixtySecondsRegistration {

    private static final Logger logger = LoggerFactory.getLogger(SixtySecondsRegistration.class);

    /**
     * Navigates to the registration page and starts the registration process.
     *
     * @param page   The Playwright page object
     * @param config The configuration JSON object
     */
    private static void navigateToRegistrationPage(Page page, JSONObject config) {
        // Navigate to the URL specified in the config
        page.navigate(config.getString("url"));

        // Interact with specific elements to start the registration process
        page.locator("#accept").click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Jetzt registrieren!")).click();
    }

    /**
     * Fills the registration form with the user's data.
     *
     * @param page     The Playwright page object
     * @param userData The user data JSON object
     */
    private static void fillRegistrationForm(Page page, JSONObject userData) {
        // Fill each field in the form using the user data
        page.getByPlaceholder("Vorname*").fill(userData.getString("vorname"));
        page.getByPlaceholder("Nachname*").fill(userData.getString("nachname"));
        page.getByPlaceholder("Telefon*").fill(userData.getString("telefon"));
        page.getByPlaceholder("E-Mail*").fill(userData.getString("email"));
        page.getByPlaceholder("Wohnort*").fill(userData.getString("wohnort"));
        page.getByText("Hiermit stimme ich der Ü").click();
    }

    /**
     * Submits the registration form.
     *
     * @param page The Playwright page object
     */
    private static void submitForm(Page page) {
        // Submit the form by clicking the submission button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Formular absenden!")).click();
    }

    /**
     * Verifies if the submission was successful.
     *
     * @param page The Playwright page object
     */
    private static void verifySubmission(Page page) {
        // Check for a specific element that confirms successful submission
        Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Vielen Dank für die Teilnahme!"));
        if (heading != null && heading.isVisible()) {
            logger.info("Erfolgreich teilgenommen.");
        } else {
            logger.error("Etwas ist schief gelaufen");
        }
    }

    /**
     * Runs the registration automation process.
     *
     * @param userData The user data JSON object
     * @param config   The configuration JSON object
     */
    public static void run(JSONObject userData, JSONObject config) {
        // Initialize resources and perform the registration steps
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

        } catch (PlaywrightException e) {
            logger.error("Error interacting with web elements: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error during automation: {}", e.getMessage());
        } finally {
            // Ensure the browser is closed even if an exception occurs
            if (browser != null) {
                browser.close();
            }
        }
    }

    /**
     * Reads and parses a JSON file.
     *
     * @param filePath The file path of the JSON file
     * @return JSONObject parsed from the file
     * @throws IOException if an I/O error occurs reading the file
     */
    public static JSONObject readJsonFile(String filePath) throws IOException {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONObject(jsonContent);
        } catch (org.json.JSONException e) {
            logger.error("Error parsing JSON file: {}", e.getMessage(), e);
            throw new IOException("JSON parsing error in file: " + filePath, e);
        }
    }

    /**
     * The main method to run the automation script.
     *
     * @param args Command line arguments (not used)
     */
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
