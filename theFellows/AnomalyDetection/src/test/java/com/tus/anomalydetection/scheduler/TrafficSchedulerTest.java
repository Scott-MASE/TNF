package com.tus.anomalydetection.scheduler;

import com.tus.anomalydetection.service.ScheduledLogicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

class TrafficSchedulerTest {

    private ScheduledLogicService service;
    private TrafficScheduler scheduler;

    @BeforeEach
    void setUp() {
        service = mock(ScheduledLogicService.class);
        scheduler = new TrafficScheduler(service);
    }

    private void setSchedulerEnabled(boolean value) {
        try {
            Field field = TrafficScheduler.class.getDeclaredField("schedulerEnabled");
            field.setAccessible(true);
            field.set(scheduler, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRunScheduledTask_WhenEnabled_ShouldExecute() {
        setSchedulerEnabled(true);
        scheduler.runScheduledTask();
        verify(service, times(1)).execute();
    }

    @Test
    void testRunScheduledTask_WhenDisabled_ShouldNotExecute() {
        setSchedulerEnabled(false);
        scheduler.runScheduledTask();
        verify(service, never()).execute();
    }
}
