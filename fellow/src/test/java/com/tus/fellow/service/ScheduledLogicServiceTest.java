package com.tus.fellow.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.TrafficDataRepository;

public class ScheduledLogicServiceTest {

    @Mock
    private TrafficDataRepository trafficDataRepository;

    @Mock
    private AnomalyDetectionService anomalyDetectionService;

    @InjectMocks
    private ScheduledLogicService scheduledLogicService;

    private TrafficData data1;
    private TrafficData data2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Example TrafficData for testing
        data1 = new TrafficData();
        data1.setNodeId(1);
        data1.setNetworkId(1);
        data1.setTrafficVolume(3000.0);
        data1.setTimestamp(LocalDateTime.now());

        data2 = new TrafficData();
        data2.setNodeId(2);
        data2.setNetworkId(1);
        data2.setTrafficVolume(0.0);
        data2.setTimestamp(LocalDateTime.now().minusHours(1));
    }

    @Test
    public void testExecuteWithHighTraffic() {
        // Mock repository to return the traffic data
        when(trafficDataRepository.findRecentTrafficData(any())).thenReturn(Arrays.asList(data1, data2));

        scheduledLogicService.execute();

        verify(anomalyDetectionService, times(0)).checkForAnomaly(data1);
        verify(anomalyDetectionService, times(0)).checkForAnomaly(data2);
    }

    @Test
    public void testExecuteWithNormalTraffic() {
        // Modify traffic data with normal volume
        data1.setTrafficVolume(500.0);

        // Mock repository to return normal traffic data
        when(trafficDataRepository.findRecentTrafficData(any())).thenReturn(Arrays.asList(data1));

        scheduledLogicService.execute();

        verify(anomalyDetectionService, times(0)).checkForAnomaly(data1);
    }

    @Test
    public void testExecuteWhenNoTraffic() {
        // Mock empty traffic data
        when(trafficDataRepository.findRecentTrafficData(any())).thenReturn(Arrays.asList());

        scheduledLogicService.execute();

        verify(anomalyDetectionService, times(0)).checkForAnomaly(any());
    }
}
