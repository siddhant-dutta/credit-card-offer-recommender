package com.ccrecommender.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_card", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "card_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String cardName;

    private String network;

    private String cardType;

    private String category;
}
