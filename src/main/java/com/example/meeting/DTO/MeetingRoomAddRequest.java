package com.example.meeting.DTO;

import lombok.Data;

import java.util.List;

@Data
public class MeetingRoomAddRequest {
    private String name;
    private String number;
    private String location;
    private Long capacity;
    private List<Long> deviceIds;
    private List<Long> departmentIds;
}
