package com.tus.anomalydetection.kafka;


import com.tus.anomalydetection.repository.TrafficDataRepository;
import com.tus.anomalydetection.service.AnomalyDetectionService;
import com.tus.common.dto.TrafficDataDTO;
import com.tus.common.entity.TrafficData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//Kafka Consumer
@Service
public class TrafficDataConsumer {
 @Autowired
 private TrafficDataRepository repository;
 
 @Autowired
 private AnomalyDetectionService anomalyService;

    @KafkaListener(
            topics = "${kafka.topic.traffic}",
            groupId = "traffic-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTrafficData(TrafficDataDTO data) {
        System.out.println("Kafka message received: " + data);
     TrafficData entity = new TrafficData();
     entity.setNodeId(data.getNodeId());
     entity.setNetworkId(data.getNetworkId());
     entity.setTrafficVolume(data.getTrafficVolume());
     entity.setTimestamp(data.getTimestamp());
     repository.save(entity);
     anomalyService.checkForAnomaly(entity);
 }
}