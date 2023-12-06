package com.example.FileWizard.repository;

import com.example.FileWizard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}