package com.tus.anomalydetection.kafka;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.TrafficDataRepository;
import com.tus.fellow.service.AnomalyDetectionService;
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

 @KafkaListener(topics = "${kafka.topic.traffic}", groupId = "traffic-group")
 public void consumeTrafficData(TrafficDataDTO data) {
	 System.out.println("Kafka message received: " + data);
     TrafficData entity = new TrafficData();
     entity.setNodeId(data.getNodeId());
     entity.setNetworkId(data.getNetworkId());
     entity.setTrafficVolume(data.getTrafficVolume());
     entity.setTimestamp(data.getTimestamp());
     repository.save(entity);
     //anomalyService.checkForAnomaly(entity);
 }
}