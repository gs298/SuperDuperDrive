package com.udacity.jwdnd.course1.cloudstorage.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignUpException extends Exception {

    public SignUpException(){
        super("Error while Signing Up");
    }

    public SignUpException(String message){
        super(message);
    }

    public SignUpException(String message, Throwable throwable){
        super(message, throwable);
    }


}
