package com.udacity.jwdnd.course1.cloudstorage.Model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "First Name cannot be blank")
    private String firstname;
    @NotBlank(message = "Last Name cannot be blank")
    private String lastname;
    @Pattern(regexp = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=])\\S{8,}\\z",
    message = "Password must contain : At least one digit, upper and lower case and a special character and no space")
    private String password;
}
