package com.tus.trafficsimulator.scheduler;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tus.common.dto.TrafficDataDTO;
import com.tus.trafficsimulator.kafka.TrafficDataProducer;

@Service
public class ScheduledTrafficSimulator {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTrafficSimulator.class);

	public ScheduledTrafficSimulator(TrafficDataProducer producer) {
		this.producer = producer;
	}

	private TrafficDataProducer producer;

	private final SecureRandom random = new SecureRandom();
	
	@Value("${traffic.threshold.night}")
    private double nightThreshold;

    @Value("${traffic.day.start}")
    private int dayStartHour;

    @Value("${traffic.day.end}")
    private int dayEndHour;
    
    private enum TrafficPattern {
        NORMAL, ZERO, HIGH, SPIKE, DROP, UNUSUAL_NIGHT
    }

    @Scheduled(fixedRate =10000) // Run every 10 seconds
    public void simulateTrafficData() {
        TrafficDataDTO data = new TrafficDataDTO();
        data.setNodeId(random.nextInt(5) + 1);            // Node IDs 1 to 5
        data.setNetworkId(random.nextInt(3) + 1);         // Network IDs 1 to 3
        data.setTimestamp(LocalDateTime.now());

        TrafficPattern pattern = choosePattern(data.getTimestamp());

        switch (pattern) {
            case ZERO -> data.setTrafficVolume(0.0);
            case HIGH -> data.setTrafficVolume(2500 + random.nextDouble() * 1000);
            case SPIKE -> data.setTrafficVolume(3500 + random.nextDouble() * 500);
            case DROP -> data.setTrafficVolume(50 + random.nextDouble() * 40);
            case UNUSUAL_NIGHT -> data.setTrafficVolume(nightThreshold + 500 + random.nextDouble() * 300);
            case NORMAL -> data.setTrafficVolume(500 + random.nextDouble() * 1000);
        }
        
        producer.sendTrafficData(data);
        logger.info("Sent simulated traffic data (" + pattern + "): " + data);
    }
    
    private TrafficPattern choosePattern(LocalDateTime now) {
        int chance = random.nextInt(100);

        boolean isNight = now.getHour() < dayStartHour || now.getHour() >= dayEndHour;
        if (isNight && chance < 5) return TrafficPattern.UNUSUAL_NIGHT;
        else if (chance < 10) return TrafficPattern.ZERO;
        else if (chance < 25) return TrafficPattern.HIGH;
        else if (chance < 40) return TrafficPattern.SPIKE;
        else if (chance < 55) return TrafficPattern.DROP;
        else return TrafficPattern.NORMAL;
    }
}
