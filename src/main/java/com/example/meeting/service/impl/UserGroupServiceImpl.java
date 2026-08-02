package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.*;
import com.example.meeting.config.JwtUtil;
import com.example.meeting.entity.UserGroup;
import com.example.meeting.entity.UserGroupFullDTO;
import com.example.meeting.entity.UserGroupMember;
import com.example.meeting.mapper.UserGroupMapper;
import com.example.meeting.mapper.UserGroupMemberMapper;
import com.example.meeting.mapper.UserMapper;
import com.example.meeting.service.UserGroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserGroupMemberMapper userGroupMemberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<UserGroupAddResult> addUserGroup(UserGroupAddRequest request, String token) {
        CheckResult nameCheckResult = checkName(request.getName());
        if (!nameCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupAddResult(false, nameCheckResult.message));
        }

        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        List<UserGroup> userGroups = userGroupMapper.selectList(
            new QueryWrapper<UserGroup>().eq("owner_user_id", userId)
        );
        for (UserGroup userGroup : userGroups) {
            if (userGroup.getName().equals(request.getName())) {
                return ResponseEntity.badRequest().body(new UserGroupAddResult(false, "群组名称已存在"));
            }
        }

        CheckResult descriptionCheckResult = checkDescription(request.getDescription());
        if (!descriptionCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupAddResult(false, descriptionCheckResult.message));
        }

        CheckResult memberIdsCheckResult = checkMemberIds(request.getMemberIds());
        if (!memberIdsCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupAddResult(false, memberIdsCheckResult.message));
        }

        UserGroup userGroup = new UserGroup(null, userId, request.getName(), request.getDescription());
        userGroupMapper.insert(userGroup);
        for (Long memberId : request.getMemberIds()) {
            userGroupMemberMapper.insert(new UserGroupMember(null, userGroup.getId(), memberId));
        }
        return ResponseEntity.ok(new UserGroupAddResult(true, "添加群组成功"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<UserGroupDeleteResult> deleteUserGroup(Long id, String token) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new UserGroupDeleteResult(false, "群组ID不能为空"));
        }
        if (userGroupMapper.selectById(id) == null) {
            return ResponseEntity.badRequest().body(new UserGroupDeleteResult(false, "群组不存在"));
        }

        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        List<UserGroup> userGroups = userGroupMapper.selectList(
            new QueryWrapper<UserGroup>().eq("owner_user_id", userId)
        );
        if (userGroups.stream().noneMatch(userGroup -> userGroup.getId().equals(id))) {
            return ResponseEntity.badRequest().body(new UserGroupDeleteResult(false, "群组不存在"));
        }

        userGroupMapper.deleteById(id);
        List<UserGroupMember> userGroupMembers = userGroupMemberMapper.selectList(
            new QueryWrapper<UserGroupMember>().eq("group_id", id)
        );
        for (UserGroupMember userGroupMember : userGroupMembers) {
            userGroupMemberMapper.deleteById(userGroupMember.getId());
        }
        return ResponseEntity.ok(new UserGroupDeleteResult(true, "删除群组成功"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<UserGroupUpdateResult> updateUserGroup(UserGroupUpdateRequest request, String token) {
        CheckResult nameCheckResult = checkName(request.getName());
        if (!nameCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupUpdateResult(false, nameCheckResult.message));
        }

        CheckResult descriptionCheckResult = checkDescription(request.getDescription());
        if (!descriptionCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupUpdateResult(false, descriptionCheckResult.message));
        }

        CheckResult memberIdsCheckResult = checkMemberIds(request.getMemberIds());
        if (!memberIdsCheckResult.isValid) {
            return ResponseEntity.badRequest().body(new UserGroupUpdateResult(false, memberIdsCheckResult.message));
        }

        if (request.getId() == null || userGroupMapper.selectById(request.getId()) == null) {
            return ResponseEntity.badRequest().body(new UserGroupUpdateResult(false, "群组不存在"));
        }

        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        List<UserGroup> userGroups = userGroupMapper.selectList(
            new QueryWrapper<UserGroup>().eq("owner_user_id", userId)
        );
        if (userGroups.stream().noneMatch(userGroup -> userGroup.getId().equals(request.getId()))) {
            return ResponseEntity.badRequest().body(new UserGroupUpdateResult(false, "群组不存在"));
        }

        UserGroup userGroup = new UserGroup(request.getId(), userId, request.getName(), request.getDescription());
        userGroupMapper.updateById(userGroup);
        List<UserGroupMember> userGroupMembers = userGroupMemberMapper.selectList(
            new QueryWrapper<UserGroupMember>().eq("group_id", request.getId())
        );
        for (UserGroupMember userGroupMember : userGroupMembers) {
            userGroupMemberMapper.deleteById(userGroupMember.getId());
        }
        for (Long memberId : request.getMemberIds()) {
            userGroupMemberMapper.insert(new UserGroupMember(null, request.getId(), memberId));
        }
        return ResponseEntity.ok(new UserGroupUpdateResult(true, "更新群组成功"));
    }

    @Override
    public ResponseEntity<UserGroupGetResult> getUserGroup(String token) {
        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        List<UserGroup> userGroups = userGroupMapper.selectList(
            new QueryWrapper<UserGroup>().eq("owner_user_id", userId)
        );
        return ResponseEntity.ok(new UserGroupGetResult(true, "获取群组成功", userGroups.stream().map(userGroup -> new UserGroupFullDTO(
            userGroup,
            userGroupMemberMapper.selectList(
                new QueryWrapper<UserGroupMember>().eq("group_id", userGroup.getId())
            ).stream().map(UserGroupMember::getMemberId).toList()
        )).toList()));
    }

    @AllArgsConstructor
    private static class CheckResult {
        boolean isValid;
        String message;
    }

    private CheckResult checkName(String name) {
        if (name == null || name.isEmpty()) {
            return new CheckResult(false, "群组名称不能为空");
        }
        if (name.length() > 20) {
            return new CheckResult(false, "群组名称过长");
        }
        return new CheckResult(true, "");
    }

    private CheckResult checkDescription(String description) {
        if (description != null && description.length() > 50) {
            return new CheckResult(false, "群组描述过长");
        }
        return new CheckResult(true, "");
    }

    private CheckResult checkMemberIds(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            if (userMapper.selectById(memberId) == null) {
                return new CheckResult(false, "群组成员不存在");
            }
        }
        return new CheckResult(true, "");
    }
}
