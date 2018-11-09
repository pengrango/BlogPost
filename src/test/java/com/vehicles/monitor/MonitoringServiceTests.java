package com.vehicles.monitor;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import com.vehicles.monitor.service.MonitoringServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceTests {

    YAMLConfig yamlConfig;
    Map<String, VehicleInfoDomain> allVehicleStatus;
    MonitoringServiceImpl monitoringService;

    @Before
    public void setup() {
        allVehicleStatus = new ConcurrentHashMap<String,VehicleInfoDomain >();
        yamlConfig = loadYaml();
        monitoringService = new MonitoringServiceImpl(allVehicleStatus, this.yamlConfig);
    }

    private YAMLConfig loadYaml() {
        Yaml yaml = new Yaml();
        InputStream in = YAMLConfig.class.getClassLoader().getResourceAsStream("test-application.yml");
        return yaml.loadAs(in, YAMLConfig.class);
    }

    @Test
    public void updateVehicleStatusSuccessTest() {
        //given
        VehicleInfoRequest exampleReq = new VehicleInfoRequest("known_vid");

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
//        when(yamlConfig.getTimeout()).thenReturn(0);
        yamlConfig.setTimeout(0);
        monitoringService.updateVehicleStatus(new VehicleInfoRequest("known_vid"));

        //when
        List<VehicleInfoResp> vehicles = monitoringService.getAllVehicles();

        //then
        assertEquals(vehicles.get(0).getConnected(), "NO");
    }
}
