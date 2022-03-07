package com.example.backend.controller;

import com.example.backend.model.ChatMessage;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayInputStream;

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

    //   some unknown stuff going on during the interception.. only able to send simple string
    @MessageMapping("/sendmessage")
    @SendTo("/topic/receivemsg")
    public void getMessage(String message) throws InterruptedException {
        log.info(message);
        String[] msgContent = message.split("\\|");
        String msg = msgContent[0];
        String user = msgContent[1];
//        message = HtmlUtils.htmlEscape(message);
//        JsonReader reader = Json.createReader(new ByteArrayInputStream(message.getBytes()));
//        log.info(String.valueOf(reader.readArray().size()));
//        JsonObject msg = reader.readObject().getJsonObject("message");
//        JsonObject user = reader.readObject().getJsonObject("user");
//        log.info(user.toString());
//        log.info(msg.toString());

        messagingTemplate.convertAndSend("/topic/receivemsg", new ChatMessage(msg, user));
//        return HtmlUtils.htmlEscape(message);
    }

/*    //    labelling from perspective of client
    @MessageMapping("/sendmessage")
//    @SendTo("/topic/receivemsg")
    public void getMessage(final ChatMessage chatMessage) throws InterruptedException {
        log.info(chatMessage.toString());
        messagingTemplate.convertAndSend("/topic/receivemsg", chatMessage);
//        return HtmlUtils.htmlEscape(message);
    }*/



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

