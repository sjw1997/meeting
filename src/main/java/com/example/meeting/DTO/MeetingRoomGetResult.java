package com.example.meeting.DTO;

import com.example.meeting.entity.MeetingRoomFullDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetingRoomGetResult {
    private Boolean success;
    private String message;
    private List<MeetingRoomFullDTO> meetingRooms;
}
