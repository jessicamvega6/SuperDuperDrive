package com.udacity.jwdnd.course1.cloudstorage.Selenium;

import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumCredentialsPageObject;
import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumLoginPageObject;
import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumSignupPageObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    private SeleniumLoginPageObject seleniumLoginPageObject;
    private SeleniumSignupPageObject signupPageObject;
    private SeleniumCredentialsPageObject seleniumCredentialsPageObject;

    String username;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:" + port + "/signup");
        wait = new WebDriverWait(driver, 10);

        seleniumLoginPageObject = new SeleniumLoginPageObject(driver);
        signupPageObject = new SeleniumSignupPageObject(driver);
        seleniumCredentialsPageObject = new SeleniumCredentialsPageObject(driver);

        username = createUniqueUser();
        seleniumLoginPageObject.login(username, "password123");

        // Navigate to the Notes tab
        clickCredentialsTab();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void testCreateCredentialsAndPwIsEncrpted() {
        String url = "https://example.com";
        String username = "testuser";
        String password = "testpassword";

        seleniumCredentialsPageObject.clickAddCredentialButton();
        addCredentials(url, username, password);

        // Navigate to the Creds tab
        clickCredentialsTab();

        // Verify the credentials are displayed
        assertEquals(seleniumCredentialsPageObject.getCredentialUrl(0), url);
        assertEquals(seleniumCredentialsPageObject.getCredentialUsername(0), username);

        // Verify the displayed password is encrypted (not equal to the original password)
        String displayedPassword = seleniumCredentialsPageObject.getCredentialPassword(0);
        assertNotEquals(displayedPassword, password);
        assertFalse(displayedPassword.isEmpty());
    }

    @Test
    public void testEditCredentialsAndPwIsDecrypted() {
        String url = "https://example.com";
        String username = "testuser";
        String password = "testpassword";

        // Edited the credentials
        String newUrl = "https://newexample.com";
        String newUsername = "newuser";
        String newPassword = "newpassword";

        // Add the credentials to the page
        seleniumCredentialsPageObject.clickAddCredentialButton();
        addCredentials(url, username, password);

        // Navigate to the Creds tab
        clickCredentialsTab();

        // Verify the credentials are displayed
        assertEquals(seleniumCredentialsPageObject.getCredentialUrl(0), url);
        assertEquals(seleniumCredentialsPageObject.getCredentialUsername(0), username);

        // Edit the current Credentials
        seleniumCredentialsPageObject.clickEditCredential(0);
        addCredentials(newUrl, newUsername, newPassword);

        // Navigate to the Creds tab
        clickCredentialsTab();

        // Verify the changes are displayed
        assertEquals(seleniumCredentialsPageObject.getCredentialUrl(0), newUrl);
        assertEquals(seleniumCredentialsPageObject.getCredentialUsername(0), newUsername);
        assertNotEquals(seleniumCredentialsPageObject.getCredentialPassword(0), newPassword);

    }

    @Test
    public void testDeleteCredentials() {
        String url = "https://example.com";
        String username = "testuser";
        String password = "testpassword";
        seleniumCredentialsPageObject.clickAddCredentialButton();
        addCredentials(url, username, password);

        // Navigate to the Creds tab
        clickCredentialsTab();

        seleniumCredentialsPageObject.clickDeleteCredential(0);

        // Navigate to the Creds tab
        clickCredentialsTab();
        assertNull(seleniumCredentialsPageObject.getCredentialUrl(0));
        assertNull(seleniumCredentialsPageObject.getCredentialPassword(0));
        assertNull(seleniumCredentialsPageObject.getCredentialUsername(0));
    }


    private void addCredentials(String url, String username, String password) {
       // seleniumCredentialsPageObject.clickAddCredentialButton();
        seleniumCredentialsPageObject.waitForModalToBeVisible();

        seleniumCredentialsPageObject.setCredentialUrl(url);
        seleniumCredentialsPageObject.setCredentialUsername(username);
        seleniumCredentialsPageObject.setCredentialPassword(password);
        seleniumCredentialsPageObject.submitCredentialForm();

        seleniumCredentialsPageObject.waitForModalToBeInvisible();
    }

    

    private String createUniqueUser() {
        String username = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "password123";
        createUser("Test", "User", username, password);
        return username;
    }

    private void createUser(String firstname, String lastname, String username, String password) {
        signupPageObject.setFirstName(firstname);
        signupPageObject.setLastName(lastname);
        signupPageObject.setUsername(username);
        signupPageObject.setInputPassword(password);
        signupPageObject.submitForm();
        signupPageObject.clickLoginButton();
    }


    private void clickCredentialsTab() {
        // Wait for the modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));

        // Ensure we're on the Notes tab
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
        tab.click();
    }

}
