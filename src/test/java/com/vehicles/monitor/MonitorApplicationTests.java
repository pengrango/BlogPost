package com.vehicles.monitor;

import com.vehicles.monitor.controller.MonitorController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorApplicationTests {

    private final String VEHICLE_ID = "VLUR4X20009048066";

	MockMvc mockMvc;

	@Autowired
	MonitorController monitorController;

	@Before
	public void setup() throws Exception {
		this.mockMvc = standaloneSetup(this.monitorController).build();
	}

	@Test
	public void testGetAllVehicles() throws Exception {
		//given
		//when
		updateVehicle();

		//then
		ResultActions resultActions = mockMvc.perform(get("/allvehicles").contentType(MediaType.APPLICATION_JSON));
		resultActions.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].vehicleId", notNullValue()))
        .andExpect(jsonPath("$[0].regNr", notNullValue()))
		.andExpect(jsonPath("$[0].companyName", notNullValue()))
		.andExpect(jsonPath("$[0].companyAddress", notNullValue()));
	}

	@Test
	public void testUpdateVehicle() throws Exception {
		updateVehicle().andExpect(status().isOk());
	}

	@Test
    public void getVehiclesByCompany() throws Exception {
        //given
        //when
        updateVehicle();

        getByStatus().andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleId", is(VEHICLE_ID)));
    }

    private ResultActions getByStatus() throws Exception {
	    return mockMvc.perform(post("/vehicles")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" +
                " \"field\":\"isConnected\",\n" +
                "\"value\": \"yes\"\n" +
                "}"));

    }

	private ResultActions updateVehicle() throws Exception {
		return mockMvc.perform(post("/vehicle")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"vehicleId\":\"" + VEHICLE_ID + "\"}"));
	}
}
