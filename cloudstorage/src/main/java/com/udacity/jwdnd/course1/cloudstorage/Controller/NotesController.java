package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private NotesService notesService;

    private FileService fileService;
    private AuthenticationService authenticationService;
    private UserService userService;

    private CredentialService credentialsService;

    public NotesController(NotesService notesService, FileService fileService,CredentialService credentialsService,AuthenticationService authenticationService, UserService userService) {
        this.notesService = notesService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @PostMapping
    public String createOrUpdateNotes (@ModelAttribute("notes") Notes note, BindingResult validator, Model model){

        if(validator.hasErrors()){
            model.addAttribute("errorMessage", "Please make sure to provide the Title and Description to the notes");
            return "result";
        }
        try{
            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            if (note.getNoteid() > 0){
                notesService.updateNotes(note);
                model.addAttribute("success", "successfully updated the note");}
            else {
                note.setUserid(userid);
                notesService.insertNotes(note);
                model.addAttribute("success", "successfully added the note");
            }
            model.addAttribute("tab", "nav-notes-tab");
            model.addAttribute("files", fileService.getFilesByUserId(userid));
            model.addAttribute("notes", notesService.selectNotes(userid));
            model.addAttribute("credentials", credentialsService.getAllCredentials(userid));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());
            return "home";
        }catch (Exception e){
            model.addAttribute("errorMessage", "something went wrong please try again");
            return "result";
        }


    }

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.DELETE})

    public String deleteNotes(@RequestParam("noteid") int noteid, Model model){
        try {

            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            notesService.deleteNotes(noteid, userid);
            model.addAttribute("tab", "nav-notes-tab");
            model.addAttribute("files", fileService.getFilesByUserId(userid));
            model.addAttribute("notes", notesService.selectNotes(userid));
            model.addAttribute("credentials", credentialsService.getAllCredentials(userid));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());

        }catch (Exception e){
            model.addAttribute("errorMessage", "Note could not be deleted, please try again");
        }

        model.addAttribute("success",true);
        return "home";
    }


}
