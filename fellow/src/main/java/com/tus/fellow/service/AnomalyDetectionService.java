package com.tus.fellow.service;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;

    //@Value("${traffic.threshold.day}")
    private double dayThreshold;

    //@Value("${traffic.threshold.night}")
    private double nightThreshold;

    public AnomalyDetectionService(AnomalyRepository anomalyRepository,
            @Value("${traffic.threshold.day}") double dayThreshold,
            @Value("${traffic.threshold.night}") double nightThreshold) {
		this.anomalyRepository = anomalyRepository;
		this.dayThreshold = dayThreshold;
		this.nightThreshold = nightThreshold;
	}

    public void checkForAnomaly(TrafficData data) {
        double currentThreshold = isDayTime() ? dayThreshold : nightThreshold;

        if (data.getTrafficVolume() > currentThreshold) {
            Anomaly anomaly = new Anomaly();
            anomaly.setNodeId(data.getNodeId());
            anomaly.setNetworkId(data.getNetworkId());
            anomaly.setTrafficVolume(data.getTrafficVolume());
            anomaly.setAnomalyType("High Traffic Volume");
            anomaly.setTimestamp(data.getTimestamp());
            anomalyRepository.save(anomaly);
        }
    }

    private boolean isDayTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(18, 0));
    }
}
