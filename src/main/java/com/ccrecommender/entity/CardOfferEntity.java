package com.ccrecommender.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "card_offer")
public class CardOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
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
