package com.udacity.jwdnd.course1.cloudstorage.Controller;


import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private  CredentialService credentialService;
    private  AuthenticationService authenticationService;
    private  UserService userService;

    private EncryptionService encryptionService;

    @Autowired
    public CredentialController(CredentialService credentialService,
                                AuthenticationService authenticationService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String createOrUpdateCredential (Model model, @ModelAttribute("credentials") Credential credential, BindingResult validator){

        if(validator.hasErrors()){
            model.addAttribute("errorMessage","Invalid fields fill all the required fields correctly");
            return "result";
        }

        int userid;
        try{
            userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
        }catch (DataNotAvailableException e){
            model.addAttribute("errorMessage", "please try again somethign went wrong");
            return "result";
        }
        if(credential.getCredentialid() < 1){
            try{
                credential.setUserid(userid);
                credential.setSalt(CredentialController.getSalt());
                credentialService.createCredential(credential);
            }catch (Exception e){
                model.addAttribute("errorMessage", "something went wrong when creating credential");
                return "result";
            }
        } else {
            try{
                credential.setUserid(userid);
                credential.setSalt(CredentialController.getSalt());
                credentialService.updateCredential(credential);
            }catch (Exception e){
                model.addAttribute("errorMessage", "something went wrong when updating credential");
                return "result";
            }
        }

        model.addAttribute("errorMessage", null);
        return "redirect:/home";

    }


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCredential(@RequestParam("credentialid") int credentialid, Model model){
        try{
            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            credentialService.deleteCredential(userid, credentialid);
        }catch (Exception e){
            model.addAttribute("errorMessage","Somethign went wrong when deleting credential");
            return "result";
        }
        model.addAttribute("errorMessage",null);
        return "redirect:/home";
    }


    public static String getSalt(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
         return Base64.getEncoder().encodeToString(key);
    }

}
