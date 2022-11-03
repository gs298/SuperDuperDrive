package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private NotesService notesService;
    private AuthenticationService authenticationService;
    private UserService userService;

    public NotesController(NotesService notesService, AuthenticationService authenticationService, UserService userService) {
        this.notesService = notesService;
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
            if (note.getNoteid() > 0)
                notesService.updateNotes(note);
            else {
                note.setUserid(userid);
                notesService.insertNotes(note);
            }
        }catch (Exception e){
            model.addAttribute("errorMessage", "something went wrong please try again");
            return "result";
        }
        model.addAttribute("errorMessage", null);
        return "redirect:/home";
    }

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.DELETE})

    public String deleteNotes(@RequestParam("noteid") int noteid, Model model){
        try {

            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            notesService.deleteNotes(noteid, userid);

        }catch (Exception e){
            model.addAttribute("errorMessage", "Note could not be deleted, please try again");
        }

        model.addAttribute("errorMessage",null);
        return "redirect:/home";
    }


}
