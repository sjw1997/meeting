package com.example.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_group")
public class UserGroup {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long ownerUserId;
    private String name;
    private String description;
}
