package com.tus.trafficsimulator.scheduler;

import com.tus.trafficsimulator.service.ScheduledLogicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrafficScheduler {

    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    private final ScheduledLogicService service;

    public TrafficScheduler(ScheduledLogicService service) {
        this.service = service;
    }

    @Scheduled(fixedRate = 300000) // every 5 min
    public void runScheduledTask() {
        if (schedulerEnabled) {
            service.execute();
        }
    }
}
