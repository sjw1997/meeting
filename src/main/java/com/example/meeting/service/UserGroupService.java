package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;

public interface UserGroupService {
    ResponseEntity<UserGroupAddResult> addUserGroup(UserGroupAddRequest request, String token);
    ResponseEntity<UserGroupDeleteResult> deleteUserGroup(Long id, String token);
    ResponseEntity<UserGroupUpdateResult> updateUserGroup(UserGroupUpdateRequest request, String token);
    ResponseEntity<UserGroupGetResult> getUserGroup(String token);
}
