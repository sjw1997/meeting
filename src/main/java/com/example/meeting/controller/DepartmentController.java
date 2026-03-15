package com.example.meeting.controller;

import com.example.meeting.DTO.*;
import com.example.meeting.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/department/add")
    public ResponseEntity<DepartmentAddResult> addDepartment(@RequestBody DepartmentAddRequest request) {
        return departmentService.addDepartment(request);
    }

    @GetMapping("/department/get")
    public ResponseEntity<DepartmentGetResult> getDepartment() {
        return departmentService.getDepartment();
    }

    @PutMapping("/department/update")
    public ResponseEntity<DepartmentUpdateResult> updateDepartment(@RequestBody DepartmentUpdateRequest request) {
        return departmentService.updateDepartment(request);
    }

    @DeleteMapping("/department/delete/{id}")
    public ResponseEntity<DepartmentDeleteResult> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
