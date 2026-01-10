package com.example.mistraltracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date de réception de la donnée
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Données brutes du capteur
    private Double temperature;      // En °C
    private Double humidity;         // En %
    private Double windSpeed;        // En km/h
    private Double windDirection;    // En degrés (0-360)
    private Double lightIntensity;   // En Lux
    private Double rainLevel;        // En mm

    // Données calculées (Notre valeur ajoutée)
    private Double mistralScore;     // Note sur 10 ou 100
    private Boolean isTerrasseOk;    // true = on peut manger dehors

    // Métadonnées techniques (Optionnel mais utile pour le debug)
    private String deviceEui;        // Identifiant unique du capteur
    private Integer batteryLevel;    // % de batterie du S2120
}