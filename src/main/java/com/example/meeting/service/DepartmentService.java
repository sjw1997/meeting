package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;

public interface DepartmentService {
    ResponseEntity<DepartmentAddResult> addDepartment(DepartmentAddRequest request);
    ResponseEntity<DepartmentGetResult> getDepartment();
    ResponseEntity<DepartmentUpdateResult> updateDepartment(DepartmentUpdateRequest request);
    ResponseEntity<DepartmentDeleteResult> deleteDepartment(Long id);
}
