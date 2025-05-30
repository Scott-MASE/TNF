package com.tus.fellow.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.repository.AnomalyRepository;

@WebMvcTest(AnomalyController.class)
public class AnomalyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnomalyRepository anomalyRepo;

    @Test
    public void testGetAllAnomalies() throws Exception {
        Anomaly anomaly = new Anomaly();
        anomaly.setId(1L);
        anomaly.setNodeId(100);
        anomaly.setNetworkId(200);
        anomaly.setAnomalyType("High Traffic Volume");
        anomaly.setTrafficVolume(1500.0);
        anomaly.setTimestamp(LocalDateTime.now());

        Mockito.when(anomalyRepo.findAll()).thenReturn(List.of(anomaly));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/anomalies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nodeId", is(100)));
    }

    @Test
    public void testGetAnomalyById() throws Exception {
        Anomaly anomaly = new Anomaly();
        anomaly.setId(1L);
        anomaly.setNodeId(100);
        anomaly.setNetworkId(200);
        anomaly.setAnomalyType("High Traffic Volume");
        anomaly.setTrafficVolume(1500.0);
        anomaly.setTimestamp(LocalDateTime.now());

        Mockito.when(anomalyRepo.findById(1L)).thenReturn(Optional.of(anomaly));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/anomalies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nodeId", is(100)));
    }

    @Test
    public void testGetByNetworkId() throws Exception {
        Anomaly anomaly = new Anomaly();
        anomaly.setNetworkId(200);

        Mockito.when(anomalyRepo.findByNetworkId(200)).thenReturn(List.of(anomaly));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/anomalies/network/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].networkId", is(200)));
    }
}
