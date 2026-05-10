package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.*;
import com.example.meeting.entity.MeetingRoom;
import com.example.meeting.entity.MeetingRoomDepartmentRel;
import com.example.meeting.entity.MeetingRoomDeviceRel;
import com.example.meeting.entity.MeetingRoomFullDTO;
import com.example.meeting.mapper.*;
import com.example.meeting.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private MeetingRoomDeviceRelMapper meetingRoomDeviceRelMapper;

    @Autowired
    private MeetingRoomDepartmentRelMapper meetingRoomDepartmentRelMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<MeetingRoomAddResult> addMeetingRoom(MeetingRoomAddRequest request) {
        String name = request.getName();
        if (name == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "请填写会议室名称"));
        }
        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室名称不能为空"));
        }

        QueryWrapper<MeetingRoom> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name", name);
        MeetingRoom existingMeetingRoom = meetingRoomMapper.selectOne(queryWrapper1);
        if (existingMeetingRoom != null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室名称已存在"));
        }

        String number = request.getNumber();
        if (number == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "请填写会议室编号"));
        }
        number = number.trim();
        if (number.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室编号不能为空"));
        }

        QueryWrapper<MeetingRoom> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("number", number);
        MeetingRoom existingMeetingRoom2 = meetingRoomMapper.selectOne(queryWrapper2);
        if (existingMeetingRoom2 != null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室编号已存在"));
        }

        String location = request.getLocation();
        if (location == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "请填写会议室位置"));
        }
        location = location.trim();
        if (location.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室位置不能为空"));
        }

        Long capacity = request.getCapacity();
        if (capacity == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "请填写会议室容量"));
        }
        if (capacity <= 0) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "会议室容量必须大于0"));
        }

        List<Long> deviceIds = request.getDeviceIds();
        if (deviceIds == null) {
            deviceIds = new ArrayList<>();
        }
        if (!deviceIds.isEmpty() && deviceIds.size() != deviceMapper.selectBatchIds(deviceIds).size()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "选择的设备不存在"));
        }

        List<Long> departmentIds = request.getDepartmentIds();
        if (departmentIds == null || departmentIds.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "请选择会议室部门"));
        }
        if (departmentIds.size() != departmentMapper.selectBatchIds(departmentIds).size()) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "选择的部门不存在"));
        }

        try {
            MeetingRoom meetingRoom = new MeetingRoom(null, name, number, location, capacity, true);
            meetingRoomMapper.insert(meetingRoom);

            Long meetingRoomId = meetingRoom.getId();

            for (Long deviceId : deviceIds) {
                if (deviceId == null) {
                    throw new Exception("设备ID不能为空");
                }
                meetingRoomDeviceRelMapper.insert(new MeetingRoomDeviceRel(null, meetingRoomId, deviceId));
            }

            for (Long departmentId : departmentIds) {
                if (departmentId == null) {
                    throw new Exception("部门ID不能为空");
                }
                meetingRoomDepartmentRelMapper.insert(new MeetingRoomDepartmentRel(null, meetingRoomId, departmentId));
            }

            return ResponseEntity.ok(new MeetingRoomAddResult(true, "添加会议室成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MeetingRoomAddResult(false, "添加会议室失败"));
        }
    }

    @Override
    public ResponseEntity<MeetingRoomDeleteResult> deleteMeetingRoom(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomDeleteResult(false, "请选择会议室"));
        }
        if (meetingRoomMapper.selectById(id) == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomDeleteResult(false, "会议室不存在"));
        }
        return meetingRoomMapper.deleteById(id) > 0 ?
                ResponseEntity.ok(new MeetingRoomDeleteResult(true, "删除会议室成功")) :
                ResponseEntity.badRequest().body(new MeetingRoomDeleteResult(false, "删除会议室失败"));
    }

    @Override
    public ResponseEntity<MeetingRoomUpdateResult> updateMeetingRoom(MeetingRoomUpdateRequest request) {
        Long id = request.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请选择会议室"));
        }
        MeetingRoom meetingRoom = meetingRoomMapper.selectById(id);
        if (meetingRoom == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室不存在"));
        }

        String name = request.getName();
        if (name == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请填写会议室名称"));
        }
        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室名称不能为空"));
        }

        if (!meetingRoom.getName().equals(name)) {
            QueryWrapper<MeetingRoom> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("name", name);
            List<MeetingRoom> existingMeetingRooms = meetingRoomMapper.selectList(queryWrapper1);
            if (existingMeetingRooms != null) {
                return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室名称已存在"));
            }
        }

        String number = request.getNumber();
        if (number == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请填写会议室编号"));
        }
        number = number.trim();
        if (number.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室编号不能为空"));
        }
        if (!meetingRoom.getNumber().equals(number)) {
            QueryWrapper<MeetingRoom> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("number", number);
            MeetingRoom existingMeetingRoom2 = meetingRoomMapper.selectOne(queryWrapper2);
            if (existingMeetingRoom2 != null) {
                return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室编号已存在"));
            }
        }

        String location = request.getLocation();
        if (location == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请填写会议室位置"));
        }
        location = location.trim();
        if (location.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室位置不能为空"));
        }

        Long capacity = request.getCapacity();
        if (capacity == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请填写会议室容量"));
        }
        if (capacity <= 0) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "会议室容量必须大于0"));
        }

        List<Long> deviceIds = request.getDeviceIds();
        if (deviceIds == null) {
            deviceIds = new ArrayList<>();
        }

        if (!deviceIds.isEmpty() && deviceIds.size() != deviceMapper.selectBatchIds(deviceIds).size()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "选择的设备不存在"));
        }

        List<Long> departmentIds = request.getDepartmentIds();
        if (departmentIds == null || departmentIds.isEmpty()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "请选择会议室部门"));
        }
        if (departmentIds.size() != departmentMapper.selectBatchIds(departmentIds).size()) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "选择的部门不存在"));
        }

        try {
            meetingRoom.setCapacity(capacity);
            meetingRoom.setName(name);
            meetingRoom.setLocation(location);
            meetingRoom.setCapacity(capacity);
            meetingRoomMapper.updateById(meetingRoom);

            Long meetingRoomId = meetingRoom.getId();

            meetingRoomDeviceRelMapper.delete(new QueryWrapper<MeetingRoomDeviceRel>().eq("room_id", meetingRoomId));
            for (Long deviceId : deviceIds) {
                if (deviceId == null) {
                    throw new Exception("设备ID不能为空");
                }
                meetingRoomDeviceRelMapper.insert(new MeetingRoomDeviceRel(null, meetingRoomId, deviceId));
            }

            meetingRoomDepartmentRelMapper.delete(new QueryWrapper<MeetingRoomDepartmentRel>().eq("room_id", meetingRoomId));
            for (Long departmentId : departmentIds) {
                if (departmentId == null) {
                    throw new Exception("部门ID不能为空");
                }
                meetingRoomDepartmentRelMapper.insert(new MeetingRoomDepartmentRel(null, meetingRoomId, departmentId));
            }

            return ResponseEntity.ok(new MeetingRoomUpdateResult(true, "更新会议室成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MeetingRoomUpdateResult(false, "更新会议室失败"));
        }
    }

    @Override
    public ResponseEntity<MeetingRoomGetResult> getMeetingRoom() {
        List<MeetingRoomFullDTO> meetingRoomFullDTOS = new ArrayList<>();

        List<MeetingRoom> meetingRooms = meetingRoomMapper.selectList(null);
        for (MeetingRoom meetingRoom : meetingRooms) {
            QueryWrapper<MeetingRoomDeviceRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", meetingRoom.getId());
            List<Long> deviceIds = meetingRoomDeviceRelMapper.selectList(queryWrapper).stream().map(MeetingRoomDeviceRel::getDeviceId).toList();

            QueryWrapper<MeetingRoomDepartmentRel> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("room_id", meetingRoom.getId());
            List<Long> departmentIds = meetingRoomDepartmentRelMapper.selectList(queryWrapper1).stream().map(MeetingRoomDepartmentRel::getDepartmentId).toList();

            meetingRoomFullDTOS.add(new MeetingRoomFullDTO(meetingRoom, deviceIds, departmentIds));
        }

        return ResponseEntity.ok(new MeetingRoomGetResult(true, "获取会议室成功", meetingRoomFullDTOS));
    }

    @Override
    public ResponseEntity<MeetingRoomForbiddenResult> forbidMeetingRoom(Long id) {
        MeetingRoom meetingRoom = meetingRoomMapper.selectById(id);
        if (meetingRoom == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomForbiddenResult(false, "选择的会议室不存在"));
        }
        meetingRoom.setAvailable(false);
        meetingRoomMapper.updateById(meetingRoom);
        return ResponseEntity.ok(new MeetingRoomForbiddenResult(true, "禁用成功"));
    }

    @Override
    public ResponseEntity<MeetingRoomEnableResult> enableMeetingRoom(Long id) {
        MeetingRoom meetingRoom = meetingRoomMapper.selectById(id);
        if (meetingRoom == null) {
            return ResponseEntity.badRequest().body(new MeetingRoomEnableResult(false, "选择的会议室不存在"));
        }
        meetingRoom.setAvailable(true);
        meetingRoomMapper.updateById(meetingRoom);
        return ResponseEntity.ok(new MeetingRoomEnableResult(true, "启用成功"));
    }
}
