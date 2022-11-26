package com.udacity.jwdnd.course1.cloudstorage;


import com.udacity.jwdnd.course1.cloudstorage.PageObjects.HomePageObject;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.LoginPageObject;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.SignUpPageObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTest {

    @LocalServerPort
    private int port;

    private static WebDriver webDriver;

    private static WebDriverWait webDriverWait;

    private static SignUpPageObject signUpPageObject;

    private static LoginPageObject loginPageObject;

    private static HomePageObject homePageObject;

    @BeforeAll
    public  static void beforAll() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll(){
        webDriver.quit();
    }

    @BeforeEach
    public  void beforeEach() throws InterruptedException{
        webDriver.get("http://localhost:"+this.port+"/sessions/signup");
        signUpPageObject = new SignUpPageObject(webDriver);
        loginPageObject = new LoginPageObject(webDriver);
        homePageObject= new HomePageObject(webDriver);
    }

    @Test
    public void testCreateCredential() throws InterruptedException{
        createCredential();

        Thread.sleep(2000);
        homePageObject.goToCredentialTab();

        assertEquals("http://testurl",homePageObject.getCredentialUrlString());
        assertEquals("test",homePageObject.getCredentialUsernameString());
        assertEquals("Test@123",homePageObject.getCredentialPwdString());

        homePageObject.deleteCredential();
        Thread.sleep(2000);
        homePageObject.logoff();
        Thread.sleep(2000);

    }

    @Test
    public void testEditCredential() throws InterruptedException{
        createCredential();

        Thread.sleep(2000);
        homePageObject.goToCredentialTab();
        Thread.sleep(1000);

        homePageObject.editCredential("http://testurl2","test1","Test@456");
        Thread.sleep(2000);
        homePageObject.goToCredentialTab();

        assertEquals("http://testurl2",homePageObject.getCredentialUrlString());
        assertEquals("test1",homePageObject.getCredentialUsernameString());
        assertEquals("Test@456",homePageObject.getCredentialPwdString());

        homePageObject.deleteCredential();
        Thread.sleep(2000);
        homePageObject.logoff();
        Thread.sleep(2000);

    }

    public void createCredential() throws InterruptedException{
        signUpPageObject.signUp("test","test","test","Test@123");

        webDriver.get("http://localhost:"+this.port+"/sessions/login");
        loginPageObject.mockLogin("test", "Test@123");

        webDriver.get("http://localhost:"+this.port+"/home");
        webDriverWait = new WebDriverWait(webDriver, 2);
        WebElement homeMarker = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
        assertNotNull(homeMarker);

        homePageObject.enterCredential("http://testurl","test","Test@123");

    }

}
