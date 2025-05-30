package com.tus.trafficsimulator.generator;

import com.tus.trafficsimulator.entities.Traffic;
import com.tus.trafficsimulator.repositories.TrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TrafficGenerator {

    private final TrafficRepository repository;

    @Autowired
    public TrafficGenerator(TrafficRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRateString = "3000")
    public void generateTraffic() {
        Traffic traffic = new Traffic();
        // Random network ID between 1 and 10
        int networkId = ThreadLocalRandom.current().nextInt(1, 11);
        // Random volume between 0.0 and 1000.0
        double volume = ThreadLocalRandom.current().nextDouble(0.0, 1000.0);
        LocalDateTime timestamp = LocalDateTime.now();

        traffic.setNetworkId(networkId);
        traffic.setVolume(volume);
        traffic.setTimestamp(timestamp);

        repository.save(traffic);
        System.out.println("Generated traffic: " + traffic);
    }
}
