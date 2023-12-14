package com.example.FileWizard.repository;

import com.example.FileWizard.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT f FROM Folder f WHERE f.user.username = :username")
    List<Folder> findFoldersByUsername(@Param("username") String username);

    @Query("SELECT f FROM Folder f WHERE f.foldername = :foldername AND f.user.username = :username")
    Folder findFolderByNameAndUsername(@Param("foldername") String foldername, @Param("username") String username);
}
