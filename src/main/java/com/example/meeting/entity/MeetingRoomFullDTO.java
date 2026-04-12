package com.example.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MeetingRoomFullDTO extends MeetingRoom {
    private Boolean isFree = true;
    private List<Long> deviceIds;
    private List<Long> departmentIds;

    public MeetingRoomFullDTO(MeetingRoom meetingRoom, List<Long> deviceIds, List<Long> departmentIds) {
        super(meetingRoom.getId(), meetingRoom.getName(), meetingRoom.getNumber(), meetingRoom.getLocation(), meetingRoom.getCapacity(), meetingRoom.getAvailable());
        this.deviceIds = deviceIds;
        this.departmentIds = departmentIds;
    }
}
