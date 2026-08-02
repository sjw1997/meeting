package com.example.meeting.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private List<Long> memberIds;
}
