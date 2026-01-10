package com.example.mistraltracker.repository;

import com.example.mistraltracker.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    // C'est tout ! Spring Data génère tout le code SQL pour toi.
}