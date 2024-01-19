package com.amdocs.poc.axon.saga.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        while (true) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                log.debug("User.Main got Exception: {}", ex.getMessage());
            }
        }
    }
}



