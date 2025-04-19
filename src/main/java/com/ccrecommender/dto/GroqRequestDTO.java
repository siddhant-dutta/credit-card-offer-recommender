package com.ccrecommender.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroqRequestDTO {
    private String model;
    private List<Message> messages;
    private double temperature;

    // Getters and Setters
    @Data
    public static class Message {
        private String role;
        private String content;

        // Getters and Setters
    }
}
