package com.ccrecommender.repository;

import com.ccrecommender.entity.UserCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardRepository extends JpaRepository<UserCardEntity, Long> {
    List<UserCardEntity> findByUserId(Long userId);
}
