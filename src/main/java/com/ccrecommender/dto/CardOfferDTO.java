package com.ccrecommender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardOfferDTO {
    private Long id;
    private String cardName;
    private String merchant;
    private Double minAmount;
    private Double cashbackPct;
    private Double maxSavings;
    private String category;
    private String paymentType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String offerNote;
}
