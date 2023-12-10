package com.example.FileWizard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "\"folder\"")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foldername;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "folder", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserFile> userFiles;

    public Folder() {
    }

    public Folder(String folderName, User user) {
        this.foldername = folderName;
        this.user = user;
    }

    public Folder(String localFolderPath) {
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", foldername='" + foldername + '\'' +
                ", owner=" + user.getUsername() +
                ", files=" + userFiles +
                '}';
    }
}
