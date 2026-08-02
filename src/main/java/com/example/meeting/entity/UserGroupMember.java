package com.example.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("user_group_member")
public class UserGroupMember {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long groupId;
    private Long memberId;
}
