package org.zerock.moamoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    //STOMP 엔드포인트 설정용 메소드
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //클라이언트는 해당 엔드포인트를 통해 WebSocket 연결을 수립할 수 있다.
        registry.addEndpoint("/chatting");
        //WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
    }

    @Override
    // MessageBroker 를 활용하는 방법 설정
    //MessageBroker : 메세지를 주고받는 미들웨어를 통칭하는 용어
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메시지 브로커가 구독 중인 클라이언트에게 메시지를 전달할 경로를 설정 : 브로드캐스팅
        //registry.enableSimpleBroker("/topic");

        //클라이언트에서 서버로 메시지를 전송할 때 사용할 경로의 prefix를 설정
        ///app 으로 시작하는 경로로 메시지를 전송하는 경우 서버에서 해당 메시지를 처리
        registry.setApplicationDestinationPrefixes("/chat");
        //registry.setApplicationDestinationPrefixes("/chat", "/topic");
    }
}
