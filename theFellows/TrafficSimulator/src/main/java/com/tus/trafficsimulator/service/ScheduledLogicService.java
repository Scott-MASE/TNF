package com.tus.trafficsimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//Scheduled Logic Service
@Service
public class ScheduledLogicService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledLogicService.class);




    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    public ScheduledLogicService() {
    }

    public void execute() {
        if (!schedulerEnabled) {
            logger.info("Scheduler is disabled via configuration.");
            return;
        }

        logger.info("Scheduled task started at {}", LocalDateTime.now());


    }
}
