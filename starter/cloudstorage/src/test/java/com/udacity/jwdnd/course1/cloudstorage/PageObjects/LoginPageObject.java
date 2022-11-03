package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPageObject {

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;
    @FindBy(id = "inputPassword")
    private WebElement passwordInput;
    @FindBy(id = "login-button")
    private WebElement loginBtn;
    @FindBy(id = "signup-link")
    private WebElement signupBtn;

    public LoginPageObject(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void mockLogin(String username, String password){
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.loginBtn.click();
    }

    public void goToSignUpPage(){
        this.signupBtn.click();
    }
}
