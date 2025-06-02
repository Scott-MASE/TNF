package com.tus.fellow.scheduler;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.kafka.TrafficDataProducer;

@Service
public class ScheduledTrafficSimulator {

    public ScheduledTrafficSimulator(TrafficDataProducer producer) {
		this.producer = producer;
	}

	private TrafficDataProducer producer;

	private final SecureRandom random = new SecureRandom();

    @Scheduled(fixedRate =10000) // Run every 10 seconds
    public void simulateTrafficData() {
        TrafficDataDTO data = new TrafficDataDTO();
        data.setNodeId(random.nextInt(5) + 1);            // Node IDs 1 to 5
        data.setNetworkId(random.nextInt(3) + 1);         // Network IDs 1 to 3
        data.setTrafficVolume(random.nextDouble() * 1000); // Random traffic volume
        data.setTimestamp(LocalDateTime.now());

        producer.sendTrafficData(data);
        System.out.println("Sent simulated traffic data: " + data);
    }
}
