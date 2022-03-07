package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private ChannelInterceptor channelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/our-websocket").setAllowedOrigins("http://localhost:4200").withSockJS();
//        registry.addEndpoint("/our-websocket").setAllowedOrigins("https://minigame-fp.herokuapp.com").withSockJS();

//        registry.addEndpoint("/gamews").setAllowedOrigins("http://localhost:4200").withSockJS();
//        registry.addEndpoint("/our-websocket");
//        https://stackoverflow.com/questions/67920378/spring-boot-websocket-without-sockjs
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/ws", "/gamews");
        registry.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}
