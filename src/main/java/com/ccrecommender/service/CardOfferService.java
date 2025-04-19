package com.ccrecommender.service;

import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.repository.CardOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardOfferService {

    private final CardOfferRepository cardOfferRepository;
    private final UserCardService userCardService;

    // ---------------------------------------------
    // 🔍 Public Methods - CRUD & Query Endpoints
    // ---------------------------------------------

    public List<CardOfferEntity> getAllCardOffers() {
        return cardOfferRepository.findAll();
    }

    public List<CardOfferEntity> getAllCardOffersByMerchant(String merchant) {
        return cardOfferRepository.findByMerchantIgnoreCase(merchant);
    }

    public CardOfferEntity createCardOffer(CardOfferEntity cardOfferEntity) {
        return cardOfferRepository.save(cardOfferEntity);
    }

    /**
     * Suggests the best card offers based on user input filters.
     *
     * @param merchant    e.g., "Amazon"
     * @param category    e.g., "Shopping"
     * @param paymentType e.g., "Online"
     * @param txnAmount   e.g., 5000.0
     * @param userId      e.g., 1234567890
     * @return list of eligible offers with estimated savings, sorted by savings
     */
    public List<OfferWithSavings> getBestCardOffers(
            String merchant,
            String category,
            String paymentType,
            double txnAmount,
            Long userId // TODO: In future, fetch from userId
    ) {

        List<String> userCards = userCardService.getUserCardNames(userId);
        List<CardOfferEntity> eligibleOffers = cardOfferRepository.findEligibleOffersWithCardFilter(
                merchant,
                category,
                paymentType,
                txnAmount,
                isNullOrEmpty(userCards) ? null : userCards
        );

        return calculateOfferSavings(eligibleOffers, txnAmount);
    }

    // ---------------------------------------------
    // 🔧 Private Helpers
    // ---------------------------------------------

    private List<OfferWithSavings> calculateOfferSavings(List<CardOfferEntity> offers, double transactionAmount) {
        List<OfferWithSavings> result = new ArrayList<>();

        for (CardOfferEntity offer : offers) {
            if (offer.getCashbackPct() == null || offer.getMaxSavings() == null) continue;

            double cashback = (transactionAmount * offer.getCashbackPct()) / 100.0;
            double savings = Math.min(cashback, offer.getMaxSavings());

            result.add(new OfferWithSavings(offer, savings));
        }

        result.sort(Comparator.comparingDouble(OfferWithSavings::getEstimatedSavings).reversed());
        return result;
    }

    private boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
