package com.example.editor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configuring a simple in-memory message broker with a destination prefix "/topic"
        config.enableSimpleBroker("/topic");
        // Application destination prefix for messages bound for @MessageMapping-annotated methods
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registering an endpoint that the clients will use to connect to the WebSocket server
        // registry.addEndpoint("/ws").withSockJS();
        // registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(rmeSessionChannelInterceptor());
    }

    @Bean
    public RmeSessionChannelInterceptor rmeSessionChannelInterceptor() {
    return new RmeSessionChannelInterceptor();
    }
}

