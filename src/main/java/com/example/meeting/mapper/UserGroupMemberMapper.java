package com.example.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.meeting.entity.UserGroupMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupMemberMapper extends BaseMapper<UserGroupMember> {
}
