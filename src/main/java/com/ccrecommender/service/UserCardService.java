package com.ccrecommender.service;

import com.ccrecommender.dto.UserCardDTO;
import com.ccrecommender.entity.UserCardEntity;
import com.ccrecommender.exception.DuplicateCardException;
import com.ccrecommender.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;

    // Fetch card names for a given user
    public List<String> getUserCardNames(Long userId) {
        return userCardRepository.findByUserId(userId).stream()
                .map(UserCardEntity::getCardName)
                .collect(Collectors.toList());
    }

    // Add a new user card
    public UserCardEntity addUserCard(UserCardDTO userCardDTO) {
        validateDuplicateCard(userCardDTO.getUserId(), userCardDTO.getCardName());

        UserCardEntity userCard = createUserCard(userCardDTO);
        return userCardRepository.save(userCard);
    }

    // Validate if the card already exists for the user
    private void validateDuplicateCard(Long userId, String cardName) {
        boolean alreadyExists = userCardRepository.existsByUserIdAndCardName(userId, cardName);
        if (alreadyExists) {
            throw new DuplicateCardException("This card has already been added for this user.");
        }
    }

    // Convert DTO to entity
    private UserCardEntity createUserCard(UserCardDTO userCardDTO) {
        UserCardEntity userCard = new UserCardEntity();
        userCard.setUserId(userCardDTO.getUserId());
        userCard.setCardName(userCardDTO.getCardName());
        // Additional fields if any can be set here
        return userCard;
    }
}
