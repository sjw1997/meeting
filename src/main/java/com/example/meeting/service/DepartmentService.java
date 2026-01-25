package com.example.meeting.service;

import com.example.meeting.DTO.DepartmentAddRequest;
import com.example.meeting.DTO.DepartmentAddResult;
import org.springframework.http.ResponseEntity;

public interface DepartmentService {
    ResponseEntity<DepartmentAddResult> addDepartment(DepartmentAddRequest request);
}
