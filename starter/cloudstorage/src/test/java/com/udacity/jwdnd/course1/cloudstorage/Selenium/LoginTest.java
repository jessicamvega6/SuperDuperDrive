package com.udacity.jwdnd.course1.cloudstorage.Selenium;

import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumLoginPageObject;
import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumSignupPageObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @LocalServerPort
    private Integer port;

    private WebDriver driver;
    private SeleniumLoginPageObject seleniumLoginPageObject;
    private SeleniumSignupPageObject signupPageObject;
    private SeleniumHomePage homePage;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:" + port + "/signup");
        seleniumLoginPageObject = new SeleniumLoginPageObject(driver);
        signupPageObject = new SeleniumSignupPageObject(driver);
        homePage = new SeleniumHomePage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
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

    @Test
    void testURL_returnsLoginUrl() {
        driver.get("http://localhost:" + port + "/login");
        verifyCurrentUrlIsLogin();
    }

    @Test
    void testInputUsername_returnsExpectedResult() {
        String username = createUniqueUser();
        verifyCurrentUrlIsLogin();
        seleniumLoginPageObject.setUsername(username);
        assertEquals(username, seleniumLoginPageObject.getUsername());
    }

    @Test
    void testInputPassword_returnsExpectedResult() {
        createUniqueUser();
        verifyCurrentUrlIsLogin();
        String expectedPwd = "password123";
        seleniumLoginPageObject.setInputPassword(expectedPwd);
        assertEquals(expectedPwd, seleniumLoginPageObject.getInputPassword());
    }

    @Test
    void testSubmit_returnsSuccessResult() {
        String username = createUniqueUser();
        verifyCurrentUrlIsLogin();
        String expectedUrl = "http://localhost:" + port + "/home";
        seleniumLoginPageObject.login(username, "password123");
        assertEquals(expectedUrl, driver.getCurrentUrl());
    }


    @Test
    void testUnauthorizedUserAccess() {
        // Test access to home page (should redirect to login)
        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());
        assertEquals("http://localhost:" + port + "/login", driver.getCurrentUrl());
    }

    @Test
    void testSignUpLoginLogoutFlow() {
        String username = createUniqueUser();
        seleniumLoginPageObject.login(username, "password123");
        // Verify home page is accessible
        assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

        homePage.logout();

        // Verify home page is no longer accessible (should redirect to login)
        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());
        assertEquals("http://localhost:" + port + "/login", driver.getCurrentUrl());
    }

    private void verifyCurrentUrlIsLogin() {
        String expectedUrl = "http://localhost:" + port + "/login";
        assertEquals(expectedUrl, seleniumLoginPageObject.getCurrentUrl());
    }

}
