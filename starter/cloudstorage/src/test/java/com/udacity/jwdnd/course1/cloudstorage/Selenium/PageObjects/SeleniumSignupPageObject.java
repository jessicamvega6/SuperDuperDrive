package com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SeleniumSignupPageObject {
    private WebDriver driver;


    @FindBy(id = "inputFirstName")
    private WebElement firstName;

    @FindBy(id = "inputLastName")
    private WebElement lastName;

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "buttonSignUp")
    private WebElement submitButton;

    @FindBy(id = "success-msg")
    private WebElement successMsg;

    @FindBy(id = "error-msg")
    private WebElement errorsMsg;

    @FindBy(id = "login-link")
    private WebElement loginLink;


    public SeleniumSignupPageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void setFirstName(String firstname) {
        this.firstName.sendKeys(firstname);
    }

    public String getFirstName() {
        return firstName.getAttribute("value");
    }

    public void setLastName(String lastname) {
        this.lastName.sendKeys(lastname);
    }

    public String getLastName() {
        return lastName.getAttribute("value");
    }

    public void setUsername(String username) {
        this.inputUsername.sendKeys(username);
    }

    public String getUsername() {
        return inputUsername.getAttribute("value");
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword.sendKeys(inputPassword);
    }

    public String getInputPassword() {
        return inputPassword.getAttribute("value");
    }

    public void submitForm() {
        submitButton.click();
    }

    public String getSuccessMsg() {
        return successMsg.getText();
    }

    public String getErrorsMsg() {
        return errorsMsg.getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void clickLoginButton() {
        loginLink.click();
    }

    public boolean isErrorMsgDisplayed() {
        return errorsMsg == null;
    }
}
