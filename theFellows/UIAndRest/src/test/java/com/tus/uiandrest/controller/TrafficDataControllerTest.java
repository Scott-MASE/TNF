package com.tus.uiandrest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tus.common.entity.TrafficData;
import com.tus.uiandrest.controllers.TrafficDataController;
import com.tus.uiandrest.repositories.TrafficDataRepository;



//MockMVC Test for Controller
@WebMvcTest(TrafficDataController.class)
class TrafficDataControllerTest {

 @MockBean
 private TrafficDataRepository trafficRepo;

 

 @InjectMocks
 private TrafficDataController controller;

 private MockMvc mockMvc;

 @BeforeEach
 public void setup() {
     MockitoAnnotations.openMocks(this);
     mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
 }

 /*@Test
 public void testSendTraffic() throws Exception {
     TrafficDataDTO dto = new TrafficDataDTO();
     dto.setNodeId(1);
     dto.setNetworkId(2);
     dto.setTrafficVolume(500.0);
     dto.setTimestamp(LocalDateTime.now());

     ObjectMapper objectMapper = new ObjectMapper();
     objectMapper.registerModule(new JavaTimeModule());
     objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
     
     mockMvc.perform(post("/api/traffic")
             .contentType(MediaType.APPLICATION_JSON)
             .content(new ObjectMapper().writeValueAsString(dto)))
             .andExpect(status().isAccepted());
 }*/

 @Test
 void testGetAllTraffic() throws Exception {
     TrafficData data = new TrafficData();
     data.setNodeId(1);
     data.setNetworkId(2);
     data.setTrafficVolume(500.0);
     data.setTimestamp(LocalDateTime.now());

     when(trafficRepo.findAll()).thenReturn(Arrays.asList(data));

     mockMvc.perform(get("/api/traffic"))
             .andExpect(status().isOk());
 }

 @Test
 void testGetTrafficById() throws Exception {
     TrafficData data = new TrafficData();
     data.setId(1L);
     data.setNodeId(1);
     data.setNetworkId(2);
     data.setTrafficVolume(500.0);
     data.setTimestamp(LocalDateTime.now());

     when(trafficRepo.findById(1L)).thenReturn(Optional.of(data));

     mockMvc.perform(get("/api/traffic/1"))
             .andExpect(status().isOk());
 }
}
