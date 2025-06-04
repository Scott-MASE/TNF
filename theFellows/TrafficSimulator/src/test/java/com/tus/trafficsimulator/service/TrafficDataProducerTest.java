package com.tus.trafficsimulator.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import com.tus.common.dto.TrafficDataDTO;
import com.tus.trafficsimulator.kafka.TrafficDataProducer;



class TrafficDataProducerTest {

    @Mock
    private KafkaTemplate<String, TrafficDataDTO> kafkaTemplate;

    @InjectMocks
    private TrafficDataProducer producer;

    private static final int ONE_TIME = 1;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producer = new TrafficDataProducer(kafkaTemplate, "test-topic"); 
    }

    @Test
    void testSendZeroTraffic() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(0.0);

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    @Test
    void testSendHighTraffic() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(3000.0); // Above high threshold

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    @Test
    void testSendSuddenSpike() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(4000.0); // Simulate a spike

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    @Test
    void testSendSuddenDrop() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(40.0); // Simulate a drop

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    @Test
    void testSendUnusualNightTraffic() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(1200.0); // Above night threshold
        dto.setTimestamp(LocalDateTime.of(2025, 6, 4, 1, 30)); // 1:30 AM = night

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    @Test
    void testSendNormalTraffic() {
        TrafficDataDTO dto = baseDTO();
        dto.setTrafficVolume(600.0); // Normal range

        producer.sendTrafficData(dto);
        verify(kafkaTemplate, times(ONE_TIME)).send(anyString(), eq(dto));
    }

    // Helper method to avoid duplication
    private TrafficDataDTO baseDTO() {
        TrafficDataDTO dto = new TrafficDataDTO();
        dto.setNodeId(1);
        dto.setNetworkId(2);
        dto.setTimestamp(LocalDateTime.now());
        return dto;
    }
}
