package com.tus.fellow.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.kafka.TrafficDataConsumer;
import com.tus.fellow.repository.TrafficDataRepository;

public class TrafficDataConsumerTest {

    @Mock
    private TrafficDataRepository repository;

    @Mock
    private AnomalyDetectionService anomalyService;

    @InjectMocks
    private TrafficDataConsumer consumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeTrafficData() {
        TrafficDataDTO dto = new TrafficDataDTO();
        dto.setNodeId(1);
        dto.setNetworkId(2);
        dto.setTrafficVolume(500.0);

        consumer.consumeTrafficData(dto);

        verify(repository, times(1)).save(any(TrafficData.class));
        //verify(anomalyService, times(1)).checkForAnomaly(any(TrafficData.class));
    }
}
