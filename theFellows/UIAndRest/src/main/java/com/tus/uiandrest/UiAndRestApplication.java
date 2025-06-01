package com.tus.uiandrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.tus.common.entity")
public class UiAndRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiAndRestApplication.class, args);
    }

}
