package com.vehicles.monitor;

import com.hazelcast.core.ReplicatedMap;
import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.dao.VehicleStore;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import com.vehicles.monitor.service.MonitoringServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceTests {

    YAMLConfig yamlConfig;
    MonitoringServiceImpl monitoringService;
    VehicleStore store;
    public final String COMPANY = "Alten AB";
    public final String ADDR = "kista";
    public final String VID = "known_vid";
    public final String REG_NR = "known_regNr";

    @Before
    public void setup() {
        VehicleInfoDomain vehicleInfoDomain = new VehicleInfoDomain(VID, REG_NR, COMPANY, ADDR);
        List<VehicleInfoDomain> vehicles = new ArrayList();
        vehicles.add(vehicleInfoDomain);
        Map<String, List<VehicleInfoDomain>> companyToVehicles = new HashMap<>();
        Map<String, List<VehicleInfoDomain>> vidToVehicles = new HashMap<String, List<VehicleInfoDomain>>();
        Map<String, LocalDateTime> vidToTime =  new HashMap<String, LocalDateTime>();

        vidToTime.put(VID, LocalDateTime.now().minusSeconds(10));
        companyToVehicles.put(COMPANY, vehicles);
        vidToVehicles.put(VID,  vehicles);

        loadYaml();
        store = new VehicleStore(yamlConfig, companyToVehicles, vidToVehicles, vidToTime);
        monitoringService = new MonitoringServiceImpl(store);
    }

    private void loadYaml() {
        yamlConfig = mock(YAMLConfig.class);
        when(yamlConfig.getTimeout()).thenReturn(10);
        List<Company> companies = new ArrayList<>();
        Company testCompany = mock(Company.class);
        companies.add(testCompany);
    }

    @Test
    public void updateVehicleStatusSuccessTest() {
        //given
        VehicleInfoRequest exampleReq = new VehicleInfoRequest(VID);

        //when
        int numUpdated = monitoringService.updateVehicleStatus(exampleReq);

        //then
        assertEquals(numUpdated, 1);
    }

    @Test
    public void updateVehicleStatusTestFailed() {
        //given
        VehicleInfoRequest exampleReq = new VehicleInfoRequest("an_unknown_id");

        //when
        int numUpdated = monitoringService.updateVehicleStatus(exampleReq);

        //then
        assertEquals(numUpdated, 0);
    }

    @Test
    public void getAllVehiclesTest() {
        //given
        monitoringService.updateVehicleStatus(new VehicleInfoRequest("known_vid"));

        //when
        List<VehicleInfoResp> vehicles = monitoringService.getAllVehicles();

        //then
        assertEquals(vehicles.size(), 1);
        assertEquals(vehicles.get(0).getVehicleId(),"known_vid");
        assertEquals(vehicles.get(0).getConnected(),"YES");
    }

    @Test
    public void returnVehiclesWithTimeout() {
        //given
        when(yamlConfig.getTimeout()).thenReturn(0);
        monitoringService.updateVehicleStatus(new VehicleInfoRequest("known_vid"));

        //when
        List<VehicleInfoResp> vehicles = monitoringService.getAllVehicles();

        //then
        assertEquals(vehicles.get(0).getConnected(), "NO");
    }
}
