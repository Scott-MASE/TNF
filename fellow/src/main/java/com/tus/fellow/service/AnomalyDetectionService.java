package com.tus.fellow.service;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.AnomalyType;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;

@Service
public class AnomalyDetectionService {

	private static final Logger logger = LoggerFactory.getLogger(AnomalyDetectionService.class);

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
        
     // Detecting various anomalies
        if (data.getTrafficVolume() > currentThreshold) {
            createAnomaly(data, AnomalyType.HIGH_TRAFFIC_VOLUME);
        }
        
        if (Double.compare(data.getTrafficVolume(), 0.0) == 0) {
        	logger.info("AnomalyDetectionService traffic: nodeId={}, networkId={}, volume={}, timestamp={}", 
                    data.getNodeId(), data.getNetworkId(), data.getTrafficVolume(), data.getTimestamp());
            createAnomaly(data, AnomalyType.ZERO_TRAFFIC);
        }

        if (isSuddenDrop(data)) {
            createAnomaly(data, AnomalyType.SUDDEN_DROP);
        }

        if (isSuddenSpike(data)) {
            createAnomaly(data, AnomalyType.SUDDEN_SPIKE);
        }

        if (isUnusualNightTraffic(data)) {
            createAnomaly(data, AnomalyType.UNUSUAL_NIGHT_TRAFFIC);
        }

    }
    private void createAnomaly(TrafficData data, AnomalyType anomalyType) {
        Anomaly anomaly = new Anomaly();
        anomaly.setNodeId(data.getNodeId());
        anomaly.setNetworkId(data.getNetworkId());
        anomaly.setTrafficVolume(data.getTrafficVolume());
        anomaly.setAnomalyType(anomalyType);
        anomaly.setTimestamp(data.getTimestamp());

        boolean exists = anomalyRepository.existsByNodeIdAndNetworkIdAndTimestamp(data.getNodeId(), data.getNetworkId(), data.getTimestamp());
        if (!exists) {
            anomalyRepository.save(anomaly);
        }
    }

    public boolean isDayTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(18, 0));
    }
    
    private boolean isSuddenDrop(TrafficData data) {
        // Compare with the previous traffic data to detect sudden drops
    	//logger.info("************** SuddenDrop:"+data.getTrafficVolume()+(data.getTrafficVolume()<100));
        return data.getTrafficVolume() < 100; // Just an example, adjust the logic
    }

    private boolean isSuddenSpike(TrafficData data) {
        // Compare with the previous traffic data to detect sudden spikes
    	//logger.info("************* SuddenSPIKE:"+data.getTrafficVolume()+(data.getTrafficVolume()>3000));
        return data.getTrafficVolume() > 3000; // Just an example, adjust the logic
    }

    private boolean isUnusualNightTraffic(TrafficData data) {
        return !isDayTime() && data.getTrafficVolume() > nightThreshold;
    }
}
