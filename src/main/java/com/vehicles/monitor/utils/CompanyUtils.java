package com.vehicles.monitor.utils;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CompanyUtils {

    public static String statusId(String companyName, String vehicleId) {
        return companyName + "-" + vehicleId;
    }

    public static Map<String, VehicleInfoDomain> getVehiclesByCompany(Company company) {
        return  company.getVehicles().entrySet().stream().collect(Collectors.toMap(
                e -> statusId(company.getName(), e.getKey()),
                e -> new VehicleInfoDomain(e.getKey(), e.getValue(), company.getName(),company.getAddress(), LocalDateTime.MIN)
        ));
    }

    public static Map<String, VehicleInfoDomain> getInitialVehicles(YAMLConfig ymlConfig) {
        Map<String, VehicleInfoDomain> initialVehiclesMap = new ConcurrentHashMap<String, VehicleInfoDomain>();
        for (Company company : ymlConfig.getCompanies()) {
            Map<String, VehicleInfoDomain> initialVehiclesPerCompany = getVehiclesByCompany(company);
            initialVehiclesMap.putAll(initialVehiclesPerCompany);
        }
        return initialVehiclesMap;
    }
}
