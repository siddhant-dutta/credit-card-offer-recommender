package com.ccrecommender.service;

import com.ccrecommender.entity.UserCardEntity;
import com.ccrecommender.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;

    public List<String> getUserCardNames(Long userId) {
        return userCardRepository.findByUserId(userId).stream()
                .map(UserCardEntity::getCardName)
                .collect(Collectors.toList());
    }

    public void addUserCard(Long userId, String cardName) {
        UserCardEntity userCard = new UserCardEntity();
        userCard.setUserId(userId);
        userCard.setCardName(cardName);
        userCardRepository.save(userCard);
    }
}
