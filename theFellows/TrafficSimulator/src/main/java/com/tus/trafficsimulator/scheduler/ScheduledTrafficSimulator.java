package com.tus.trafficsimulator.scheduler;

import com.tus.trafficsimulator.dto.TrafficDataDTO;
import com.tus.trafficsimulator.kafka.TrafficDataProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ScheduledTrafficSimulator {

    @Autowired
    private TrafficDataProducer producer;

    private final Random random = new Random();

    @Scheduled(fixedRate =30000) // Run every 30 seconds
    public void simulateTrafficData() {
        TrafficDataDTO data = new TrafficDataDTO();
        data.setNodeId(random.nextInt(5) + 1);            // Node IDs 1 to 5
        data.setNetworkId(random.nextInt(3) + 1);         // Network IDs 1 to 3
        data.setTrafficVolume(random.nextDouble() * 2000); // Random traffic volume
        data.setTimestamp(LocalDateTime.now());

        producer.sendTrafficData(data);
        System.out.println("Sent simulated traffic data: " + data);
    }
}
