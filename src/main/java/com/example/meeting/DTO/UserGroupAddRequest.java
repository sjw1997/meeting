package com.example.meeting.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupAddRequest {
    private String name;
    private String description;
    private List<Long> memberIds;
}
