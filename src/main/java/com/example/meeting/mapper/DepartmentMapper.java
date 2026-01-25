package com.example.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.meeting.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
