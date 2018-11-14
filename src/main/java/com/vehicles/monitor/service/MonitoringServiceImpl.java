package com.vehicles.monitor.service;

import com.vehicles.monitor.UnknownQueryException;
import com.vehicles.monitor.dao.VehicleStore;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringServiceImpl implements MonitoringService {

//    private Map<String, VehicleInfoDomain> allVehicleStatus;
//    private YAMLConfig ymlConfig;
    private VehicleStore store;
    //vehicleStore

    @Autowired
    public MonitoringServiceImpl(VehicleStore store) {
        this.store = store;
    }

    @Override
    public int updateVehicleStatus(VehicleInfoRequest infoReq) {
        if (!store.getVidToTime().containsKey(infoReq.getVehicleId())) {
            return 0;
        }
        store.updateVehicle(infoReq.getVehicleId());
        return store.getVidToVehicles().get(infoReq.getVehicleId()).size();
    }


    @Override
    public List<VehicleInfoResp> getAllVehicles() {
        return store.getAllVehicles();
//        return allVehicleStatus.values().stream()
//                .map(vehicleInfoDomain -> VehicleInfoResp.fromDomain(vehicleInfoDomain, LocalDateTime.now(), ymlConfig.getTimeout()))
//                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleInfoResp> getVehicles(String field, String value) throws UnknownQueryException {
        switch (field) {
            case "companyName" :
                return store.getVehiclesByCompanyName(value);
            case "isConnected" :
                return store.getVehiclesByStatus(value);
            default:
                throw new UnknownQueryException("Can not add filter for field: " + field + " , value: " + value);
        }
    }
}
