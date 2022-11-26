package com.udacity.jwdnd.course1.cloudstorage.Controller;


import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.FileResponse;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {


    private NotesService notesService;

    private FileService fileService;
    private AuthenticationService authenticationService;
    private UserService userService;

    private CredentialService credentialsService;


    public FileController(FileService fileService, NotesService notesService,CredentialService credentialsService, AuthenticationService authenticationService, UserService userService){
        this.notesService = notesService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    // upload file
    @PostMapping
    public String uploadFile(Model model, @RequestParam("fileUpload")MultipartFile file){
        try{
            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            int fileId = fileService.uploadFile(file);
            if(Integer.valueOf(fileId) != null){
                model.addAttribute("tab", "nav-files-tab");
                model.addAttribute("success", true);
                model.addAttribute("files", fileService.getFilesByUserId(userid));
                model.addAttribute("notes", notesService.selectNotes(userid));
                model.addAttribute("credentials", credentialsService.getAllCredentials(userid));
                model.addAttribute("note", new Notes());
                 model.addAttribute("credential", new Credential());
                return "home";
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


    // update file

    // delete file
    @RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.DELETE})
    public String deleteFile(Model model, @RequestParam int fileId){
        try{
            int userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
            fileService.deleteFileByFileId(fileId);
            model.addAttribute("tab", "nav-files-tab");
            model.addAttribute("success", true);
            model.addAttribute("files", fileService.getFilesByUserId(userid));
            model.addAttribute("notes", notesService.selectNotes(userid));
            model.addAttribute("credentials", credentialsService.getAllCredentials(userid));
            model.addAttribute("note", new Notes());
            model.addAttribute("credential", new Credential());
            return "home";
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
