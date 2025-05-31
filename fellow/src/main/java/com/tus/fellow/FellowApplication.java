package com.tus.fellow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FellowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FellowApplication.class, args);
    }

}
