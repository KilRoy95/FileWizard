package com.example.FileWizard.dto;

import lombok.Data;

@Data
public class FolderDto {
    private Long id;
    private String folderName;

    public FolderDto() {
    }

    public FolderDto(Long id, String folderName) {
        this.id = id;
        this.folderName = folderName;
    }
}
