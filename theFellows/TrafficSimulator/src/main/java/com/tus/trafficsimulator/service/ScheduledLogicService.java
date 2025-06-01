package com.tus.trafficsimulator.service;

import com.tus.trafficsimulator.entity.TrafficData;
import com.tus.trafficsimulator.repository.TrafficDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//Scheduled Logic Service
@Service
public class ScheduledLogicService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledLogicService.class);

    private final TrafficDataRepository trafficDataRepository;


    @Value("${traffic.threshold.day}")
    private double dayThreshold;

    @Value("${traffic.threshold.night}")
    private double nightThreshold;

    @Value("${traffic.day.start}")
    private int dayStartHour;

    @Value("${traffic.day.end}")
    private int dayEndHour;

    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    public ScheduledLogicService(TrafficDataRepository trafficDataRepository) {
        this.trafficDataRepository = trafficDataRepository;
    }

    public void execute() {
        if (!schedulerEnabled) {
            logger.info("Scheduler is disabled via configuration.");
            return;
        }

        logger.info("Scheduled task started at {}", LocalDateTime.now());

        // Cleanup stale records
        List<TrafficData> staleRecords = trafficDataRepository.findAll().stream()
                .filter(data -> data.getTrafficVolume() == 0 || data.getTimestamp().isBefore(LocalDateTime.now().minusDays(30)))
                .toList();
        if (!staleRecords.isEmpty()) {
            trafficDataRepository.deleteAll(staleRecords);
            logger.info("Deleted {} stale records.", staleRecords.size());
        }

        // Fetch recent traffic data
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<TrafficData> recentTraffic = trafficDataRepository.findRecentTrafficData(since);

        int currentHour = LocalDateTime.now().getHour();
        double currentThreshold = (currentHour >= dayStartHour && currentHour < dayEndHour) ? dayThreshold : nightThreshold;

        logger.info("Using threshold {} for current hour {}", currentThreshold, currentHour);


        logger.info("Checked {} records for anomalies.", recentTraffic.size());
    }
}
