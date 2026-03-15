package com.example.meeting.controller;

import com.example.meeting.DTO.*;
import com.example.meeting.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomService meetingRoomService;

    @PostMapping("/meeting_room/add")
    public ResponseEntity<MeetingRoomAddResult> addMeetingRoom(@RequestBody MeetingRoomAddRequest request) {
        return meetingRoomService.addMeetingRoom(request);
    }

    @GetMapping("/meeting_room/get")
    public ResponseEntity<MeetingRoomGetResult> getMeetingRoom() {
        return meetingRoomService.getMeetingRoom();
    }

    @PutMapping("/meeting_room/update")
    public ResponseEntity<MeetingRoomUpdateResult> updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest request) {
        return meetingRoomService.updateMeetingRoom(request);
    }

    @DeleteMapping("/meeting_room/delete/{id}")
    public ResponseEntity<MeetingRoomDeleteResult> deleteMeetingRoom(@PathVariable Long id) {
        return meetingRoomService.deleteMeetingRoom(id);
    }
}
