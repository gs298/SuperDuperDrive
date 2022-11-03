package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    private int credentialid;
    private String url;
    @NotBlank(message = "username cannot be blank")
    private String username;
    private String salt;
    private String password;
    private Integer userid;
    @NotBlank(message = "password cannot be blank")
    private String rawpassword;

}
