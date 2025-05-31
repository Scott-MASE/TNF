package com.tus.fellow.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;
import com.tus.fellow.service.AnomalyDetectionService;

public class AnomalyDetectionServiceTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    private AnomalyDetectionService anomalyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        anomalyService = new AnomalyDetectionService(anomalyRepository, 1000.0, 500.0); // Inject mock thresholds
    }

    @Test
    public void testCheckForAnomaly_HighTraffic() {
        TrafficData data = new TrafficData();
        data.setNodeId(1);
        data.setNetworkId(2);
        data.setTrafficVolume(1500.0);
        data.setTimestamp(java.time.LocalDateTime.now());

        anomalyService.checkForAnomaly(data);

        verify(anomalyRepository, times(1)).save(any(Anomaly.class));
    }

    @Test
    public void testCheckForAnomaly_NoAnomaly() {
        TrafficData data = new TrafficData();
        data.setNodeId(1);
        data.setNetworkId(2);
        data.setTrafficVolume(200.0);
        data.setTimestamp(java.time.LocalDateTime.now());

        anomalyService.checkForAnomaly(data);

        verify(anomalyRepository, never()).save(any(Anomaly.class));
    }
}
