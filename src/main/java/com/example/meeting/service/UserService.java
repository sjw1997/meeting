package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<RegisterResult> register(RegisterRequest request);
    ResponseEntity<LoginResult> login(LoginRequest request);
    ResponseEntity<VerifyTokenResult> verifyToken(String token);
    ResponseEntity<UserGetResult> getUsers();
    ResponseEntity<UserUpdateResult> updateUser(UserUpdateRequest request);
    ResponseEntity<UserDeleteResult> deleteUser(Long id);
    ResponseEntity<UserImportResult> importUser(MultipartFile file);
    ResponseEntity<UserAddResult> addUser(UserAddRequest request);
}
