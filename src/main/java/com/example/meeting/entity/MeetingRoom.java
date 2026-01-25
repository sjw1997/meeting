package com.example.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@TableName("meeting_room")
public class MeetingRoom {
    @TableId
    private Long id;
    private String name;
    private String number;
    private String location;
    private Long capacity;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> deviceId;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> departmentId;
}
