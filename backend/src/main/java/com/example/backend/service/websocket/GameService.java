package com.example.backend.service.websocket;

import com.example.backend.model.game.GameStats;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class GameService {
    private GameStats gameStats = new GameStats();
    private List<String> gameIds = new ArrayList<>();

/*    public boolean isOldGameId(String gameId) {
        for (String s : gameIds) {
            if (s.equals(gameId)) {
                return true;
            }
        }
        return false;
    }*/

}
