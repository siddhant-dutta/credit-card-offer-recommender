package com.ccrecommender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardDTO {
    private Long id;
    private Long userId;
    private String cardName;
}
