package com.example.meeting.DTO;

import com.example.meeting.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DepartmentGetResult {
    private boolean success;
    private String message;
    private List<Department> departments;
}
