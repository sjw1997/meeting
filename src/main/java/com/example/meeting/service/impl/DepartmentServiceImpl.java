package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.*;
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

        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new DepartmentAddResult(false, "部门名称不能为空"));
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

    @Override
    public ResponseEntity<DepartmentGetResult> getDepartment() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        return ResponseEntity.ok(new DepartmentGetResult(true, "获取部门成功", departmentMapper.selectList(queryWrapper)));
    }

    @Override
    public ResponseEntity<DepartmentUpdateResult> updateDepartment(DepartmentUpdateRequest request) {
        Long id = request.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new DepartmentUpdateResult(false, "请选择部门"));
        }

        Department department = departmentMapper.selectById(id);
        if (department == null) {
            return ResponseEntity.badRequest().body(new DepartmentUpdateResult(false, "部门不存在"));
        }

        String name = request.getName();
        if (name == null) {
            return ResponseEntity.badRequest().body(new DepartmentUpdateResult(false, "请填写部门名称"));
        }
        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new DepartmentUpdateResult(false, "部门名称不能为空"));
        }
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Department existingDepartment = departmentMapper.selectOne(queryWrapper);
        if (existingDepartment != null) {
            return ResponseEntity.badRequest().body(new DepartmentUpdateResult(false, "部门名称已存在"));
        }


        if (department.getName().equals(name)) {
            return ResponseEntity.ok(new DepartmentUpdateResult(true, "更新部门成功"));
        }

        department.setName(name);
        departmentMapper.updateById(department);
        return ResponseEntity.ok(new DepartmentUpdateResult(true, "更新部门成功"));
    }

    @Override
    public ResponseEntity<DepartmentDeleteResult> deleteDepartment(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new DepartmentDeleteResult(false, "请选择部门"));
        }

        Department department = departmentMapper.selectById(id);
        if (department == null) {
            return ResponseEntity.badRequest().body(new DepartmentDeleteResult(false, "部门不存在"));
        }
        departmentMapper.deleteById(id);
        return ResponseEntity.ok(new DepartmentDeleteResult(true, "删除部门成功"));
    }
}
