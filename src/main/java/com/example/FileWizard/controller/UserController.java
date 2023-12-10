package com.example.FileWizard.controller;

import com.example.FileWizard.dto.AuthenticationRequest;
import com.example.FileWizard.dto.AuthenticationResponse;
import com.example.FileWizard.dto.RegistrationUserDto;
import com.example.FileWizard.jwtConfig.JwtUtil;
import com.example.FileWizard.model.User;
import com.example.FileWizard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtTokenService;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtTokenService) {
        this.userService = userService;

        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());

        //endpoint: http://localhost:8080/api/all-users
        //Desc: Returns all users
        //Require: Token
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            AuthenticationResponse response = userService.login(authenticationRequest);
            System.out.println(response);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, response.getJwt())
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging in user");
        }
        // endpoint: http://localhost:8080/api/login
    /* JSON:
        {
           "username":  "test@test.com"
           "password":  "test"
        }
    */
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUserWithRole(@RequestBody RegistrationUserDto userRegistrationDTO) {
        try {
            User savedUser = userService.registerUser(userRegistrationDTO);
            // Issue a JWT token and include it in the response headers
            var token = jwtTokenService.issueToken(userRegistrationDTO.getUsername(), userRegistrationDTO.getRoles().toString());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(savedUser);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

        // endpoint: http://localhost:8080/api/register
    /* JSON:
        {
            "username":  "test@test.com",
            "password":  "test",
            "roles": ["test"]
        }
    */
        //"roles" is either ["ADMIN"] or ["USER"]
    }
}