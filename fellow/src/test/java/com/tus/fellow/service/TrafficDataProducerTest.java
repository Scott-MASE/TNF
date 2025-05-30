package com.tus.fellow.service;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.kafka.TrafficDataProducer;

public class TrafficDataProducerTest {

    @Mock
    private KafkaTemplate<String, TrafficDataDTO> kafkaTemplate;

    @InjectMocks
    private TrafficDataProducer producer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendTrafficData() {
        TrafficDataDTO dto = new TrafficDataDTO();
        dto.setNodeId(1);
        dto.setNetworkId(2);
        dto.setTrafficVolume(300.0);

        producer.sendTrafficData(dto);

        verify(kafkaTemplate, times(1)).send(anyString(), eq(dto));
    }
}
