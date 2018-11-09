package com.vehicles.monitor.model;

import java.util.Map;

public class Company {
    private String name;
    private String address;
    private Map<String, String> vehicles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<String, String> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", vehicles=" + vehicles +
                '}';
    }
}
