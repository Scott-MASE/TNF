package com.tus.trafficsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.tus.common.entity")
public class TrafficSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficSimulatorApplication.class, args);
    }

}
