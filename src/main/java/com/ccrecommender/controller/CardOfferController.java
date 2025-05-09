package com.ccrecommender.controller;

import com.ccrecommender.dto.BestOffersRequestDTO;
import com.ccrecommender.dto.CardOfferDTO;
import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.mapper.CardOfferMapper;
import com.ccrecommender.service.CardOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class CardOfferController {

    private final CardOfferService cardOfferService;

    // 🔹 Get all offers
    @GetMapping
    public ResponseEntity<List<CardOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(cardOfferService.getAllCardOffers());
    }

    // 🔹 Create a new offerDTO
    @PostMapping
    public ResponseEntity<CardOfferDTO> createOffer(@RequestBody CardOfferDTO offerDTO) {
        CardOfferEntity created = cardOfferService.createCardOffer(offerDTO);
        return ResponseEntity.ok(CardOfferMapper.toDTO(created));
    }

    // 🔹 Get offers by merchant
    @GetMapping("/merchant/{merchant}")
    public ResponseEntity<List<CardOfferDTO>> getOffersByMerchant(@PathVariable String merchant) {
        List<CardOfferDTO> offers = cardOfferService.getAllCardOffersByMerchant(merchant).stream()
                .map(CardOfferMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offers);
    }

    // 🔹 Get best offers for a user (based on input + optional user cards)
    @PostMapping("/best-offers")
    public ResponseEntity<List<OfferWithSavings>> getBestOffersForUser(@RequestBody BestOffersRequestDTO requestDTO) {
        List<OfferWithSavings> offers = cardOfferService.getBestCardOffers(requestDTO);
        return ResponseEntity.ok(offers);
    }
}
