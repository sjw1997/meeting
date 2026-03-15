package com.example.meeting.DTO;

import com.example.meeting.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeviceGetResult {
    private boolean success;
    private String message;
    private List<Device> devices;
}
