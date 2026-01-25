package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.DepartmentAddRequest;
import com.example.meeting.DTO.DepartmentAddResult;
import com.example.meeting.entity.Department;
import com.example.meeting.mapper.DepartmentMapper;
import com.example.meeting.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ResponseEntity<DepartmentAddResult> addDepartment(DepartmentAddRequest request) {
        String name = request.getName();
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(new DepartmentAddResult(false, "请填写部门名称"));
        }

        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Department existingDepartment = departmentMapper.selectOne(queryWrapper);
        if (existingDepartment != null) {
            return ResponseEntity.badRequest().body(new DepartmentAddResult(false, "部门名称已存在"));
        }
        Department department = new Department(null, name);
        departmentMapper.insert(department);
        return ResponseEntity.ok(new DepartmentAddResult(true, "添加部门成功"));
    }
}
