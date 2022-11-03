package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePageObject {


    @FindBy(id = "logout-btn")
    private WebElement logoutBtn;

    @FindBy(id ="nav-files-tab")
    private WebElement filesTab;

    @FindBy(id ="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id ="nav-credentials-tab")
    private WebElement credentialTab;

    @FindBy(id = "fileUpload")
    private WebElement fileUpload;

    @FindBy(id = "uploadButton")
    private WebElement uploadButton;


    @FindBy(id = "addNotes-button")
    private WebElement addNotes;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "note-edit")
    private WebElement noteEdit;

    @FindBy(id = "notes-save-button")
    private WebElement noteSaveBtn;

    @FindBy(id = "note-edit-btn")
    private WebElement noteEditBtn;

    @FindBy(id = "note-delete-btn")
    private WebElement noteDeleteBtn;

    @FindBy(id = "notes-close-button")
    private WebElement noteCloseBtn;

    private WebDriverWait wait;

    private WebDriver webDriver;

    public HomePageObject(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logoff(){
        logoutBtn.click();
    }
    public void goToNotesTab(){
      //  webDriver.manage().window().maximize();
        notesTab.click();
    }

    public void goToFilesTab(){
        webDriver.manage().window().maximize();
        filesTab.click();
    }

    public void goToCredentialTab(){
        webDriver.manage().window().maximize();
        credentialTab.click();
    }

    public void enterNote(String title, String description) throws InterruptedException{
        goToNotesTab();
        wait = new WebDriverWait(webDriver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));

        addNotes.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        noteTitle.sendKeys(title);
        noteDescription.sendKeys(description);

        noteSaveBtn.click();

        Thread.sleep(2000);


    }

    public String getNoteTile(){
        String result =  webDriver.findElement(By.id("displayed-note-title")).getText();
        return result;
    }

    public String getNoteDescription(){
        return webDriver.findElement(By.id("displayed-note-description")).getText();

    }


   public void deleteNote() throws InterruptedException{
        goToNotesTab();
        Thread.sleep(2000);
        noteDeleteBtn.click();
       Thread.sleep(2000);
}

    public void editNote(String title, String description) throws InterruptedException{
        goToNotesTab();
        Thread.sleep(2000);
        noteEditBtn.click();
        Thread.sleep(2000);

        noteTitle.clear();
        noteTitle.sendKeys(title);

        noteDescription.clear();
        noteDescription.sendKeys(description);

        noteSaveBtn.click();
        Thread.sleep(2000);
    }

}
