package com.example.FileWizard.repository;

import com.example.FileWizard.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UserFile, Long> {
}
