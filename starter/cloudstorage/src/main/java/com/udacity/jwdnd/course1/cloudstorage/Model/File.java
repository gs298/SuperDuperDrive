package com.udacity.jwdnd.course1.cloudstorage.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    private Integer fileId;
    @NotBlank(message = "File name must be specified")
    private String filename;

    @NotBlank(message = "File content type must be specified")
    private String contenttype;

    private Long filesize;
    private Integer userid;
    private byte[] filedata;
    private Timestamp createdon;





}
