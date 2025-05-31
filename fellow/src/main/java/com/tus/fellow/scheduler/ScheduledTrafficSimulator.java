package com.tus.fellow.scheduler;

import java.time.LocalDateTime;
import java.util.Random;

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

    private final Random random = new Random();

    @Scheduled(fixedRate =30000) // Run every 30 seconds
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
