package com.example.FileWizard.service;

import com.example.FileWizard.model.Folder;
import com.example.FileWizard.model.User;
import com.example.FileWizard.model.UserFile;
import com.example.FileWizard.repository.FileRepository;
import com.example.FileWizard.repository.FolderRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {


    private final FileRepository fileRepo;
    private final FolderRepository folderRepo;

    public FileService(FileRepository fileRepo, FolderRepository folderRepo) {
        this.fileRepo = fileRepo;
        this.folderRepo = folderRepo;
    }

    public List<UserFile> getAll() {
        return fileRepo.findAll();
    }

    public List<UserFile> getMyFiles(String username) {
        return fileRepo.findFilesByUsername(username);
    }

    public String uploadFile(MultipartFile file, String folderName, User userDetails) {
        try {
            String fileName = file.getOriginalFilename();

            // Check if the folder exists in the user's list of folders
            List<Folder> userFolders = folderRepo.findFoldersByUsername(userDetails.getUsername());
            Optional<Folder> existingFolder = userFolders.stream()
                    .filter(folder -> folder.getFoldername().equals(folderName))
                    .findFirst();

            if (existingFolder.isEmpty()) {
                return "Folder does not exist";
            }

            List<UserFile> folderFiles = fileRepo.findFilesByFolderAndUsername(folderName, userDetails.getUsername());
            if (containsFileWithName(folderFiles, fileName)) {
                return "File already exists";
            } else {
                // Read file content into byte array
                byte[] fileContent = file.getBytes();

                // Create file entity with content and save to the database
                UserFile userFile = new UserFile(fileName, file.getSize(), fileContent, userDetails, folderRepo.findFolderByNameAndUsername(folderName, userDetails.getUsername()));
                fileRepo.save(userFile);

                System.out.println("File uploaded successfully to database: " + userFile);
                return "Uploaded successfully: " + userFile;
            }


        } catch (Exception e) {
            return "Upload failed: " + e.getMessage();
        }
    }

    public ResponseEntity<byte[]> downloadFile(Long fileId, User userDetails) {
        // Check if the file is associated with the user
        List<UserFile> userFiles = userDetails.getFiles();

        if (!containsFileWithId(userFiles, fileId)) {
            throw new RuntimeException("File not found");
        }

        UserFile file = fileRepo.findFileByIdAndUsername(fileId, userDetails.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getFilename());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getSize())
                .body(file.getContent());
    }

    public ResponseEntity<String> deleteFile(Long fileId, User userDetails) {
        List<UserFile> userFiles = userDetails.getFiles();

        if (!containsFileWithId(userFiles, fileId)) {
            throw new RuntimeException("File not found");
        }

        UserFile file = fileRepo.findFileByIdAndUsername(fileId, userDetails.getUsername());

        fileRepo.delete(file);

        return ResponseEntity.ok("File deleted successfully");
    }

    private static boolean containsFileWithName(List<UserFile> files, String fileName) {
        for (UserFile file : files) {
            if (file.getFilename().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsFileWithId(List<UserFile> files, Long fileId) {
        for (UserFile file : files) {
            if (file.getId().equals(fileId)) {
                return true;
            }
        }
        return false;
    }
}
