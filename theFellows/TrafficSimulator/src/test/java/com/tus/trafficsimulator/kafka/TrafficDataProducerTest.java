package com.tus.trafficsimulator.kafka;

import com.tus.common.dto.TrafficDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class TrafficDataProducerTest {

    private KafkaTemplate<String, TrafficDataDTO> kafkaTemplate;
    private TrafficDataProducer producer;

    private final String testTopic = "test-traffic-topic";

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new TrafficDataProducer(kafkaTemplate, testTopic);
    }

    @Test
    void testSendTrafficData() {
        TrafficDataDTO mockData = new TrafficDataDTO();
        mockData.setNodeId(1);
        mockData.setNetworkId(2);
        mockData.setTrafficVolume(100.5);
        mockData.setTimestamp(java.time.LocalDateTime.now());

        producer.sendTrafficData(mockData);

        verify(kafkaTemplate, times(1)).send(testTopic, mockData);
    }
}
