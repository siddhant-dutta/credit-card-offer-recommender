package com.ccrecommender.controller;

import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.service.CardOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class CardOfferController {

    private final CardOfferService cardOfferService;

    public CardOfferController(CardOfferService cardOfferService) {
        this.cardOfferService = cardOfferService;
    }

    @GetMapping
    public List<CardOfferEntity> getAllOffers() {
        return cardOfferService.getAllCardOffers();
    }

    @PostMapping
    public CardOfferEntity createOffer(@RequestBody CardOfferEntity offer) {

        return cardOfferService.createCardOffer(offer);
    }


    @GetMapping("/merchant/{merchant}")
    public List<CardOfferEntity> getOffersByMerchant(@PathVariable String merchant) {
        return cardOfferService.getAllCardOffersByMerchant(merchant);
    }

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
