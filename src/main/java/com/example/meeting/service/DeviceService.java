package com.example.meeting.service;

import com.example.meeting.DTO.*;
import org.springframework.http.ResponseEntity;

public interface DeviceService {
    ResponseEntity<DeviceAddResult> addDevice(DeviceAddRequest request);
    ResponseEntity<DeviceDeleteResult> deleteDevice(Long id);
    ResponseEntity<DeviceUpdateResult> updateDevice(DeviceUpdateRequest request);
    ResponseEntity<DeviceGetResult> getDevice();
}
