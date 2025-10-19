package com.example.meeting.controller;

import com.example.meeting.DTO.LoginRequest;
import com.example.meeting.DTO.LoginResult;
import com.example.meeting.DTO.RegisterRequest;
import com.example.meeting.DTO.RegisterResult;
import com.example.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResult> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}