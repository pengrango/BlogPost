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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorApplicationTests {

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
		mockMvc.perform(get("/vehiclelist").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleId", is("VLUR4X20009048066")));
	}

	@Test
	public void testUpdateVehicle() throws Exception {
		updateVehicle().andExpect(status().isOk());
	}

	private ResultActions updateVehicle() throws Exception {
		return mockMvc.perform(post("/vehicle")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"vehicleId\":\"VLUR4X20009048066\"}"));
	}
}
