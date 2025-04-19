package com.ccrecommender.mapper;

import com.ccrecommender.dto.UserCardDTO;
import com.ccrecommender.entity.UserCardEntity;

public class UserCardMapper {

    public static UserCardDTO toDTO(UserCardEntity entity) {
        return new UserCardDTO(entity.getId(), entity.getUserId(), entity.getCardName());
    }

    public static UserCardEntity toEntity(UserCardDTO dto) {
        UserCardEntity entity = new UserCardEntity();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setCardName(dto.getCardName());
        return entity;
    }
}
