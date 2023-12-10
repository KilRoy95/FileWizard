package com.example.FileWizard.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"file\"")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private double size; // in byted

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserFile() {
    }

    public UserFile(String name, double size, User user, Folder folder) {
        this.fileName = name;
        this.size = size;
        this.user = user;
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", filename='" + fileName + '\'' +
                ", user=" + (user != null ? user.getId() : "null") +
                ", folder=" + folder +
                '}';
    }
}
