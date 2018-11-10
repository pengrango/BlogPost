package com.vehicles.monitor.config;

import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    @Autowired
    YAMLConfig ymlConfig;

    @Bean
    @Scope("singleton")
    public Map<String, VehicleInfoDomain> allVehicleStatus() {
        return getInitialVehicles();
    }

    private Map<String, VehicleInfoDomain> getInitialVehicles() {
        Map<String, VehicleInfoDomain> initialVehiclesMap = new ConcurrentHashMap<String, VehicleInfoDomain>();

        for (Company company : ymlConfig.getCompanies()) {
            Map<String, VehicleInfoDomain> initialVehiclesPerCompany = getVehiclesByCompany(company);
            initialVehiclesMap.putAll(initialVehiclesPerCompany);

        }
        return initialVehiclesMap;
    }

    private Map<String, VehicleInfoDomain> getVehiclesByCompany(Company company) {
       return  company.getVehicles().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey() + "-" + e.getValue(),
                e -> new VehicleInfoDomain(e.getKey(), e.getValue(), company.getName(),company.getAddress(), LocalDateTime.MIN)
        ));
    }

}
