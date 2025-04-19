package com.ccrecommender.controller;

import com.ccrecommender.dto.UserCardDTO;
import com.ccrecommender.entity.UserCardEntity;
import com.ccrecommender.mapper.UserCardMapper;
import com.ccrecommender.service.UserCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-cards")
@RequiredArgsConstructor
public class UserCardController {

    private final UserCardService userCardService;

    // 🔹 1. Add a card for a user
    public ResponseEntity<?> addCard(@RequestBody UserCardDTO userCardDTO) {
        try {
            UserCardEntity saved = userCardService.addUserCard(userCardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserCardMapper.toDTO(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 2. View cards of a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getUserCards(@PathVariable Long userId) {
        return ResponseEntity.ok(userCardService.getUserCardNames(userId));
    }
}
