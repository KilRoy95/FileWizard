package com.example.FileWizard.service;

import com.example.FileWizard.dto.AuthenticationRequest;
import com.example.FileWizard.dto.AuthenticationResponse;
import com.example.FileWizard.dto.RegistrationUserDto;
import com.example.FileWizard.dto.UserDto;
import com.example.FileWizard.jwtConfig.JwtUtil;
import com.example.FileWizard.model.User;
import com.example.FileWizard.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(RegistrationUserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setRoles(userDto.getRoles());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return userRepository.save(user);
        }
    }

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        return getAuthenticationResponse(authenticationRequest);
    }

    private AuthenticationResponse getAuthenticationResponse(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles());

        String jwt = jwtUtil.generateToken(userDto, userDto.getRoles());
        return new AuthenticationResponse(jwt, userDto);
    }
}

