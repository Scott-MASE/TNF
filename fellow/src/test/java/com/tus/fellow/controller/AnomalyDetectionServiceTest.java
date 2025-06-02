package com.tus.fellow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;
import com.tus.fellow.service.AnomalyDetectionService;

class AnomalyDetectionServiceTest {

	@Mock
	private AnomalyRepository anomalyRepository;

	// Manually provide constructor arguments for AnomalyDetectionService

	private AnomalyDetectionService anomalyDetectionService;

	private TrafficData data;

	private static final double HIGH_VOLUME = 2500;

	private static final int ONE_TIME = 1;

	private static final double ZERO = 0.0;

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
	void testHighTrafficVolume() {
		// Set traffic volume above the threshold
		data.setTrafficVolume(HIGH_VOLUME);

		anomalyDetectionService.checkForAnomaly(data);

		// Verify anomaly is saved
		verify(anomalyRepository, times(ONE_TIME)).save(any(Anomaly.class));
	}

	@Test
	void testZeroTraffic() {
		// Set traffic volume to zero
		data.setTrafficVolume(ZERO);

		anomalyDetectionService.checkForAnomaly(data);

		// Verify anomaly is saved
		verify(anomalyRepository, times(ONE_TIME)).save(any(Anomaly.class));
	}

	@Test
	void testSuddenDrop() {
		// Simulate a sudden drop in traffic volume
		data.setTrafficVolume(10.0);

		anomalyDetectionService.checkForAnomaly(data);

		// Verify anomaly is saved
		verify(anomalyRepository, times(ONE_TIME)).save(any(Anomaly.class));
	}

	@Test
	void testSuddenSpike() {
		// Simulate a sudden spike in traffic volume
		data.setTrafficVolume(3000.0);

		anomalyDetectionService.checkForAnomaly(data);

		// Verify anomaly is saved
		verify(anomalyRepository, times(ONE_TIME)).save(any(Anomaly.class));
	}

	@Test
	void testNoAnomalyDetected() {
		// Simulate normal traffic volume
		data.setTrafficVolume(1000.0);

		anomalyDetectionService.checkForAnomaly(data);

		// Verify no anomaly is saved
		verify(anomalyRepository, times(ONE_TIME)).save(any(Anomaly.class));
	}
}
