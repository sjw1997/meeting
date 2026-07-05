package com.example.meeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAddRequest {
    private String name;
    private String workNum;
    private String phoneNum;
    private Long departmentId;
}
