package com.example.FileWizard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"file\"")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private Long size; // in bytes

    @Lob
    @Column(name = "content", columnDefinition = "BLOB")
    @JsonIgnore
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonBackReference
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public UserFile() {
    }

    public UserFile(String filename, Long size, byte[] content, User user, Folder folder) {
        this.filename = filename;
        this.size = size;
        this.content = content;
        this.user = user;
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", bytes=" + size +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", folder=" + (folder != null ? folder.getFoldername() : "null") +
                '}';
    }
}
