package com.vehicles.monitor.service;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private Map<String, VehicleInfoDomain> allVehicleStatus;
    private YAMLConfig ymlConfig;

    @Autowired
    public MonitoringServiceImpl(Map<String, VehicleInfoDomain> allVehicleStatus, YAMLConfig ymlConfig) {
        this.allVehicleStatus = allVehicleStatus;
        this.ymlConfig = ymlConfig;
    }

    @Override
    public int updateVehicleStatus(VehicleInfoRequest infoReq) {
        List<Company> companies = ymlConfig.getCompanies();
        LocalDateTime now = LocalDateTime.now();
        return companies.stream().filter(company -> updateOneCompany(company, infoReq, now) > 0).collect(Collectors.toList()).size();
    }

    private int updateOneCompany(Company company, VehicleInfoRequest infoReq, LocalDateTime now) {
        Map<String, String> vehicles = company.getVehicles();
        String vehicleId = infoReq.getVehicleId();
        if (vehicles.containsKey(vehicleId)) {
            allVehicleStatus.put(statusId(company.getName(), vehicleId), new VehicleInfoDomain(vehicleId, vehicles.get(vehicleId), company.getName(), company.getAddress(), now));
            return 1;
        }
        return 0;
    }

    private String statusId(String companyName, String vehicleId) {
        return companyName + "-" + vehicleId;
    }

    @Override
    public List<VehicleInfoResp> getAllVehicles() {
        return allVehicleStatus.values().stream()
                .map(vehicleInfoDomain -> VehicleInfoResp.fromDomain(vehicleInfoDomain, LocalDateTime.now(), ymlConfig.getTimeout()))
                .collect(Collectors.toList());
    }
}
