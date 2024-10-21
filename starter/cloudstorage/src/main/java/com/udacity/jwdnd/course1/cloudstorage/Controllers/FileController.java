package com.udacity.jwdnd.course1.cloudstorage.Controllers;

import com.udacity.jwdnd.course1.cloudstorage.CommonComponents;
import com.udacity.jwdnd.course1.cloudstorage.Entity.File;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.UnauthorizedAccessException;
import com.udacity.jwdnd.course1.cloudstorage.Services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Controller
public class FileController {

    private final FileService fileService;
    private final CommonComponents commonComponents;


    public FileController(FileService fileService, UserMapper userMapper, CommonComponents commonComponents, FilesMapper filesMapper) {
        this.fileService = fileService;
        this.commonComponents = commonComponents;
    }

    @PostMapping("/upload")
    public String addFile(Model model, @RequestParam("fileUpload") MultipartFile multiPartFile, Authentication authentication) {

        if(multiPartFile.isEmpty()) {
            model.addAttribute("errorMsg", "Please select a file");
            commonComponents.addCommonAttributes(model, authentication);
            return "home";
        }

        try {
            fileService.uploadFile(multiPartFile, authentication.getName());
            model.addAttribute("successMsgFile", "File uploaded successfully");
        } catch (FileAlreadyExistsException e) {
            model.addAttribute("errorMsg", "File already exists, cannot duplicate.");
        } catch (IOException e) {
            model.addAttribute("errorMsg", "File upload failed: " + e.getMessage());
        } catch (UnauthorizedAccessException e) {
            model.addAttribute("errorMsg", "Access denied");
        }

        commonComponents.addCommonAttributes(model, authentication);
        return "home";
    }


    @PostMapping("/deleteFile")
    public String deleteFile(Integer fileId, Authentication authentication, Model model) {

        try {
            fileService.deleteFile(fileId, authentication.getName());
        } catch (FileNotFoundException e) {
            model.addAttribute("errorMsg", "File not found: " + e.getMessage());
        } catch (UnauthorizedAccessException e) {
            model.addAttribute("errorMsg", "Unauthorized: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "An error occurred: " + e.getMessage());
        }
        commonComponents.addCommonAttributes(model, authentication);
        return "home";
    }


    @GetMapping("/getFile/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer fileId, Authentication authentication) throws IOException {
        ResponseEntity<byte[]> file = null;
        try {
            file = fileService.getFile(fileId, authentication.getName());
        } catch (IOException e) {
            throw new IOException("Error Getting File for download");
        } catch (UnauthorizedAccessException e) {
            throw new UnauthorizedAccessException("File not found: " + e.getMessage());

        }
        return file;
    }
}
