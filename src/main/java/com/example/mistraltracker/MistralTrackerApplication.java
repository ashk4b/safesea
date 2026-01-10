package com.example.mistraltracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MistralTrackerApplication {

    public static void main(String[] args) {
        // C'est cette ligne qui démarre tout le serveur (Tomcat)
        SpringApplication.run(MistralTrackerApplication.class, args);
    }
}