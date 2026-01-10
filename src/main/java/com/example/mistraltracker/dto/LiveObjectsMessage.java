package com.example.mistraltracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Important : ignore les champs "extra", "tags" qu'on n'utilise pas pour éviter les crashs
public class LiveObjectsMessage {

    private String id;
    private String streamId;
    private String timestamp;
    private String model;

    // Le champ "value" contient un objet, pas directement le string
    private Value value;

    // Le champ "metadata" pour récupérer le DevEUI (l'identité du capteur)
    private Metadata metadata;

}