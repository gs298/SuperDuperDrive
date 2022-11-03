package com.udacity.jwdnd.course1.cloudstorage.Model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {

    private int userid;
    @NotBlank (message = "FirstName cannot be blank")
    private String firstname;
    @NotBlank (message = "LastName cannot be blank")
    private String lastname;
    private String salt;

    @NotBlank(message = "Please ensure that username be not blank")
    private String username;

    @Pattern(regexp = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=])\\S{8,}\\z",
            message = "password must contain at least one digit, upper and lower case, and a  special character and no space")
    @NotBlank(message = "Please ensure that password be not blank")
    private String password;



}
