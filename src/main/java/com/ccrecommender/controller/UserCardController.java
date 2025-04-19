package com.ccrecommender.controller;

import com.ccrecommender.service.UserCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-cards")
@RequiredArgsConstructor
public class UserCardController {

    private final UserCardService userCardService;

    // 🔹 1. Add a card for a user
    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addUserCard(
            @PathVariable Long userId,
            @RequestParam String cardName
    ) {
        userCardService.addUserCard(userId, cardName);
        return ResponseEntity.ok("Card added for user ID: " + userId);
    }

    // 🔹 2. View cards of a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getUserCards(@PathVariable Long userId) {
        return ResponseEntity.ok(userCardService.getUserCardNames(userId));
    }
}
