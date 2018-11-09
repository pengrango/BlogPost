package com.vehicles.monitor.service;

import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;

import java.util.List;

public interface MonitoringService {
    public int updateVehicleStatus(VehicleInfoRequest inf);
    public List<VehicleInfoResp> getAllVehicles();
}
