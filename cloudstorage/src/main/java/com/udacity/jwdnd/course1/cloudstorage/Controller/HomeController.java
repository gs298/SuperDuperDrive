package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;

    private NotesService notesService;
    private AuthenticationService authenticationService;
    private UserService userService;

    private CredentialService credentialService;

    public HomeController(FileService fileService, NotesService notesService ,CredentialService credentialService,AuthenticationService authenticationService, UserService userService){
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialService = credentialService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model){
        int userId;
        try{
            userId = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
        }catch (Exception e){
            return "home";
        }

        try {
            model.addAttribute("files", fileService.getFilesByUserId(userId));
            model.addAttribute("notes", notesService.selectNotes(userId));
            model.addAttribute("credentials", credentialService.getAllCredentials(userId));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());
           // model.addAttribute("success","You have logged in");
            return "home";
        }
        catch (DataNotAvailableException e){
            model.addAttribute("errorMessage", "something unexpected happen please try logging in again");
            return "redirect:/sessions/login-error";
        }
    }

}
