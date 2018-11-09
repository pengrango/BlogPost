package com.vehicles.monitor.model;

public class VehicleInfoRequest {
    private String vehicleId;

    public VehicleInfoRequest(){}

    public VehicleInfoRequest(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }
}
