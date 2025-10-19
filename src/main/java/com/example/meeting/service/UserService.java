package com.example.meeting.service;

import com.example.meeting.DTO.LoginRequest;
import com.example.meeting.DTO.LoginResult;
import com.example.meeting.DTO.RegisterRequest;
import com.example.meeting.DTO.RegisterResult;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<RegisterResult> register(RegisterRequest request);
    ResponseEntity<LoginResult> login(LoginRequest request);
}
