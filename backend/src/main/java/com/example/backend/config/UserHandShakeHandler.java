/*
package com.example.backend.config;

import com.example.backend.service.websocket.GameService;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class UserHandShakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandShakeHandler.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private WebSocketSession webSocketSession;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        LOG.info("user with ID {} opened the page", webSocketSession.getId());
        gameService.getGameIds().add(webSocketSession.getId());

        return new UserPrincipal(webSocketSession.getId());
    }

}

*/
