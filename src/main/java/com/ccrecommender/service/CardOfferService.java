package com.ccrecommender.service;

import com.ccrecommender.dto.BestOffersRequestDTO;
import com.ccrecommender.dto.CardOfferDTO;
import com.ccrecommender.dto.OfferWithSavings;
import com.ccrecommender.entity.CardOfferEntity;
import com.ccrecommender.mapper.CardOfferMapper;
import com.ccrecommender.repository.CardOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardOfferService {

    private final CardOfferRepository cardOfferRepository;
    private final UserCardService userCardService;

    // ---------------------------------------------
    // 🔍 Public Methods - CRUD & Query Endpoints
    // ---------------------------------------------

    public List<CardOfferDTO> getAllCardOffers() {
        return cardOfferRepository.findAll().stream()
                .map(CardOfferMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CardOfferEntity> getAllCardOffersByMerchant(String merchant) {
        return cardOfferRepository.findByMerchantIgnoreCase(merchant);
    }

    public CardOfferEntity createCardOffer(CardOfferDTO cardOfferDTO) {

        return cardOfferRepository.save(CardOfferMapper.toEntity(cardOfferDTO));
    }

    /**
     * Suggests the best card offers based on user input filters.
     *
     * @param requestDTO contains filters like merchant, category, paymentType, txnAmount, userId
     * @return list of eligible offers with estimated savings, sorted by savings
     */
    public List<OfferWithSavings> getBestCardOffers(BestOffersRequestDTO requestDTO) {

        // Fetch the user cards based on the userId in the request DTO
        List<String> userCards = userCardService.getUserCardNames(requestDTO.getUserId());

        // Fetch eligible offers with card filter logic
        List<CardOfferEntity> eligibleOffers = cardOfferRepository.findEligibleOffersWithCardFilter(
                requestDTO.getMerchant(),
                requestDTO.getCategory(),
                requestDTO.getPaymentType(),
                requestDTO.getTxnAmount(),
                isNullOrEmpty(userCards) ? null : userCards
        );

        return calculateOfferSavings(eligibleOffers, requestDTO.getTxnAmount());
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
