package com.ccrecommender.mapper;

import com.ccrecommender.dto.CardOfferDTO;
import com.ccrecommender.entity.CardOfferEntity;

public class CardOfferMapper {

    public static CardOfferDTO toDTO(CardOfferEntity entity) {
        return new CardOfferDTO(
                entity.getId(),
                entity.getCardName(),
                entity.getMerchant(),
                entity.getMinAmount(),
                entity.getCashbackPct(),
                entity.getMaxSavings(),
                entity.getCategory(),
                entity.getPaymentType(),
                entity.getValidFrom(),
                entity.getValidTo(),
                entity.getOfferNote()
        );
    }

    public static CardOfferEntity toEntity(CardOfferDTO dto) {
        CardOfferEntity entity = new CardOfferEntity();
        entity.setId(dto.getId());
        entity.setCardName(dto.getCardName());
        entity.setMerchant(dto.getMerchant());
        entity.setMinAmount(dto.getMinAmount());
        entity.setCashbackPct(dto.getCashbackPct());
        entity.setMaxSavings(dto.getMaxSavings());
        entity.setCategory(dto.getCategory());
        entity.setPaymentType(dto.getPaymentType());
        entity.setValidFrom(dto.getValidFrom());
        entity.setValidTo(dto.getValidTo());
        entity.setOfferNote(dto.getOfferNote());
        return entity;
    }
}
