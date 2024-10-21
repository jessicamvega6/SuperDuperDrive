package com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumCredentialsPageObject {

     private WebDriver driver;

     @FindBy(id = "add-cred-btn")
     public WebElement addCredentialButton;


     @FindBy(xpath = "//table[@id='credentialTable']//button[contains(@class, 'btn-success')]")
     public List<WebElement> editButtons;

     @FindBy(xpath = "//table[@id='credentialTable']//button[contains(@class, 'btn-danger')]")
     public List<WebElement> deleteButtons;

     @FindBy(xpath = "//th[starts-with(@data, 'url-')]")
     public List<WebElement> credentialUrls;

     @FindBy(xpath = "//td[starts-with(@data, 'username-')]")
     public List<WebElement> credentialUsernames;

     @FindBy(xpath = "//td[starts-with(@data, 'pw-')]")
     public List<WebElement> credentialPasswords;

     // Modal elements
     @FindBy(id = "credentialModal")
     public WebElement credentialModal;

     @FindBy(id = "credential-url")
     public WebElement credentialUrlInput;

     @FindBy(id = "credential-username")
     public WebElement credentialUsernameInput;

     @FindBy(id = "credential-password")
     public WebElement credentialPasswordInput;

     @FindBy(id = "save-cred")
     public WebElement saveChangesButton;

     WebDriverWait wait;

     public SeleniumCredentialsPageObject(WebDriver driver) {
         this.driver = driver;
         PageFactory.initElements(driver, this);
         wait = new WebDriverWait(driver, 30);

     }

     public void clickAddCredentialButton() {
          wait.until(ExpectedConditions.elementToBeClickable(By.id("add-cred-btn")));
          addCredentialButton.click();
     }

     public void setCredentialUrl(String url) {
          credentialUrlInput.clear();
          credentialUrlInput.sendKeys(url);
     }

     public void setCredentialUsername(String username) {
          credentialUsernameInput.clear();
          credentialUsernameInput.sendKeys(username);
     }

     public void setCredentialPassword(String password) {
          credentialPasswordInput.clear();
          credentialPasswordInput.sendKeys(password);
     }

     public void submitCredentialForm() {
          saveChangesButton.click();
     }

     public void clickEditCredential(int index) {
          editButtons.get(index).click();
     }

     public void clickDeleteCredential(int index) {
          try {
               wait.until(ExpectedConditions.visibilityOf(credentialUrls.get(index)));
          } catch (Exception e) {
               e.printStackTrace();
          }
          deleteButtons.get(index).click();
     }

     public String getCredentialUrl(int index) {

          if(credentialUrlsExist() && index < credentialUrls.size()) {
               wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                       By.xpath("//th[starts-with(@data, 'url-')]"), index));

               wait.until(ExpectedConditions.visibilityOf(credentialUrls.get(index)));

               return credentialUrls.get(index).getText();
          } else {
               return null;
          }
     }

     public String getCredentialUsername(int index) {
          if(credentialUsernamesExist() && index < credentialUsernames.size()) {
               wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                       By.xpath("//td[starts-with(@data, 'username-')]"), index));

               wait.until(ExpectedConditions.visibilityOf(credentialUrls.get(index)));

               return credentialUsernames.get(index).getText();
          } else {
               return null;
          }
     }

     public String getCredentialPassword(int index) {

          if(credentialPasswordsExist() && index < credentialPasswords.size()) {
               wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                       By.xpath("//td[starts-with(@data, 'pw-')]"), index));

               wait.until(ExpectedConditions.visibilityOf(credentialPasswords.get(index)));

               return credentialUrls.get(index).getText();
          } else {
               return null;
          }
     }

     public void waitForModalToBeVisible() {
          wait.until(ExpectedConditions.visibilityOf(credentialModal));
     }

     public void waitForModalToBeInvisible() {
          wait.until(ExpectedConditions.invisibilityOf(credentialModal));
     }

     public boolean credentialUsernamesExist() {
          return !credentialUsernames.isEmpty();
     }

     public boolean credentialPasswordsExist() {
          return !credentialPasswords.isEmpty();
     }

     public boolean credentialUrlsExist() {
          return !credentialUrls.isEmpty();
     }
}
