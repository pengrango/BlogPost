package com.vehicles.monitor;

import com.vehicles.monitor.controller.MonitorController;
import com.vehicles.monitor.model.VehicleInfoRequest;
import com.vehicles.monitor.model.VehicleInfoResp;
import com.vehicles.monitor.service.MonitoringService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorControllerTest {
    MockMvc mockMvc;

    @Autowired
    MonitorController monitorController;

    @MockBean
    MonitoringService monitoringService;

    private List<VehicleInfoResp> vehicles;

    private final String VEHICLE_ID = "test_id";
    private final String REG_NR = "regNr";
    private final String COMPANY_NAME = "alten";
    private final String ADDRESS = "stockholm";
    private final String CONNECTED = "YES";

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(this.monitorController).build();
        vehicles = new ArrayList<>();
        vehicles.add(new VehicleInfoResp(VEHICLE_ID, REG_NR, COMPANY_NAME, ADDRESS, CONNECTED));
    }

    @Test
    public void getAllVehiclesTest() throws Exception {
        //when
        //given
       when(monitoringService.getAllVehicles()).thenReturn(vehicles);

       //then
       mockMvc.perform(get("/allvehicles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].vehicleId", is(VEHICLE_ID)))
               .andExpect(jsonPath("$[0].companyName", is(COMPANY_NAME)))
               .andExpect(jsonPath("$[0].companyAddress", is(ADDRESS)))
               .andExpect(jsonPath("$[0].connected", is(CONNECTED)));
    }

    @Test
    public void updateVehicleInfoTest() throws Exception {
        //when
        //given
        when(monitoringService.updateVehicleStatus(any(VehicleInfoRequest.class))).thenReturn(1);

        //then
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vehicleId\":\"VLUR4X20009048066\"}"))
        .andExpect(status().isOk());
    }

}
