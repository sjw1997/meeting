package com.example.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithoutPassword {
    private Long id;
    private String username;
    private Boolean isAdmin;
    private String name;
    private String workNum;
    private String phoneNum;
    private Long departmentId;
}
