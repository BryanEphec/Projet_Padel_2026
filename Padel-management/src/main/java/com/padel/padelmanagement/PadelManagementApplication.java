package com.padel.padelmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PadelManagementApplication {

    public static void main(String[] args) {

        SpringApplication.run(PadelManagementApplication.class, args);
    }

}
