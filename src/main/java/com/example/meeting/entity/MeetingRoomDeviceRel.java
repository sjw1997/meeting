package com.example.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("meeting_room_device_rel")
public class MeetingRoomDeviceRel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long roomId;
    private Long deviceId;
}
