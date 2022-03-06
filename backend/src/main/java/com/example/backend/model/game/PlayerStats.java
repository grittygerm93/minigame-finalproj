package com.example.backend.model.game;

import lombok.Data;

@Data
public class PlayerStats {
    int rotation = 0;
    int x = (int)(Math.random() * 800);
    int y = (int)(Math.random() * 600);
    String playerId = null;
}

