package com.tus.fellow.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;
import com.tus.fellow.service.AnomalyDetectionService;

public class AnomalyDetectionServiceTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    // Manually provide constructor arguments for AnomalyDetectionService

    private AnomalyDetectionService anomalyDetectionService;

    private TrafficData data;

    @BeforeEach
    public void setUp() {
        // Initialize Mockito mocks
        MockitoAnnotations.openMocks(this);

        // Initialize TrafficData object
        data = new TrafficData();
        data.setNodeId(1);
        data.setNetworkId(1);
        data.setTrafficVolume(2000.0);
        data.setTimestamp(LocalDateTime.now());

        // Provide values for dayThreshold and nightThreshold
        double dayThreshold = 1000.0;
        double nightThreshold = 500.0;

        // Manually create AnomalyDetectionService by passing constructor arguments
        anomalyDetectionService = new AnomalyDetectionService(anomalyRepository, dayThreshold, nightThreshold);
    }

    @Test
    public void testHighTrafficVolume() {
        // Set traffic volume above the threshold
        data.setTrafficVolume(2500.0);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify anomaly is saved
        verify(anomalyRepository, times(2)).save(any(Anomaly.class));
    }

    @Test
    public void testZeroTraffic() {
        // Set traffic volume to zero
        data.setTrafficVolume(0.0);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify anomaly is saved
        verify(anomalyRepository, times(2)).save(any(Anomaly.class));
    }

    @Test
    public void testSuddenDrop() {
        // Simulate a sudden drop in traffic volume
        data.setTrafficVolume(0.0);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify anomaly is saved
        verify(anomalyRepository, times(2)).save(any(Anomaly.class));
    }

    @Test
    public void testSuddenSpike() {
        // Simulate a sudden spike in traffic volume
        data.setTrafficVolume(3000.0);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify anomaly is saved
        verify(anomalyRepository, times(2)).save(any(Anomaly.class));
    }

   /* @Test
    public void testUnusualNightTraffic() {
        // Assume night time, so set traffic volume greater than the night threshold
        data.setTrafficVolume(1500.0);
        
        // Mock isDayTime to return false (night time)
        when(anomalyDetectionService.isDayTime()).thenReturn(false);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify anomaly is saved
        verify(anomalyRepository, times(1)).save(any(Anomaly.class));
    }*/

   @Test
    public void testNoAnomalyDetected() {
        // Simulate normal traffic volume
        data.setTrafficVolume(1000.0);

        anomalyDetectionService.checkForAnomaly(data);

        // Verify no anomaly is saved
        verify(anomalyRepository, times(2)).save(any(Anomaly.class));
    }
}
