package org.zerock.moamoa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.zerock.moamoa.common.websocket.StompHandler;

@Configuration
@EnableWebSocketMessageBroker   // 메시지 브로커가 메시지를 처리할 수 있게 활성화
@RequiredArgsConstructor
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
//    private final StompHandler stompHandler;

    @Override
    // STOMP 엔드포인트 설정용 메소드
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //클라이언트는 해당 엔드포인트를 통해 WebSocket 연결을 수립할 수 있다.
        registry.addEndpoint("/chatting")  // 연결될 엔드포인트
                .setAllowedOriginPatterns("*"); // 허용하는 도메인 주소
//                .withSockJS(); // 웹소켓 사용: 얘를 사용하면 ws:// 대신 http:// 쓰게 됨
        //WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
    }

    @Override
    // enableSimpleBroker를 통해 메시지 브로커가 /topic/chat으로 시작하는 주소 구독자들에게 메시지를 전달하도록 함
    // setApplicationDestinationPrefixes: 클라이언트가 서버로 메시지를 발송할 수 있는 경로의 prefix를 지정
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /app이라고 설정 시 /topic/chat라는 토픽에 대해 구독을 신청했을 때 실제경로: /app/topic/chat
        // registry.setApplicationDestinationPrefixes("/chat", "/topic");
        registry.setApplicationDestinationPrefixes("/app");    // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때


        // /topic은 한 명이 msg 발행했을 때 해당 토픽을 구독하고 있는 n명에게 메시지를 뿌려야 할 때 사용
        // /queue는 한 명이 msg 발행했을 떄 발행한 한 명에게 다시 정보를 보내는 경우
        //registry.enableSimpleBroker("/topic");
        registry.enableSimpleBroker("/topic/chat");  // 메시지를 구독하는 요청 url => 즉 메시지 받을 때


        // 테스트 링크: https://apic.app/online/#/tester
        // request url -> ws://localhost:8080/chatting
        // stomp subscription url -> /topic/chat/{채팅방id}
        // destination queue url -> /app/chat/send
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }
}
