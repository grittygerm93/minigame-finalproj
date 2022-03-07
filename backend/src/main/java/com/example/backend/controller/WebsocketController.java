package com.example.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

//@RestController
@Slf4j
@Controller
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
//    private GameService gameService;

    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
//        this.gameService = gameService;
    }

    //    labelling from perspective of client
    @MessageMapping("/sendmessage")
    @SendTo("/topic/receivemsg")
    public String getMessage(final String message) throws InterruptedException {
        log.info(message);
//        messagingTemplate.convertAndSend("/topic/receivemsg", message);
        return HtmlUtils.htmlEscape(message);
    }

    /*@MessageMapping("/gameid")
    public void gameInit(final String gameId) throws InterruptedException {
        log.info("a new player connected");
        PlayerStats playerStats = new PlayerStats();
        gameService.getGameStats().getPlayersStats().put(gameId, playerStats);
        gameService.getGameStats().setNumPlayers(gameService.getGameStats().getPlayersStats().size());
        log.info(gameService.getGameStats().toString());

        messagingTemplate.convertAndSend("/topic/setState", gameService.getGameStats());
//        for (String gid: gameService.getGameIds()) {
//            if(!gid.equals(webSocketSession.getId())) {
//                messagingTemplate.convertAndSendToUser(gid,"/topic/newPlayer", gameService.getGameStats());
//            }
//        }

//        messagingTemplate.convertAndSend("/topic/currentPlayers", message);
//        messagingTemplate.convertAndSend("/topic/newPlayer", message);*/
//    }
}

