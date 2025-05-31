package com.tus.fellow.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tus.fellow.dto.TrafficDataDTO;

@Service
public class TrafficDataProducer {
    @Autowired
    private KafkaTemplate<String, TrafficDataDTO> kafkaTemplate;
    
   

    @Value("${kafka.topic.traffic}")
    private String trafficTopic;
    
   
    public void sendTrafficData(TrafficDataDTO data) {
        kafkaTemplate.send(trafficTopic, data);
    }
    
   
}
