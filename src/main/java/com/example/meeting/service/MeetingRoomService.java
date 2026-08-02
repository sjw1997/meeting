package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;

public interface MeetingRoomService {
    ResponseEntity<MeetingRoomAddResult> addMeetingRoom(MeetingRoomAddRequest request) throws Exception;
    ResponseEntity<MeetingRoomDeleteResult> deleteMeetingRoom(Long id);
    ResponseEntity<MeetingRoomUpdateResult> updateMeetingRoom(MeetingRoomUpdateRequest request) throws Exception;
    ResponseEntity<MeetingRoomGetResult> getMeetingRoom();
    ResponseEntity<MeetingRoomForbiddenResult> forbidMeetingRoom(Long id);
    ResponseEntity<MeetingRoomEnableResult> enableMeetingRoom(Long id);
}
