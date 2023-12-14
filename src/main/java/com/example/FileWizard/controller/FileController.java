package com.example.FileWizard.controller;

import com.example.FileWizard.model.User;
import com.example.FileWizard.model.UserFile;
import com.example.FileWizard.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/all-files")
    public ResponseEntity<List<UserFile>> getAll() {
        return ResponseEntity.ok(fileService.getAll());

        //endpoint: http://localhost:8080/api/all-files
        //Desc: Returns all files
    }

    @GetMapping("/my-files")
    public ResponseEntity<?> getMyFolders(@AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("Finding files for: " + userDetails.getUsername());
        return ResponseEntity.ok(fileService.getMyFiles(userDetails.getUsername()));

        //endpoint: http://localhost:8080/api/my-files
        //Desc: Returns all (authenticated users) files
        //Require: Token
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(fileService.uploadFile(file, folder, (User) userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating file: " + e.getMessage());
        }

        // endpoint: http://localhost:8080/api/upload
    /* MultipartFile / form-data:
        {
           "file" (file):  testfile.file
           "folder" (text/string):  "testfolder"
        }
    */
        //Require: Token
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return fileService.downloadFile(fileId, (User) userDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating file: " + e.getMessage());
        }

        // endpoint: http://localhost:8080/api/download/{id}
        //Require: Token
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId, @AuthenticationPrincipal UserDetails userDetails) {

        try {
            return fileService.deleteFile(fileId, (User) userDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating file: " + e.getMessage());
        }

        // endpoint: http://localhost:8080/api/delete/{id}
        //Require: Token
    }
}
