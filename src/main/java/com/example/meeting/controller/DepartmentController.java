package com.example.meeting.controller;

import com.example.meeting.DTO.DepartmentAddRequest;
import com.example.meeting.DTO.DepartmentAddResult;
import com.example.meeting.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/department/add")
    public ResponseEntity<DepartmentAddResult> addDepartment(@RequestBody DepartmentAddRequest request) {
        return departmentService.addDepartment(request);
    }
}
