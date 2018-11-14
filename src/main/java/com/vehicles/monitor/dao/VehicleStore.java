package com.vehicles.monitor.dao;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.vehicles.monitor.UnknownQueryException;
import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.model.VehicleInfoResp;
import com.vehicles.monitor.utils.StoreUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleStore {
    private Map<String, List<VehicleInfoDomain>> companyToVehicles ;
    private Map<String, List<VehicleInfoDomain>> vidToVehicles;
    private Map<String, LocalDateTime> vidToTime;
    private YAMLConfig config;
    @Autowired
    public VehicleStore (YAMLConfig config,
                          Map<String, List<VehicleInfoDomain>> companyToVehicles,
                          Map<String, List<VehicleInfoDomain>> vidToVehicles,
                          Map<String, LocalDateTime> vidToTime) {

        this.companyToVehicles = companyToVehicles;//
        this.vidToVehicles = vidToVehicles;
        this.vidToTime = vidToTime;
        this.config = config;
    }

    public void updateVehicle(String vid) {
        if (vidToTime.containsKey(vid)) {
            vidToTime.put(vid, LocalDateTime.now());
        }
    }

    public List<VehicleInfoResp> getAllVehicles() {
        LocalDateTime now = LocalDateTime.now();
        List<VehicleInfoResp> allVehicles = new ArrayList();
        for (String vid : vidToVehicles.keySet()) {
            List<VehicleInfoDomain> vehicleByCompany = vidToVehicles.get(vid);
            LocalDateTime lastUpdate = vidToTime.get(vid);
            for (VehicleInfoDomain vehicleDomain : vehicleByCompany) {
                allVehicles.add(VehicleInfoResp.fromDomain(vehicleDomain, isConnected(lastUpdate, now, config.getTimeout())));
            }
        }
        return allVehicles;
    }

    public List<VehicleInfoResp> getVehiclesByCompanyName(String companyName) {
        LocalDateTime now = LocalDateTime.now();
        List<VehicleInfoResp> vehiclesResp = new ArrayList<>();
        if(companyToVehicles.containsKey(companyName)) {
            List<VehicleInfoDomain> vehicleInfoDomains = companyToVehicles.get(companyName);
            for (VehicleInfoDomain vehicleDomain : vehicleInfoDomains) {
                LocalDateTime lastUpdate = vidToTime.get(vehicleDomain.getVehicleId());
                vehiclesResp.add(VehicleInfoResp.fromDomain(vehicleDomain, isConnected(lastUpdate, now, config.getTimeout())));
            }
        }
        return vehiclesResp;
    }

    public List<VehicleInfoResp> getVehiclesByStatus(String status) throws UnknownQueryException {
        LocalDateTime now = LocalDateTime.now();
//        List<String> vids = vidToTime.keySet().stream().filter(vid -> isSelected(status, vidToTime.get(vid), now)).collect(Collectors.toList());
        if (!"YES".equalsIgnoreCase(status) && !"NO".equalsIgnoreCase(status)) {
            throw new UnknownQueryException("Can't recognise the status value: " + status);
        }
        List<VehicleInfoResp> vehiclesResp = new ArrayList<>();
        for (String vid : vidToTime.keySet()) {
            if (isSelected(status, vidToTime.get(vid), now)) {
                List<VehicleInfoDomain> vehicleInfoDomains = vidToVehicles.get(vid);
                addVehicleResps(vehiclesResp, vehicleInfoDomains, status);
            }
        }
        return vehiclesResp;
    }

    private void addVehicleResps(List<VehicleInfoResp> vehiclesResp, List<VehicleInfoDomain> vehicleInfoDomains, String status) {
        for (VehicleInfoDomain vehicleInfoDomain : vehicleInfoDomains) {
            vehiclesResp.add(VehicleInfoResp.fromDomain(vehicleInfoDomain, status));
        }
    }

    private boolean isSelected(String status, LocalDateTime lastUpdate, LocalDateTime now) {
        return status.equalsIgnoreCase("YES")
                ? isConnected(lastUpdate,  now, config.getTimeout()) :  !isConnected(lastUpdate,  now, config.getTimeout());
    }

    private boolean isConnected(LocalDateTime lastUpdate, LocalDateTime now, int timeout) {
        return now.minusSeconds(timeout).isBefore(lastUpdate);

    }

    public Map<String, List<VehicleInfoDomain>> getCompanyToVehicles() {
        return companyToVehicles;
    }

    public Map<String, List<VehicleInfoDomain>> getVidToVehicles() {
        return vidToVehicles;
    }

    public Map<String, LocalDateTime> getVidToTime() {
        return vidToTime;
    }
}
