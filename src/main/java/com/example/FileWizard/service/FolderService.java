package com.example.FileWizard.service;

import com.example.FileWizard.dto.FolderDto;
import com.example.FileWizard.model.Folder;
import com.example.FileWizard.model.User;
import com.example.FileWizard.repository.FolderRepository;
import com.example.FileWizard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FolderService {
    private FolderRepository folderRepo;
    private UserRepository userRepo;

    @Value("${myapp.folder-path}") // Path to folder dir
    private String folderPath;

    public FolderService(FolderRepository folderRepo, UserRepository userRepo) {
        this.folderRepo = folderRepo;
        this.userRepo = userRepo;
    }

    public List<Folder> getAll() {
        return folderRepo.findAll();
    }

    public String addFolder(FolderDto folderDto, User currentUser) {

        // Create a Path object
        Path path = Paths.get(folderPath + currentUser.getUsername(), folderDto.getFolderName());

        List<Folder> userFolders = currentUser.getFolders();
        String localFolderPath = folderPath;

        try {

            if (!currentUser.getFolders().isEmpty()) {
                // Check if folders are same in both db and local UserDate folder
                boolean foldersAreEqual = areFoldersEqual(userFolders, localFolderPath + currentUser.getUsername());
                if (foldersAreEqual) {
                    System.out.println("Folders synced.");
                } else {
                    System.out.println("Folders are not synced.");
                }
            }

            if (containsFolderWithName(userFolders, folderDto.getFolderName())) {
                throw new RuntimeException("Folder already exists: (" + localFolderPath + ")");
            } else {
                Files.createDirectories(path);

                Folder folder = new Folder(folderDto.getFolderName(), currentUser);
                System.out.println("New folder: " + folder);
                folderRepo.save(folder);


                System.out.println("Folder created successfully at: " + path);
                return "Folder created successfully at: " + path;
            }

        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace();
            return "Folder creation failed: " + e.getMessage();

        } catch (Exception e) {
            return "Folder creation failed";
        }
    }

    public static boolean areFoldersEqual(List<Folder> userFolders, String localFolderPath) {
        File localFolder = new File(localFolderPath);

        if (!localFolder.exists() || !localFolder.isDirectory()) {
            // The local folder doesn't exist or is not a directory
            return false;
        }

        File[] localFiles = localFolder.listFiles();

        if (localFiles == null || localFiles.length == 0) {
            // The local folder is empty
            return userFolders.isEmpty();
        }

        for (File localFile : localFiles) {
            if (!containsFolderWithName(userFolders, localFile.getName())) {
                // The user's list of folders does not contain the local folder
                return false;
            }
        }
        return true;
    }

    private static boolean containsFolderWithName(List<Folder> folders, String folderName) {
        for (Folder folder : folders) {
            if (folder.getFoldername().equals(folderName)) {
                return true;
            }
        }
        return false;
    }
}