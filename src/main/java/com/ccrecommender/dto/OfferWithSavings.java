package com.ccrecommender.dto;

import com.ccrecommender.entity.CardOfferEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferWithSavings {
    private CardOfferEntity offer;
    private double estimatedSavings;
}
