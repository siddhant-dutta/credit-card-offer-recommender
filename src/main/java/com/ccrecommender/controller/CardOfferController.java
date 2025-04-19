package com.ccrecommender.controller;

import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.service.CardOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class CardOfferController {

    private final CardOfferService cardOfferService;

    // 🔹 Get all offers
    @GetMapping
    public ResponseEntity<List<CardOfferEntity>> getAllOffers() {
        return ResponseEntity.ok(cardOfferService.getAllCardOffers());
    }

    // 🔹 Create a new offer
    @PostMapping
    public ResponseEntity<CardOfferEntity> createOffer(@RequestBody CardOfferEntity offer) {
        CardOfferEntity created = cardOfferService.createCardOffer(offer);
        return ResponseEntity.ok(created);
    }

    // 🔹 Get offers by merchant
    @GetMapping("/merchant/{merchant}")
    public ResponseEntity<List<CardOfferEntity>> getOffersByMerchant(@PathVariable String merchant) {
        return ResponseEntity.ok(cardOfferService.getAllCardOffersByMerchant(merchant));
    }

    // 🔹 Get best offers for a user (based on input + optional user cards)
    @GetMapping("/{userId}/best-offers")
    public ResponseEntity<List<OfferWithSavings>> getBestOffersForUser(
            @PathVariable Long userId,
            @RequestParam String merchant,
            @RequestParam String category,
            @RequestParam String paymentType,
            @RequestParam double txnAmount
    ) {
        List<OfferWithSavings> offers = cardOfferService.getBestCardOffers(
                merchant, category, paymentType, txnAmount, userId
        );
        return ResponseEntity.ok(offers);
    }
}
