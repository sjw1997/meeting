package com.example.meeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGroupDeleteResult {
    private Boolean success;
    private String message;
}
