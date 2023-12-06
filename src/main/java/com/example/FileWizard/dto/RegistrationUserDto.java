package com.example.FileWizard.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class RegistrationUserDto {
    private String username;
    private String password;
    /*private String roles;*/
    private Collection<String> roles;

    public RegistrationUserDto() {
    }

    public RegistrationUserDto(String username, String password, Collection<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "RegistrationUserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + roles + '\'' +
                '}';
    }
}
