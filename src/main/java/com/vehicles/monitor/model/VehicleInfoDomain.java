package com.vehicles.monitor.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VehicleInfoDomain implements Serializable {
    private String vehicleId;
    private String regNr;
    private String companyName;
    private String companyAddress;

    public VehicleInfoDomain(String vehicleId, String regNr, String companyName, String companyAddress) {
        this.vehicleId = vehicleId;
        this.regNr = regNr;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
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

}
