package com.example.mistraltracker.service;

import com.example.mistraltracker.dto.LiveObjectsMessage;
import com.example.mistraltracker.model.WeatherData;
import com.example.mistraltracker.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProcessingService {

    private final WeatherDataRepository repository;
    // private final DecoderService decoderService; // On le fera juste après

    public void processMessage(LiveObjectsMessage message) {
        String hexPayload = message.getValue().getPayload();

        // TODO: Appeler le vrai décodeur ici.
        // Pour l'instant, on va simuler ou mettre des valeurs nulles pour tester la chaine

        log.info("🛠️ Traitement du payload: {}", hexPayload);

        // Exemple de création de l'objet à sauvegarder
        WeatherData data = new WeatherData();
        data.setTimestamp(LocalDateTime.now()); // On prend l'heure de réception
        if (message.getMetadata() != null
                && message.getMetadata().getNetwork() != null
                && message.getMetadata().getNetwork().getLora() != null) {

            data.setDeviceEui(message.getMetadata().getNetwork().getLora().getDevEUI());
        }

        // ICI : Il faudra extraire les vraies valeurs du Hex
        // data.setTemperature(...);
        // data.setWindSpeed(...);

        // Sauvegarde en base
        repository.save(data);
        log.info("✅ Donnée sauvegardée en base avec ID: {}", data.getId());
    }
}