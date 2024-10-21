package com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class NoteSeleniumPageObject {

     private WebDriver driver;

     @FindBy(id = "note-title")
     public WebElement noteTitle;

     @FindBy(id="note-description")
     public WebElement noteDescription;

     @FindBy(id = "save-note")
     public WebElement noteSubmit;

     @FindBy(id = "add-note-btn")
     public WebElement addNoteBtn;

     @FindBy(id = "edit-note-1")
     public WebElement editBtnNote1;

     WebDriverWait wait;

     public NoteSeleniumPageObject(WebDriver driver) {
         this.driver = driver;
         PageFactory.initElements(driver, this);
         wait = new WebDriverWait(driver, 30);

     }

     public void setNoteTitle(String noteTitle) {
          this.noteTitle.sendKeys(noteTitle);
     }

     public String getNoteDescription() {
          List<WebElement> noteTitles = driver.findElements(By.cssSelector("[data-name='msgDesc']"));
          WebDriverWait wait = new WebDriverWait(driver, 30);

          if (!noteTitles.isEmpty()) {
               return noteTitles.get(0).getText();
          }
          return null;
     }

     public String getDisplayedNoteTitle() {
          wait.pollingEvery(Duration.ofMillis(500));

          try {
               // Wait for the presence of elements
               wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[data-name='msgTitle']")));

               // Then wait for at least one element to be visible
               WebElement visibleTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-name='msgTitle']")));

               return visibleTitle.getText();
          } catch (TimeoutException e) {
               System.out.println("Timed out waiting for note title to be visible");
               return null;
          } catch (StaleElementReferenceException e) {
               System.out.println("Element became stale while trying to get note title");
               return null;
          }
     }

     public void setNoteDescription(String noteDescription) {
          this.noteDescription.sendKeys(noteDescription);
     }

     public void submitForm() {
          noteSubmit.click();
     }

     public void clickAddNoteBtn() {
          wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-btn")));
          addNoteBtn.click();
     }

     public void clickEditBtnNote1() {
          editBtnNote1.click();
     }

     public void clickDelBtnNote(String noteTitle) {
         driver.findElement(By.xpath("//th[@data-name='msgTitle' and text()='" + noteTitle + "']/preceding-sibling::td//button[contains(@class, 'btn-danger')]")).click();
     }

     public void resetInputs() {
          this.noteTitle.clear();
          this.noteDescription.clear();
     }





}
