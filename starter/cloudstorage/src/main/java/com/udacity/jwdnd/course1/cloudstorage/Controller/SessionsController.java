package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Exceptions.SignUpException;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.LoginRequest;
import com.udacity.jwdnd.course1.cloudstorage.Model.SignupRequest;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller()
@RequestMapping("/sessions")
public class SessionsController {

    private UserService userService;

    public SessionsController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("signup", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupReq(Model model, @Valid @ModelAttribute("signup") SignupRequest signupRequest, BindingResult validator){
        if(validator.hasErrors()){
            String pwdError = "Password Format is not correct";
            model.addAttribute("errorMessage",validator.getFieldError().getArguments()[0].toString().contains("password")? pwdError:"password cannot be blank");
            model.addAttribute("message", null);
            return "signup";
        }

        try{
            userService.createUser(signupRequest);
        }
        catch (SignUpException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("message", null);
            return "signup";
        }
        model.addAttribute("message","You successfully signed up!");
        return "redirect:/login";

    }


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("login", new LoginRequest());
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model){
        HttpSession httpSession = request.getSession(false);
        String errorMessage = "incorrect login";

        if(httpSession != null){
            AuthenticationException e = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if(e != null){
                errorMessage = e.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "redirect:/login";

    }
}