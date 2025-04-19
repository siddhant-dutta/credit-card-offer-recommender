package com.ccrecommender.controller;

import com.ccrecommender.dto.BestOffersRequestDTO;
import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.service.CardOfferService;
import com.ccrecommender.service.GroqService;
import com.ccrecommender.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/smart-offers")
@RequiredArgsConstructor
public class SmartOfferController {

    private final OpenAIService openAIService;
    private final CardOfferService cardOfferService;
    private final GroqService groqService;

    @PostMapping("/from-text")
    public ResponseEntity<?> getOffersFromNaturalText(@RequestBody String userInput) {
        try {
            BestOffersRequestDTO filters = groqService.extractFiltersFromText(userInput);
            List<OfferWithSavings> offers = cardOfferService.getBestCardOffers(filters);
            return ResponseEntity.ok(offers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not extract offer filters: " + e.getMessage());
        }
    }
}

