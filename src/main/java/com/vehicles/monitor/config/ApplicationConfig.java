package com.vehicles.monitor.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import com.vehicles.monitor.dao.VehicleStore;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.utils.StoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Autowired
    YAMLConfig ymlConfig;

    @Bean
    @Scope("singleton")
    public VehicleStore vehicleStore() {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ReplicatedMap<String, List<VehicleInfoDomain>> companyToVehicles = hz.getReplicatedMap("companyToVehicles");
        ReplicatedMap<String, List<VehicleInfoDomain>> vidToVehicles = hz.getReplicatedMap("vidToVehicles");
        ReplicatedMap<String, LocalDateTime> vidToTime = hz.getReplicatedMap("vidToTime");
        System.out.println("initialize the distributed map");
        StoreUtils.initializeVehicles(ymlConfig, vidToTime, companyToVehicles, vidToVehicles);
        return new VehicleStore(ymlConfig, companyToVehicles, vidToVehicles, vidToTime);
    }
}
