package com.example.meeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTokenResult {
    private Boolean success;
    private String username;
    private String userId;
    private Boolean isAdmin;
}
