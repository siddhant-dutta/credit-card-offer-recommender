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

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "merchant", nullable = false)
    private String merchant;

    @Column(name = "min_amount")
    private Double minAmount;

    @Column(name = "cashback_pct")
    private Double cashbackPct;

    @Column(name = "max_savings")
    private Double maxSavings;

    @Column(name = "category")
    private String category;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "offer_note")
    private String offerNote;
}
