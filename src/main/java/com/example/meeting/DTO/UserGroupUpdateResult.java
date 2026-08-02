package com.example.meeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGroupUpdateResult {
    private Boolean success;
    private String message;
}
