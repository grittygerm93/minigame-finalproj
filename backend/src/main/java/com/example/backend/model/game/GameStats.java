package com.example.backend.model.game;

import lombok.Data;

import java.util.HashMap;

@Data
public class GameStats {
    private HashMap<String, PlayerStats> playersStats = new HashMap<>();
    private int numPlayers;
}
