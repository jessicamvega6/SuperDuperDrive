package com.udacity.jwdnd.course1.cloudstorage.Selenium.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SeleniumLoginPageObject {

    private WebDriver driver;

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id =  "login-button")
    private WebElement loginButton;

    @FindBy(id = "error-msg")
    private WebElement errorsMsg;

    public SeleniumLoginPageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.driver = driver;
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
        loginButton.click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getErrorMsg() {
        return errorsMsg.getText();
    }

    public void login(String username, String password) {
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        loginButton.click();
    }

}
