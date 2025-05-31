package com.tus.fellow.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tus.fellow.dto.TrafficDataDTO;

@Service
public class TrafficDataProducer {

    private final KafkaTemplate<String, TrafficDataDTO> kafkaTemplate;
    private final String trafficTopic;

    public TrafficDataProducer(KafkaTemplate<String, TrafficDataDTO> kafkaTemplate,
                               @Value("${kafka.topic.traffic}") String trafficTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.trafficTopic = trafficTopic;
    }

    public void sendTrafficData(TrafficDataDTO data) {
        kafkaTemplate.send(trafficTopic, data);
    }
}
