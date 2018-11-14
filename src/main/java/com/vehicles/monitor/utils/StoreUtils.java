package com.vehicles.monitor.utils;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StoreUtils {

    public static String statusId(String companyName, String vehicleId) {
        return companyName + "-" + vehicleId;
    }

    public static void initializeVehiclesByCompany(Company company,
                                                   Map<String, LocalDateTime> vidToTime,
                                                   Map<String, List<VehicleInfoDomain>> companyToVehicles,
                                                   Map<String, List<VehicleInfoDomain>> vidToVehicles) {
        Map<String, String> vehicles = company.getVehicles();

        for (String vid : vehicles.keySet()) {
            String regNr = vehicles.get(vid);
            VehicleInfoDomain vehicleInfoDomain = new VehicleInfoDomain(vid, regNr, company.getName(), company.getAddress());
            vidToTime.putIfAbsent(vid, LocalDateTime.MIN);
            initCompanyToVehicles(companyToVehicles, vehicleInfoDomain);
            initVidToVehicles(vidToVehicles, vehicleInfoDomain);
        }
    }

    private static void initCompanyToVehicles(Map<String, List<VehicleInfoDomain>> companyToVehicles, VehicleInfoDomain vehicleInfoDomain) {
        if (!companyToVehicles.containsKey(vehicleInfoDomain.getCompanyName())) {
            companyToVehicles.putIfAbsent(vehicleInfoDomain.getCompanyName(), new ArrayList<>());
        }
        companyToVehicles.get(vehicleInfoDomain.getCompanyName()).add(vehicleInfoDomain);
    }

    private static void initVidToVehicles(Map<String, List<VehicleInfoDomain>> vidToVehicles, VehicleInfoDomain vehicleInfoDomain) {
        if (!vidToVehicles.containsKey(vehicleInfoDomain.getVehicleId())) {
            vidToVehicles.putIfAbsent(vehicleInfoDomain.getVehicleId(), new ArrayList<>());
        }
        vidToVehicles.get(vehicleInfoDomain.getVehicleId()).add(vehicleInfoDomain);
    }

    public static void initializeVehicles(YAMLConfig ymlConfig,
                                          Map<String, LocalDateTime> vidToTime,
                                          Map<String, List<VehicleInfoDomain>> companyToVehicles,
                                          Map<String, List<VehicleInfoDomain>> vidToVehicles) {
        for (Company company : ymlConfig.getCompanies()) {
            initializeVehiclesByCompany(company, vidToTime, companyToVehicles, vidToVehicles);
        }
    }
}
