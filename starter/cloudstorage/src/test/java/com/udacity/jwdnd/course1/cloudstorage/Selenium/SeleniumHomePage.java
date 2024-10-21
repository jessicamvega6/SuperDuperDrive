package com.udacity.jwdnd.course1.cloudstorage.Selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SeleniumHomePage {
    private WebDriver driver;

    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    public SeleniumHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }
}