package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notes {

    private int noteid;

    @NotBlank(message = "Note Title cannot be empty")
    private String notetitle;
    private String notedescription;
    private int userid;



}
