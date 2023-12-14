package com.example.FileWizard.service;

import com.example.FileWizard.dto.FolderDto;
import com.example.FileWizard.model.Folder;
import com.example.FileWizard.model.User;
import com.example.FileWizard.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {
    private FolderRepository folderRepo;

    public FolderService(FolderRepository folderRepo) {
        this.folderRepo = folderRepo;
    }

    public List<Folder> getAll() {
        return folderRepo.findAll();
    }

    public List<Folder> getMyFolders(String username) {
        return folderRepo.findFoldersByUsername(username);
    }

    public String addFolder(FolderDto folderDto, User currentUser) {

        List<Folder> userFolders = currentUser.getFolders();

        try {
            if (containsFolderWithName(userFolders, folderDto.getFolderName())) {
                throw new RuntimeException("Folder already exists");
            } else {
                Folder folder = new Folder(folderDto.getFolderName(), currentUser);
                System.out.println("New folder: " + folder);
                folderRepo.save(folder);

                System.out.println("Folder created successfully");
                return "Folder created successfully";
            }

        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace();
            return "Folder creation failed: " + e.getMessage();

        } catch (Exception e) {
            return "Folder creation failed";
        }
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