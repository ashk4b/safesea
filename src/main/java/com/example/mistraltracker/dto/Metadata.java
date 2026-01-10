package com.example.mistraltracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    // Le JSON d'Orange a une clé "network" ici, pas directement "devEUI"
    private Network network;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        private Lora lora;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lora {
        // C'est ICI que se trouve enfin l'ID du capteur
        private String devEUI;
        private Integer rssi;
        private Integer snr;
    }
}