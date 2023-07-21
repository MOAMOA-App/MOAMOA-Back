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
@EnableWebSocketMessageBroker   // WebSocket 메시징 브로커 활성화: 서버-클라이언트 간 메시지 전달 단순화+관리
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler; // jwt 토큰 인증 핸들러
    // Stomp: WebSocket 기반 메시지 프로토콜, 클라이언트-서버 간 상호 작용을 단순화에 사용됨

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 연결할 수 있는 엔드포인트를 등록하는 메소드
        // /ws-stomp 경로로 WebSocket 연결이 가능하도록 설정
        registry.addEndpoint("/ws-stomp")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커의 구성을 수행하는 메소드
        registry.enableSimpleBroker("/sub");// /sub로 시작하는 주제를 구독하는 클라이언트에게
                                                            // 메시지 브로드캐스팅 가능
        registry.setApplicationDestinationPrefixes("/pub"); // /pub로 시작하는 메시지가 메시지 핸들러로 라우팅되도록 설정
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 클라이언트로 들어오는 인바운드 채널 구성 수행
        registration.interceptors(stompHandler); // 핸들러 등록
    }
}
