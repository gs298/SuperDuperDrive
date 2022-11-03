package com.udacity.jwdnd.course1.cloudstorage.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotAvailableException extends Exception{

    public DataNotAvailableException(){
        super("Data is not available in Database");
    }

    public DataNotAvailableException(String message){
        super(message);
    }

    public DataNotAvailableException(String message, Exception e){
        super(message, e);
    }


}
