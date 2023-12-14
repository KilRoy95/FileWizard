package com.example.FileWizard.repository;

import com.example.FileWizard.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<UserFile, Long> {
    @Query("SELECT f FROM UserFile f WHERE f.user.username = :username")
    List<UserFile> findFilesByUsername(@Param("username") String username);

    @Query("SELECT f FROM UserFile f WHERE f.folder.foldername = :foldername AND f.user.username = :username")
    List<UserFile> findFilesByFolderAndUsername(@Param("foldername") String foldername, @Param("username") String username);

    @Query("SELECT f FROM UserFile f WHERE f.id = :fileId AND f.user.username = :username")
    UserFile findFileByIdAndUsername(@Param("fileId") Long fileId, @Param("username") String username);
}
