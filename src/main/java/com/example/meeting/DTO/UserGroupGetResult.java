package com.example.meeting.DTO;

import com.example.meeting.entity.UserGroupFullDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserGroupGetResult {
    private Boolean success;
    private String message;
    private List<UserGroupFullDTO> userGroups;
}
