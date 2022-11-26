package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.FileResponse;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;


import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {


    private static final int MAX_FILE_SIZE = 1048576;
    private FileMapper fileMapper;
    private UserService userService;
    private AuthenticationService authService;

    @Autowired
    public FileService(FileMapper fileMapper, UserService userService,
                       AuthenticationService authService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
        this.authService = authService;
    }

    public int uploadFile(MultipartFile file) throws Exception {

        // check what user is logged in
        try {
            int userid = userService.getUserByUserName(authService.getLoggedInUser().getName()).getUserid();

            if (file == null || file.isEmpty()) {
                throw new FileNotFoundException("Please attach a file");
            }

            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
                throw new FileNotFoundException("File Name cannot be empty");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new FileSizeLimitExceededException("File is too large", file.getSize(), MAX_FILE_SIZE);
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (ifFileExists(fileName, userid)) {
                throw new FileAlreadyExistsException("This file already exists");
            }

            File fileToUpload = File.builder()
                    .filename(fileName)
                    .contenttype(file.getContentType())
                    .filesize(file.getSize())
                    .userid(userid)
                    .filedata(file.getBytes())
                    .createdon(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
//"INSERT INTO files (filename, contenttype,filesize,filedata, userid) " +
//            "VALUES (#{filename}, #{contenttype} ,#{filesize}, #{userid}, #{filedata}, #{createdon})")
            return fileMapper.insertFile(fileToUpload);
        } catch (Exception e) {
            throw new Exception("An Issue Occurred When trying to upload the file");
        }
    }

    public boolean ifFileExists(String fileName, int userId) throws Exception {
        //@Select("SELECT * FROM files WHERE filename = #{filename} AND userid = #{userid}")
        //    public File getFileByFileName (String filename, int userid );

        try {
            File file = fileMapper.getFileByFileName(fileName, userId);
            return file != null;

        } catch (Exception e) {
            throw new Exception(" Error when trying to fetch filename");
        }

    }

    public void updateFilename(File file) throws Exception {
        try {
            fileMapper.updateFileName(file.getUserid(), file.getFilename());
        } catch (Exception e) {
            throw new Exception("Something went wrong when updating the file name" + e.getMessage());
        }
    }

    public List<FileResponse> getFilesByUserId(int userId) throws DataNotAvailableException{
        List<FileResponse> fileList;

        try{
            //int userId = userService.getUserByUserName(authService.getLoggedInUser().getName()).getUserid();
            fileList = fileMapper.getFilesByUserId(userId).stream().map(FileResponse::new).collect(Collectors.toList());
            return  fileList;
        } catch (Exception e){
            throw new DataNotAvailableException("Could Not Find The File" + e.getMessage());
        }

    }




    public FileResponse getFileByFileId(int fileId) throws Exception{
        File file;
        try{
            file = fileMapper.getFileByFileId(fileId);
            if(file == null){
                return null;
            }else{
                return  new FileResponse(file);
            }
        }catch (Exception e){
            throw new DataNotAvailableException("File does not exists" + e.getMessage());
        }
    }



    public FileResponse getFileByFileName(String filename) throws Exception{
        int userId;
        File file;
        try{
            userId = userService.getUserByUserName(authService.getLoggedInUser().getName()).getUserid();
            file = fileMapper.getFileByFileName(filename, userId);

            if(file == null){
                return  null;
            }else{
                return new FileResponse(file);
            }
        }catch (Exception e){
            throw new DataNotAvailableException("File does not exists" + e.getMessage());
        }
    }

    public File downloadFile(int fileId) throws Exception{
        File file;
        try{
            file = fileMapper.getFileByFileId(fileId);
            return file;
        }catch (Exception e){
            throw new DataNotAvailableException("File Does Not Exists" + e.getMessage());
        }
    }

    public void deleteFileByFileId(int fileId) throws Exception{
        try{
            fileMapper.deleteFileByFileId(fileId);

        }catch(Exception e){
            throw new Exception("file does not exist in our backend" + e.getMessage());
        }

    }

    public void deleteFileByFilename(String filename) throws Exception{
        try {
            int userid = userService.getUserByUserName(authService.getLoggedInUser().getName()).getUserid();
            fileMapper.deletFileByFilename(filename, userid);
        }catch (Exception e){
            throw new Exception("file does not exist in our backend" + e.getMessage());
        }
    }
}
