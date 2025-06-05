package com.tus.anomalydetection.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tus.anomalydetection.repository.AnomalyRepository;
import com.tus.common.entity.Anomaly;
import com.tus.common.entity.TrafficData;

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


	@ParameterizedTest
	@CsvSource({
			"10.0, 1",      // Sudden drop
			"3000.0, 1",    // Sudden spike
			"1100.0, 1"
	})
	void testTrafficVolumeAnomalyDetection(double trafficVolume, int expectedSaves) {
		data.setTrafficVolume(trafficVolume);

		anomalyDetectionService.checkForAnomaly(data);

		verify(anomalyRepository, times(expectedSaves)).save(any(Anomaly.class));
	}
}
