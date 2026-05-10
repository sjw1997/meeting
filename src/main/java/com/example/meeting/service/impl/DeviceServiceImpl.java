package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.*;
import com.example.meeting.entity.Device;
import com.example.meeting.mapper.DeviceMapper;
import com.example.meeting.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public ResponseEntity<DeviceAddResult> addDevice(DeviceAddRequest request) {
        String name = request.getName();
        if (name == null) {
            return ResponseEntity.badRequest().body(new DeviceAddResult(false, "请填写设备名称"));
        }

        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new DeviceAddResult(false, "设备名称不能为空"));
        }

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Device existingDevice = deviceMapper.selectOne(queryWrapper);
        if (existingDevice != null) {
            return ResponseEntity.badRequest().body(new DeviceAddResult(false, "设备名称已存在"));
        }
        Device device = new Device(null, name);
        deviceMapper.insert(device);
        return ResponseEntity.ok(new DeviceAddResult(true, "添加设备成功"));
    }

    @Override
    public ResponseEntity<DeviceDeleteResult> deleteDevice(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new DeviceDeleteResult(false, "请选择设备"));
        }
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            return ResponseEntity.badRequest().body(new DeviceDeleteResult(false, "设备不存在"));
        }
        deviceMapper.deleteById(id);
        return ResponseEntity.ok(new DeviceDeleteResult(true, "删除设备成功"));
    }

    @Override
    public ResponseEntity<DeviceUpdateResult> updateDevice(DeviceUpdateRequest request) {
        Long id = request.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new DeviceUpdateResult(false, "请选择设备"));
        }
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            return ResponseEntity.badRequest().body(new DeviceUpdateResult(false, "设备不存在"));
        }
        String name = request.getName();
        if (name == null) {
            return ResponseEntity.badRequest().body(new DeviceUpdateResult(false, "请填写设备名称"));
        }
        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new DeviceUpdateResult(false, "设备名称不能为空"));
        }

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Device existingDevice = deviceMapper.selectOne(queryWrapper);
        if (existingDevice != null) {
            return ResponseEntity.badRequest().body(new DeviceUpdateResult(false, "设备名称已存在"));
        }

        if (device.getName().equals(name)) {
            return ResponseEntity.ok(new DeviceUpdateResult(true, "更新设备成功"));
        }
        device.setName(name);
        deviceMapper.updateById(device);
        return ResponseEntity.ok(new DeviceUpdateResult(true, "更新设备成功"));
    }

    @Override
    public ResponseEntity<DeviceGetResult> getDevice() {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        return ResponseEntity.ok(new DeviceGetResult(true, "获取设备成功", deviceMapper.selectList(queryWrapper)));
    }
}
