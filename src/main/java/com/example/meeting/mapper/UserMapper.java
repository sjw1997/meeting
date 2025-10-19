package com.example.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.meeting.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}