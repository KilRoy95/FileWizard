package com.example.FileWizard.dto;


import lombok.Data;

import java.util.Collection;

@Data
public class UserDto {
    private String username;
    private Collection<String> roles;

    public UserDto() {
    }

    public UserDto(String username, Collection<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", role='" + roles + '\'' +
                '}';
    }

}
