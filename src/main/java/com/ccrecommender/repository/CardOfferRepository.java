package com.ccrecommender.repository;

import com.ccrecommender.entity.CardOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardOfferRepository extends JpaRepository<CardOfferEntity, Long> {
    List<CardOfferEntity> findByMerchantIgnoreCase(String merchant);

    @Query("""
                SELECT o FROM CardOfferEntity o
                WHERE (:merchant IS NULL OR o.merchant = :merchant)
                  AND (:category IS NULL OR o.category = :category)
                  AND (:paymentType IS NULL OR o.paymentType = :paymentType)
                  AND (:txnAmount IS NULL OR o.minAmount <= :txnAmount)
                  AND (COALESCE(:userCards, NULL) IS NULL OR o.cardName IN :userCards)
                  AND CURRENT_DATE BETWEEN o.validFrom AND o.validTo
            """)
    List<CardOfferEntity> findEligibleOffersWithCardFilter(
            @Param("merchant") String merchant,
            @Param("category") String category,
            @Param("paymentType") String paymentType,
            @Param("txnAmount") Double txnAmount,
            @Param("userCards") List<String> userCards
    );


}
