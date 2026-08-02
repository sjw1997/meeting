package com.example.meeting.controller;

import com.example.meeting.DTO.*;
import com.example.meeting.config.JwtUtil;
import com.example.meeting.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserGroupController {
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/user_group/add")
    public ResponseEntity<UserGroupAddResult> addUserGroup(
        @RequestBody UserGroupAddRequest request,
        @RequestHeader("Authorization") String header
    ) {
        try {
            return userGroupService.addUserGroup(request, jwtUtil.getToken(header));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserGroupAddResult(false, "添加群组失败"));
        }
    }

    @GetMapping("/user_group/get")
    public ResponseEntity<UserGroupGetResult> getUserGroup(
        @RequestHeader("Authorization") String header
    ) {
        return userGroupService.getUserGroup(jwtUtil.getToken(header));
    }

    @DeleteMapping("/user_group/delete/{id}")
    public ResponseEntity<UserGroupDeleteResult> deleteUserGroup(
        @PathVariable Long id,
        @RequestHeader("Authorization") String header
    ) {
        return userGroupService.deleteUserGroup(id, jwtUtil.getToken(header));
    }

    @PutMapping("/user_group/update")
    public ResponseEntity<UserGroupUpdateResult> updateUserGroup(
        @RequestBody UserGroupUpdateRequest request,
        @RequestHeader("Authorization") String header
    ) {
        return userGroupService.updateUserGroup(request, jwtUtil.getToken(header));
    }
}
