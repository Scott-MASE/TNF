package com.tus.anomalydetection.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.anomalydetection.kafka.TrafficDataConsumer;
import com.tus.anomalydetection.repository.TrafficDataRepository;
import com.tus.common.dto.TrafficDataDTO;
import com.tus.common.entity.TrafficData;

class TrafficDataConsumerTest {

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
    void testConsumeTrafficData() {
        TrafficDataDTO dto = new TrafficDataDTO();
        dto.setNodeId(1);
        dto.setNetworkId(2);
        dto.setTrafficVolume(500.0);

        consumer.consumeTrafficData(dto);

        verify(repository, times(1)).save(any(TrafficData.class));
        //verify(anomalyService, times(1)).checkForAnomaly(any(TrafficData.class));
    }
}
