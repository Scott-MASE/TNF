package com.tus.uiandrest.controller;

import com.tus.common.entity.Anomaly;
import com.tus.common.entity.AnomalyType;
import com.tus.uiandrest.controllers.AIController;
import com.tus.uiandrest.repositories.AnomalyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AIController.class)
@Import(AIControllerTestConfig.class)
class AIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnomalyRepository anomalyRepo;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetAIAnalysis_NoAnomalies() throws Exception {
        Mockito.when(anomalyRepo.findTop20ByOrderByTimestampDesc()).thenReturn(List.of());

        mockMvc.perform(get("/api/ai/analysis"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No anomaly data available")));
    }


    @Test
    void testGetAIAnalysis_ApiFailure() throws Exception {
        Anomaly a = new Anomaly();
        a.setNodeId(1);
        a.setNetworkId(1);
        a.setTrafficVolume(123.45);
        a.setAnomalyType(AnomalyType.SUDDEN_SPIKE);
        a.setTimestamp(LocalDateTime.now());

        Mockito.when(anomalyRepo.findTop20ByOrderByTimestampDesc()).thenReturn(List.of(a));
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("API is down"));

        mockMvc.perform(get("/api/ai/analysis"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("AI analysis failed")));
    }
}
