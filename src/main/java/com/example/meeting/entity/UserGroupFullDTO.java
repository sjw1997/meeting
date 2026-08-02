package com.example.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserGroupFullDTO extends UserGroup {
    private List<Long> memberIds;

    public UserGroupFullDTO(UserGroup userGroup, List<Long> memberIds) {
        super(userGroup.getId(), userGroup.getOwnerUserId(), userGroup.getName(), userGroup.getDescription());
        this.memberIds = memberIds;
    }
}
