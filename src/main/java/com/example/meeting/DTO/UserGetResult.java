package com.example.meeting.DTO;


import com.example.meeting.entity.UserWithoutPassword;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserGetResult {
    private Boolean success;
    private String message;
    private List<UserWithoutPassword> users;
}
