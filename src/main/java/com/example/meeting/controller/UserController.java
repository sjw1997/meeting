package com.example.meeting.controller;

import com.example.meeting.DTO.*;
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

    @PostMapping("/verifyToken")
    public ResponseEntity<VerifyTokenResult> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            return userService.verifyToken(token);
        }
        return ResponseEntity.badRequest().build();
    }
}