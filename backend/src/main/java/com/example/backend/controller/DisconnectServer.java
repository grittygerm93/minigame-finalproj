/*
package com.example.backend.controller;

import com.example.backend.model.game.GameStats;
import com.example.backend.service.websocket.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class DisconnectServer implements WebSocketHandler {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        gameService.getGameIds().add(session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @MessageMapping("/disconnect")
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String myGameId = session.getId();
        log.info("a player disconnected, gameId is {}", myGameId);
        GameStats gameStats = gameService.getGameStats();
        gameStats.getPlayersStats().remove(myGameId);
        gameStats.setNumPlayers(gameStats.getPlayersStats().size());
        log.info(gameStats.toString());
        messagingTemplate.convertAndSend("/topic/disconnect", myGameId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}
*/
