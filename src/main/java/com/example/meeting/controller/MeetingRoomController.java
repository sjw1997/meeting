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
        try {
            return meetingRoomService.addMeetingRoom(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "添加会议室失败"));
        }
    }

    @GetMapping("/meeting_room/get")
    public ResponseEntity<MeetingRoomGetResult> getMeetingRoom() {
        return meetingRoomService.getMeetingRoom();
    }

    @PutMapping("/meeting_room/update")
    public ResponseEntity<MeetingRoomUpdateResult> updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest request) {
        try {
            return meetingRoomService.updateMeetingRoom(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "更新会议室失败"));
        }
    }

    @DeleteMapping("/meeting_room/delete/{id}")
    public ResponseEntity<MeetingRoomDeleteResult> deleteMeetingRoom(@PathVariable Long id) {
        return meetingRoomService.deleteMeetingRoom(id);
    }

    @PostMapping("/meeting_room/forbid/{id}")
    public ResponseEntity<MeetingRoomForbiddenResult> forbidMeetingRoom(@PathVariable Long id) {
        return meetingRoomService.forbidMeetingRoom(id);
    }

    @PostMapping("/meeting_room/enable/{id}")
    public ResponseEntity<MeetingRoomEnableResult> enableMeetingRoom(@PathVariable Long id) {
        return meetingRoomService.enableMeetingRoom(id);
    }
}
