package com.vehicles.monitor;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import com.vehicles.monitor.service.MonitoringServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceTests {

    YAMLConfig yamlConfig;
    Map<String, VehicleInfoDomain> allVehicleStatus;
    MonitoringServiceImpl monitoringService;

    @Before
    public void setup() {
        allVehicleStatus = new ConcurrentHashMap<String,VehicleInfoDomain >();

        yamlConfig = mock(YAMLConfig.class);
        when(yamlConfig.getTimeout()).thenReturn(10);
        ArrayList<Company> companies = mock(ArrayList.class);
        Company company = mock(Company.class);
        Iterator<Company> companyIterator = mock(Iterator.class);

        Map<String, String> vehicles = mock(ConcurrentHashMap.class);
        when(companyIterator.hasNext()).thenReturn(true, false);
        when(companyIterator.next()).thenReturn(company);

        when(yamlConfig.getCompanies()).thenReturn(companies);
        when(companies.iterator()).thenReturn(companyIterator);
        when(company.getName()).thenReturn("Company A");
        when(company.getVehicles()).thenReturn(vehicles);
        when(vehicles.containsKey("example_vid")).thenReturn(true);
        when(vehicles.get("example_vid")).thenReturn("example_reg_nr");
        monitoringService = new MonitoringServiceImpl(allVehicleStatus, yamlConfig);
    }

    @Test
    public void updateVehicleStatusSuccessTest() {
        //given
        VehicleInfoRequest exampleReq = new VehicleInfoRequest("example_vid");

        //when
        int numUpdated = monitoringService.updateVehicleStatus(exampleReq);

        //then
        assertEquals(numUpdated, 1);
    }

    @Test
    public void updateVehicleStatusTestFailed() {
        //given
        VehicleInfoRequest exampleReq = new VehicleInfoRequest("example_vid2");

        //when
        int numUpdated = monitoringService.updateVehicleStatus(exampleReq);

        //then
        assertEquals(numUpdated, 0);
    }

    @Test
    public void getAllVehiclesTest() {
        //given
        monitoringService.updateVehicleStatus(new VehicleInfoRequest("example_vid"));

        //when
        List<VehicleInfoResp> vehicles = monitoringService.getAllVehicles();

        //then
        assertEquals(vehicles.size(), 1);
        assertEquals(vehicles.get(0).getVehicleId(),"example_vid");
        assertEquals(vehicles.get(0).getConnected(),"YES");
    }

    @Test
    public void returnVehiclesWithTimeout() {
        //given
        when(yamlConfig.getTimeout()).thenReturn(0);
        monitoringService.updateVehicleStatus(new VehicleInfoRequest("example_vid"));

        //when
        List<VehicleInfoResp> vehicles = monitoringService.getAllVehicles();

        //then
        assertEquals(vehicles.get(0).getConnected(), "NO");
    }
}
