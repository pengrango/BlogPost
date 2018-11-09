package com.vehicles.monitor.config;

import com.vehicles.monitor.model.VehicleInfoDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ApplicationConfig {

    @Bean
    @Scope("singleton")
    public Map<String, VehicleInfoDomain> allVehicleStatus() {
        Map<String, VehicleInfoDomain> vehiclesMap = new ConcurrentHashMap<String, VehicleInfoDomain>();
        return vehiclesMap;
    }

}
