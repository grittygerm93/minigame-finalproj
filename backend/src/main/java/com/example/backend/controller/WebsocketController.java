package com.example.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

//@RestController
@Slf4j
@Controller
public class WebsocketController {

//    private final SimpMessagingTemplate messagingTemplate;

//    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }

    //    labelling from perspective of client
    @MessageMapping("/sendmessage")
    @SendTo("/topic/receivemsg")
    public String getMessage(final String message) throws InterruptedException {
        log.info(message);
        Thread.sleep(1000);
//        messagingTemplate.convertAndSend("/topic/receivemsg", message);
        return HtmlUtils.htmlEscape(message);
    }


}
