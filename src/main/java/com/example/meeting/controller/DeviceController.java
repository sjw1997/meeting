package com.example.meeting.controller;

import com.example.meeting.DTO.*;
import com.example.meeting.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/device/add")
    public ResponseEntity<DeviceAddResult> addDevice(@RequestBody DeviceAddRequest request) {
        return deviceService.addDevice(request);
    }

    @GetMapping("/device/get")
    public ResponseEntity<DeviceGetResult> getDevice() {
        return deviceService.getDevice();
    }

    @PutMapping("/device/update")
    public ResponseEntity<DeviceUpdateResult> updateDevice(@RequestBody DeviceUpdateRequest request) {
        return deviceService.updateDevice(request);
    }

    @DeleteMapping("/device/delete/{id}")
    public ResponseEntity<DeviceDeleteResult> deleteDevice(@PathVariable Long id) {
        return deviceService.deleteDevice(id);
    }
}
