package com.tus.fellow.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.TrafficDataRepository;

//Kafka Consumer
@Service
public class TrafficDataConsumer {
	private TrafficDataRepository repository;

	public TrafficDataConsumer(TrafficDataRepository repository) {
		this.repository = repository;
	}

	@KafkaListener(topics = "${kafka.topic.traffic}", groupId = "traffic-group")
	public void consumeTrafficData(TrafficDataDTO data) {
		System.out.println("Kafka message received: " + data);
		TrafficData entity = new TrafficData();
		entity.setNodeId(data.getNodeId());
		entity.setNetworkId(data.getNetworkId());
		entity.setTrafficVolume(data.getTrafficVolume());
		entity.setTimestamp(data.getTimestamp());
		repository.save(entity);
	}

}