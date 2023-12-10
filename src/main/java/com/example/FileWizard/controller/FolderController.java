package com.example.FileWizard.controller;

import com.example.FileWizard.dto.FolderDto;
import com.example.FileWizard.model.Folder;
import com.example.FileWizard.model.User;
import com.example.FileWizard.repository.FolderRepository;
import com.example.FileWizard.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FolderController {
    private final FolderRepository folderRepo;
    private final FolderService folderService;

    @Autowired
    public FolderController(FolderRepository folderRepo, FolderService folderService) {
        this.folderRepo = folderRepo;
        this.folderService = folderService;
    }

    @GetMapping("/all-folders")
    public ResponseEntity<List<Folder>> getAll() {
        return ResponseEntity.ok(folderService.getAll());

        //endpoint: http://localhost:8080/api/all-folders
        //Desc: Returns all folders
        //Require: Token
    }

    @PostMapping("/create-folder")
    public ResponseEntity<?> createFolder(@RequestBody FolderDto folderDto) {
        try {
            Object currentUser = getPrincipal();
            System.out.println("currentUser.getName: " + currentUser);
            return ResponseEntity.ok(folderService.addFolder(folderDto, (User) currentUser));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating folder: " + e);
        }
    }

    private Object getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return principal;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
    }

    /*private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
        return username;
    }*/
}
