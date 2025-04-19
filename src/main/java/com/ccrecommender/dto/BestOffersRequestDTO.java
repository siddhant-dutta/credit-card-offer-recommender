package com.ccrecommender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestOffersRequestDTO {

    private String merchant;
    private String category;
    private String paymentType;
    private double txnAmount;
    private Long userId;
}