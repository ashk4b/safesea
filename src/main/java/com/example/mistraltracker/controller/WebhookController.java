package com.example.mistraltracker.controller;

import com.example.mistraltracker.dto.LiveObjectsMessage;
import com.example.mistraltracker.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather") // L'URL sera http://localhost:8080/api/webhook
@Slf4j // Pour les logs
@RequiredArgsConstructor // Injection de dépendance automatique (Lombok)
public class WebhookController {

    private final DataProcessingService dataProcessingService;

    @PostMapping
    public ResponseEntity<String> receiveData(@RequestBody LiveObjectsMessage message) {

        // 1. Log pour voir si ça marche
        log.info("📡 Donnée reçue du capteur : {}", message.getMetadata().getNetwork().getLora().getDevEUI());
        log.info("📦 Payload Hex : {}", message.getValue().getPayload());

        // 2. Envoyer au service pour traitement (décodage + sauvegarde)
        dataProcessingService.processMessage(message);

        // 3. Répondre 200 OK à Orange (sinon ils vont renvoyer le message indéfiniment)
        return ResponseEntity.ok("Received");
    }
}