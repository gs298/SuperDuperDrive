package com.udacity.jwdnd.course1.cloudstorage.Controller;


import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.FileResponse;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.tomcat.util.modeler.modules.ModelerSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Controller
@RequestMapping("/files")
public class FileController {


    private FileService fileService;
    private AuthenticationService authenticationService;
    private UserService userService;


    public FileController(FileService fileService, AuthenticationService authenticationService, UserService userService){
        this.fileService = fileService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    // upload file
    @PostMapping
    public String uploadFile(Model model, @RequestParam("fileUpload")MultipartFile file){
        try{
            int fileId = fileService.uploadFile(file);
            if(Integer.valueOf(fileId) != null){
                model.addAttribute("errorMessage", null);
                return "redirect:/home";
            }else{
                throw new Exception("File Could Not Be Uploaded");
            }

        }catch (Exception e){
            if(e.getMessage().contains("user-error"))
                model.addAttribute("errorMessage", e.getMessage());
            else model.addAttribute("errorMessage", "An Error Has Occurred When Uploading The File");
            return "result";
        }
    }
    //view file

    @GetMapping
    public String viewFile(Model model, @RequestParam int fileId){
        try{
           FileResponse fileResponse =  fileService.getFileByFileId(fileId);
           model.addAttribute("message", fileResponse);
           return "home";
        }catch (Exception e){
            model.addAttribute("errorMessage", "an error has occurred while getting the file");
            return "home";
        }
    }

    // update file

    // delete file
    @RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.DELETE})
    public String deleteFile(Model model, @RequestParam int fileId){
        try{
            fileService.deleteFileByFileId(fileId);
            model.addAttribute("errorMessage", null);
            return "redirect:/home";
        }catch (Exception e){
            model.addAttribute("errorMessage", "an error occurred when deleting the file");
            return "result";
        }
    }
    // download file

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public ResponseEntity<ByteArrayResource> downloadFile(Model model, @RequestParam int fileId){
        File file;

        try{
            file = fileService.downloadFile(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContenttype()))
                    .contentLength(file.getFilesize())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\""+file.getFilename()+"\"")
                    .body(new ByteArrayResource(file.getFiledata()));

        } catch(Exception e){
            model.addAttribute("errorMessage", "File Cannot Be Downloaded");
            return ResponseEntity.notFound().build();
        }
    }


}
