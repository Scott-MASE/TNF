package com.tus.trafficsimulator.scheduler;

import com.tus.common.dto.TrafficDataDTO;
import com.tus.trafficsimulator.kafka.TrafficDataProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

class ScheduledTrafficSimulatorTest {

    private TrafficDataProducer producer;
    private ScheduledTrafficSimulator simulator;

    @BeforeEach
    void setUp() {
        producer = mock(TrafficDataProducer.class);
        simulator = new ScheduledTrafficSimulator(producer);
        setField(simulator, "nightThreshold", 100.0);
        setField(simulator, "dayStartHour", 6);
        setField(simulator, "dayEndHour", 22);
    }

    @Test
    void testSimulateTrafficData_CallsProducer() {
        simulator.simulateTrafficData();
        verify(producer, times(1)).sendTrafficData(any(TrafficDataDTO.class));
    }

    // Utility method to set @Value fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
