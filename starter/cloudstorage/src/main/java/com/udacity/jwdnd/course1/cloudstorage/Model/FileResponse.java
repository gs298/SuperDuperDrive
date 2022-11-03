package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.OptionalLong;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {

    private Long id;
    private String name;
    private String size;

    private String createdon;


    public FileResponse(File file){
        id = OptionalLong.of(file.getFileId()).orElse(0L);
        name = Optional.of(file.getFilename()).orElse("no file name");
        size = Optional.of(String.format("%.2f KB", Math.ceil(file.getFilesize()/1024.0))).orElse("0 MB");
        createdon = file.getCreatedon() == null ? "not found" : file.getCreatedon().toString();
    }
}
