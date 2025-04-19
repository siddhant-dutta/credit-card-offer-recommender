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

    // Query when userCards is non-null and non-empty
    @Query(value = """
                SELECT * FROM card_offer o
                WHERE (:merchant IS NULL OR LOWER(o.merchant) LIKE LOWER(CONCAT('%', :merchant, '%')))
                  AND (:category IS NULL OR LOWER(o.category) LIKE LOWER(CONCAT('%', :category, '%')))
                  AND (:paymentType IS NULL OR LOWER(o.payment_type) LIKE LOWER(CONCAT('%', :paymentType, '%')))
                  AND (:txnAmount IS NULL OR o.min_amount <= :txnAmount)
                  AND o.card_name = ANY(CAST(:userCards AS text[]))
                  AND CURRENT_DATE BETWEEN o.valid_from AND o.valid_to
            """, nativeQuery = true)
    List<CardOfferEntity> findEligibleOffersWithCardFilterNonNullCards(
            @Param("merchant") String merchant,
            @Param("category") String category,
            @Param("paymentType") String paymentType,
            @Param("txnAmount") Double txnAmount,
            @Param("userCards") List<String> userCards
    );

    // Query when userCards is null or empty
    @Query(value = """
                SELECT * FROM card_offer o
                WHERE (:merchant IS NULL OR LOWER(o.merchant) LIKE LOWER(CONCAT('%', :merchant, '%')))
                  AND (:category IS NULL OR LOWER(o.category) LIKE LOWER(CONCAT('%', :category, '%')))
                  AND (:paymentType IS NULL OR LOWER(o.payment_type) LIKE LOWER(CONCAT('%', :paymentType, '%')))
                  AND (:txnAmount IS NULL OR o.min_amount <= :txnAmount)
                  AND CURRENT_DATE BETWEEN o.valid_from AND o.valid_to
            """, nativeQuery = true)
    List<CardOfferEntity> findEligibleOffersWithCardFilterNullCards(
            @Param("merchant") String merchant,
            @Param("category") String category,
            @Param("paymentType") String paymentType,
            @Param("txnAmount") Double txnAmount
    );

}
