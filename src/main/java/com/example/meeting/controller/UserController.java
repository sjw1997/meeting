package com.example.meeting.controller;

import com.example.meeting.DTO.*;
import com.example.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<VerifyTokenResult> verifyToken(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        return userService.verifyToken(token);
    }

    @GetMapping("/user/getUsers")
    public ResponseEntity<UserGetResult> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/user/update")
    public ResponseEntity<UserUpdateResult> updateUser(@RequestBody UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<UserDeleteResult> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/user/import")
    public ResponseEntity<UserImportResult> importUser(@RequestParam("file") MultipartFile file) {
        return userService.importUser(file);
    }

    @PostMapping("/user/add")
    public ResponseEntity<UserAddResult> addUser(@RequestBody UserAddRequest request) {
        return userService.addUser(request);
    }
}