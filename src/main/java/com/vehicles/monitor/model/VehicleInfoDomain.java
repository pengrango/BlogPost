package com.vehicles.monitor.model;

import java.time.LocalDateTime;

public class VehicleInfoDomain {
    private String vehicleId;
    private String regNr;
    private String companyName;
    private String companyAddress;
    private LocalDateTime lastUpdate;

    public VehicleInfoDomain(String vehicleId, String regNr, String companyName, String companyAddress, LocalDateTime lastUpdate) {
        this.vehicleId = vehicleId;
        this.regNr = regNr;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.lastUpdate = lastUpdate;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getRegNr() {
        return regNr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
