package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<RegisterResult> register(RegisterRequest request);
    ResponseEntity<LoginResult> login(LoginRequest request);
    ResponseEntity<VerifyTokenResult> verifyToken(String token);
}
