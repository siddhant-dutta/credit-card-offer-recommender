package com.ccrecommender.service;

import com.ccrecommender.dto.BestOffersRequestDTO;
import com.ccrecommender.dto.CardOfferDTO;
import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.mapper.CardOfferMapper;
import com.ccrecommender.repository.CardOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardOfferService {

    private final CardOfferRepository cardOfferRepository;
    private final UserCardService userCardService;

    // ---------------------------
    // 🔍 Public Methods - CRUD & Query Endpoints
    // ---------------------------

    /**
     * Fetches all card offers.
     *
     * @return list of all card offers mapped to DTOs
     */
    public List<CardOfferDTO> getAllCardOffers() {
        return cardOfferRepository.findAll().stream()
                .map(CardOfferMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches all card offers for a given merchant.
     *
     * @param merchant the merchant name to filter offers by
     * @return list of card offers for the specified merchant
     */
    public List<CardOfferEntity> getAllCardOffersByMerchant(String merchant) {
        return cardOfferRepository.findByMerchantIgnoreCase(merchant);
    }

    /**
     * Creates and saves a new card offer.
     *
     * @param cardOfferDTO DTO containing card offer details
     * @return the created CardOfferEntity
     */
    public CardOfferEntity createCardOffer(CardOfferDTO cardOfferDTO) {
        return cardOfferRepository.save(CardOfferMapper.toEntity(cardOfferDTO));
    }

    /**
     * Suggests the best card offers based on user input filters and calculates the estimated savings.
     *
     * @param requestDTO contains filters like merchant, category, paymentType, txnAmount, and userId
     * @return list of eligible offers with estimated savings, sorted by savings
     */
    public List<OfferWithSavings> getBestCardOffers(BestOffersRequestDTO requestDTO) {
        List<String> userCards = userCardService.getUserCardNames(requestDTO.getUserId());

        // Fetch eligible offers based on user card availability (null or not)
        List<CardOfferEntity> eligibleOffers = fetchEligibleOffers(
                requestDTO.getMerchant(),
                requestDTO.getCategory(),
                requestDTO.getPaymentType(),
                requestDTO.getTxnAmount(),
                userCards
        );

        // Calculate savings and return sorted offers
        return calculateOfferSavings(eligibleOffers, requestDTO.getTxnAmount());
    }

    // ---------------------------
    // 🔧 Private Helper Methods
    // ---------------------------

    /**
     * Fetch eligible card offers based on filters.
     *
     * @param merchant    the merchant filter
     * @param category    the category filter
     * @param paymentType the payment type filter
     * @param txnAmount   the transaction amount
     * @param userCards   the list of user cards (null or empty to ignore the filter)
     * @return list of eligible card offers
     */
    private List<CardOfferEntity> fetchEligibleOffers(String merchant, String category,
                                                      String paymentType, Double txnAmount,
                                                      List<String> userCards) {
        if (isNullOrEmpty(userCards)) {
            return cardOfferRepository.findEligibleOffersWithCardFilterNullCards(
                    merchant, category, paymentType, txnAmount
            );
        } else {
            return cardOfferRepository.findEligibleOffersWithCardFilterNonNullCards(
                    merchant, category, paymentType, txnAmount, userCards
            );
        }
    }

    /**
     * Calculates the estimated savings for each offer based on the transaction amount.
     *
     * @param offers            the list of eligible card offers
     * @param transactionAmount the transaction amount to calculate savings
     * @return list of offers with estimated savings, sorted by savings in descending order
     */
    private List<OfferWithSavings> calculateOfferSavings(List<CardOfferEntity> offers, double transactionAmount) {
        return offers.stream()
                .filter(offer -> offer.getCashbackPct() != null && offer.getMaxSavings() != null)
                .map(offer -> {
                    double cashback = (transactionAmount * offer.getCashbackPct()) / 100.0;
                    double savings = Math.min(cashback, offer.getMaxSavings());
                    return new OfferWithSavings(offer, savings);
                })
                .sorted(Comparator.comparingDouble(OfferWithSavings::getEstimatedSavings).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Helper method to check if a list is null or empty.
     *
     * @param list the list to check
     * @return true if the list is null or empty, false otherwise
     */
    private boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
