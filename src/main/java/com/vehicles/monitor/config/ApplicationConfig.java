package com.vehicles.monitor.config;

import com.vehicles.monitor.model.VehicleInfoDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

import static com.vehicles.monitor.utils.CompanyUtils.getInitialVehicles;

@Configuration
public class ApplicationConfig {

    @Autowired
    YAMLConfig ymlConfig;

    @Bean
    @Scope("singleton")
    public Map<String, VehicleInfoDomain> allVehicleStatus() {
        return getInitialVehicles(ymlConfig);
    }



}
