package com.ccrecommender.service;

import com.ccrecommender.dto.BestOffersRequestDTO;
import com.ccrecommender.dto.GroqRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroqService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    public BestOffersRequestDTO extractFiltersFromText(String userInput) {
        GroqRequestDTO requestDTO = new GroqRequestDTO();
        requestDTO.setModel("meta-llama/llama-4-scout-17b-16e-instruct");
        requestDTO.setTemperature(0.2);

        GroqRequestDTO.Message systemMessage = new GroqRequestDTO.Message();
        systemMessage.setRole("system");
        systemMessage.setContent("You extract structured offer filters from user input. Return JSON with keys: merchant, category, paymentType, txnAmount. if you find any of the values to be null populate them as null");

        GroqRequestDTO.Message userMessage = new GroqRequestDTO.Message();
        userMessage.setRole("user");
        userMessage.setContent(userInput);

        requestDTO.setMessages(List.of(systemMessage, userMessage));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<GroqRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(groqApiUrl, request, String.class);
        System.out.println("Groq API response: " + response.getBody());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode content = mapper.readTree(response.getBody())
                    .get("choices").get(0).get("message").get("content");
            // Clean the content string (remove extra text, leaving just the JSON structure)
            String jsonResponse = content.asText().split("```")[1].trim(); // Extract the JSON part
            return mapper.readValue(jsonResponse, BestOffersRequestDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response", e);
        }
    }
}
