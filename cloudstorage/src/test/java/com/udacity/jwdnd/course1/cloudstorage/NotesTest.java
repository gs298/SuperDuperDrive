package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.PageObjects.HomePageObject;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.LoginPageObject;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.SignUpPageObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Builder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesTest {

    @LocalServerPort
    private int port;

    private static WebDriver webDriver;

    private WebDriverWait webDriverWait;

    private static SignUpPageObject signUpPageObject;

    private static LoginPageObject loginPageObject;

    private static HomePageObject homePageObject;

    @BeforeAll
    public static void beforeAll() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll(){
        webDriver.quit();
    }

    @BeforeEach
    public  void beforeEach() {
        webDriver.get("http://localhost:"+this.port+"/sessions/signup");
        signUpPageObject = new SignUpPageObject(webDriver);
        loginPageObject = new LoginPageObject(webDriver);
        homePageObject= new HomePageObject(webDriver);

    }

    @Test
    public void testCreateNote() throws InterruptedException{

        createNote();
        homePageObject.goToNotesTab();
        Thread.sleep(2000);
        assertEquals("Test", homePageObject.getNoteTile());
        assertEquals("Test", homePageObject.getNoteDescription());
        homePageObject.deleteNote();
        Thread.sleep(2000);
        homePageObject.logoff();
        Thread.sleep(2000);
    }


    @Test
    public void testEditNote() throws InterruptedException{
        createNote();
        homePageObject.editNote("Edit","Edit");
        homePageObject.goToNotesTab();
        Thread.sleep(2000);
        assertEquals("Edit", homePageObject.getNoteTile());
        assertEquals("Edit", homePageObject.getNoteDescription());
        homePageObject.deleteNote();
        Thread.sleep(2000);
        homePageObject.logoff();
        Thread.sleep(2000);
    }

    @Test
    public void testDeleteNote() throws InterruptedException{

        createNote();
        Thread.sleep(2000);

        homePageObject.goToNotesTab();
        homePageObject.deleteNote();

        homePageObject.goToNotesTab();
        Thread.sleep(2000);

        try{
            webDriver.findElement(By.id("displayed-note-title"));
            fail("Note not deleted");
        }catch (NoSuchElementException e){
            assertTrue(true);
        }
        homePageObject.logoff();

    }

    public void createNote() throws InterruptedException{

        signUpPageObject.signUp("test", "test", "test", "Test@123");

        webDriver.get("http://localhost:"+this.port+"/sessions/login");
        loginPageObject.mockLogin("test", "Test@123");


        webDriver.get("http://localhost:"+this.port+"/home");
        webDriverWait = new WebDriverWait(webDriver, 2);
        WebElement homeMarker = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
        assertNotNull(homeMarker);

        homePageObject.enterNote("Test","Test");
    }

}
