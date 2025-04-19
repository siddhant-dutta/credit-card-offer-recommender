package com.ccrecommender.service;

import com.ccrecommender.dto.BestOffersRequestDTO;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public BestOffersRequestDTO extractFiltersFromText(String userInput) {
        // 🔹 1. Create structured message list
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You extract structured offer filters from user input. Return JSON with keys: merchant, category, paymentType, txnAmount."),
                Map.of("role", "user", "content", userInput)
        );

        // 🔹 2. Build the full request payload
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", messages,
                "temperature", 0.2
        );

        // 🔹 3. Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        // 🔹 4. Send the request
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 🔹 5. Parse the response using Jackson
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode contentNode = mapper.readTree(response.getBody())
                    .get("choices").get(0).get("message").get("content");

            return mapper.readValue(contentNode.asText(), BestOffersRequestDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
