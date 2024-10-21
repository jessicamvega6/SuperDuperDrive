package com.udacity.jwdnd.course1.cloudstorage.Selenium;

import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.SeleniumSignupPageObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpTest {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;

    private SeleniumSignupPageObject signupPageObject;

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @BeforeEach
    void setUpSelenium() {
        driver.get("http://localhost:" + port + "/signup");
        signupPageObject = new SeleniumSignupPageObject(driver);
    }

    @Test
    void testFirstNameInput_returnsExpectedResult() {
        String expectedName = "Jessica";
        signupPageObject.setFirstName("Jessica");
        assertEquals(expectedName, signupPageObject.getFirstName() );
    }

    @Test
    void testLastNameInput_returnsExpectedResult() {
        String expectedName = "Vega";
        signupPageObject.setLastName("Vega");
        assertEquals(expectedName, signupPageObject.getLastName() );
    }

    @Test
    void testInputUsername_returnsExpectedResult() {
        String expectedName = "Vega2";
        signupPageObject.setUsername("Vega2");
        assertEquals(expectedName, signupPageObject.getUsername() );
    }

    @Test
    void testInputPassword_returnsExpectedResult() {
        String expectedPwd = "Vega2";
        signupPageObject.setInputPassword("Vega2");
        assertEquals(expectedPwd, signupPageObject.getInputPassword() );
    }

    @Test
    void testSubmit_returnsSuccessResult() {
        signupPageObject.setFirstName("Jessica");
        signupPageObject.setLastName("Vega");
        signupPageObject.setUsername("Vega2_" + System.currentTimeMillis());
        signupPageObject.setInputPassword("Vega2");
        signupPageObject.submitForm();

        assertEquals("You successfully signed up! Please continue to the login page.",
                signupPageObject.getSuccessMsg());
    }

    @Test
    void testSubmit_noDuplicateUsers_returnsErrorResult() {
        signupPageObject.setFirstName("Jessica");
        signupPageObject.setLastName("Vega");
        signupPageObject.setUsername("Vega2");
        signupPageObject.setInputPassword("Vega2");
        signupPageObject.submitForm();
        driver.get("http://localhost:" + port + "/signup");

        signupPageObject.setFirstName("Jessica");
        signupPageObject.setLastName("Vega");
        signupPageObject.setUsername("Vega2");
        signupPageObject.setInputPassword("Vega2");
        signupPageObject.submitForm();
        assertEquals("Username is already taken", signupPageObject.getErrorsMsg());
    }

    @Test
    void testSignUp_noErrorMsgOnPageLoad_returnsElementNotDisplayed() {
        boolean displayed = signupPageObject.isErrorMsgDisplayed();
        assertFalse(displayed);
    }

}
