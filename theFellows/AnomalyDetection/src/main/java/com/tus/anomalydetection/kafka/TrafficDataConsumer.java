package com.tus.anomalydetection.kafka;


import com.tus.anomalydetection.repository.TrafficDataRepository;
import com.tus.common.dto.TrafficDataDTO;
import com.tus.common.entity.TrafficData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//Kafka Consumer
@Service
public class TrafficDataConsumer {

 private TrafficDataRepository repository;



 TrafficDataConsumer(TrafficDataRepository repository){
     this.repository = repository;
 }

    @KafkaListener(
            topics = "${kafka.topic.traffic}",
            groupId = "traffic-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTrafficData(TrafficDataDTO data) {

		TrafficData entity = new TrafficData();
		entity.setNodeId(data.getNodeId());
		entity.setNetworkId(data.getNetworkId());
		entity.setTrafficVolume(data.getTrafficVolume());
		entity.setTimestamp(data.getTimestamp());
		repository.save(entity);
    }
}