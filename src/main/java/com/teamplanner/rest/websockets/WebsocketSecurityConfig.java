package com.teamplanner.rest.websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().hasRole("USER")
                .simpSubscribeDestMatchers("/user/queue/friendRequests").hasRole("USER")
                .simpDestMatchers("/sockets/**").hasRole("USER");
    }

    @Override
    protected boolean sameOriginDisabled(){
        return true;
    }
}
