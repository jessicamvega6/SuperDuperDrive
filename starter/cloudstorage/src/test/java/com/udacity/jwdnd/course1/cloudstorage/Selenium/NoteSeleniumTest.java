package com.udacity.jwdnd.course1.cloudstorage.Selenium;

import com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects.NoteSeleniumPageObject;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteSeleniumTest {

    @LocalServerPort
    private Integer port;

    private WebDriver driver;
    private WebDriverWait wait;
    private SeleniumLoginPageObject seleniumLoginPageObject;
    private SeleniumSignupPageObject signupPageObject;
    private NoteSeleniumPageObject noteSeleniumPageObject;

    String username;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:" + port + "/signup");
        wait = new WebDriverWait(driver, 10);
        seleniumLoginPageObject = new SeleniumLoginPageObject(driver);
        signupPageObject = new SeleniumSignupPageObject(driver);
        noteSeleniumPageObject = new NoteSeleniumPageObject(driver);

        username = createUniqueUser();
        seleniumLoginPageObject.login(username, "password123");

        // Navigate to the Notes tab
        clickNotesTab();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testCreateNote() {
        String noteTitle = "Test Note " + UUID.randomUUID().toString().substring(0, 5);
        String noteDescription = "This is a test note description.";

        addNoteToPage(noteTitle, noteDescription);

        // Verify the note is displayed
        assertEquals(noteTitle, noteSeleniumPageObject.getDisplayedNoteTitle());
        assertEquals(noteDescription, noteSeleniumPageObject.getNoteDescription());
    }

    @Test
    void testEditNote() {
        String noteTitle = "Test Note " + UUID.randomUUID().toString().substring(0, 5);
        String noteDescription = "This is a test note description.";

        addNoteToPage(noteTitle, noteDescription);
        assertEquals(noteTitle, noteSeleniumPageObject.getDisplayedNoteTitle());        // Verify the note is displayed

        String editedTitle = "this is an edited";
        String editedDesc = "this is an edited description";

        noteSeleniumPageObject.clickEditBtnNote1();

        // Wait for the modal to appear and fill in the note details
        wait.until(ExpectedConditions.visibilityOf(noteSeleniumPageObject.noteTitle));
        noteSeleniumPageObject.resetInputs();
        noteSeleniumPageObject.setNoteTitle(editedTitle);
        noteSeleniumPageObject.setNoteDescription(editedDesc);

        // Save the note
        noteSeleniumPageObject.submitForm();
        clickNotesTab();

        // Verify the note is displayed
        assertEquals(editedTitle, noteSeleniumPageObject.getDisplayedNoteTitle());
        assertEquals(editedDesc, noteSeleniumPageObject.getNoteDescription());
    }

    @Test
    void testDeleteNote() {
        String noteTitle = "Test Note " + UUID.randomUUID().toString().substring(0, 5);
        String noteDescription = "This is a test note description.";

        addNoteToPage(noteTitle, noteDescription);
        assertEquals(noteTitle, noteSeleniumPageObject.getDisplayedNoteTitle());        // Verify the note is displayed

        //click delete btn
        noteSeleniumPageObject.clickDelBtnNote(noteTitle);
        //get back to notes tab
        clickNotesTab();

        // Verify the note is gone
        assertNull(noteSeleniumPageObject.getDisplayedNoteTitle());
        assertNull(noteSeleniumPageObject.getNoteDescription());

    }

    private void addNoteToPage(String noteTitle, String noteDesc) {
        // Click 'Add a New Note' button
        noteSeleniumPageObject.clickAddNoteBtn();

        // Wait for the modal to appear and fill in the note details
        wait.until(ExpectedConditions.visibilityOf(noteSeleniumPageObject.noteTitle));
        noteSeleniumPageObject.setNoteTitle(noteTitle);
        noteSeleniumPageObject.setNoteDescription(noteDesc);

        // Save the note
        noteSeleniumPageObject.submitForm();

        clickNotesTab();
    }

    private void clickNotesTab() {
        // Wait for the modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));

        // Ensure we're on the Notes tab
        WebElement notesTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
        notesTab.click();
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


}
