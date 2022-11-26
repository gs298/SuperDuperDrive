package com.udacity.jwdnd.course1.cloudstorage.Controller;


import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private NotesService notesService;

    private FileService fileService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    private final CredentialService credentialService;
    private EncryptionService encryptionService;

    @Autowired
    public CredentialController(NotesService notesService, FileService fileService,
                                CredentialService credentialService, EncryptionService encryptionService,
                                AuthenticationService authenticationService, UserService userService) {
        this.credentialService = credentialService;
        this.notesService = notesService;
        this.fileService = fileService;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String createOrUpdateCredential(Model model, @ModelAttribute("credentials") Credential credential, BindingResult validator) {

        if (validator.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid fields fill all the required fields correctly");
            return "result";
        }

        int userid;
        try {
            userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
        } catch (DataNotAvailableException e) {
            model.addAttribute("errorMessage", "please try again somethign went wrong");
            return "result";
        }
        try {
            if (credential.getCredentialid() < 1) {

                    credential.setUserid(userid);
                    credential.setSalt(CredentialController.getSalt());
                    credentialService.createCredential(credential);


            } else {

                    credential.setUserid(userid);
                    credential.setSalt(CredentialController.getSalt());
                    credentialService.updateCredential(credential);

            }
            model.addAttribute("success", true);
            model.addAttribute("tab", "nav-credentials-tab");
            model.addAttribute("files", fileService.getFilesByUserId(userid));
            model.addAttribute("notes", notesService.selectNotes(userid));
            model.addAttribute("credentials", credentialService.getAllCredentials(userid));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());
            return "home";
        }catch (Exception e) {
            model.addAttribute("errorMessage", "something went wrong when updating credential");
            return "result";
        }

    }


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCredential(@RequestParam("credentialid") int credentialid, Model model){
        try{
            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            credentialService.deleteCredential(userid, credentialid);
            model.addAttribute("tab", "nav-credentials-tab");
            model.addAttribute("files", fileService.getFilesByUserId(userid));
            model.addAttribute("notes", notesService.selectNotes(userid));
            model.addAttribute("credentials", credentialService.getAllCredentials(userid));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());
        }catch (Exception e){
            model.addAttribute("errorMessage","Somethign went wrong when deleting credential");
            return "result";
        }
        model.addAttribute("success", true);
        return "home";
    }


    public static String getSalt(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
         return Base64.getEncoder().encodeToString(key);
    }

}
