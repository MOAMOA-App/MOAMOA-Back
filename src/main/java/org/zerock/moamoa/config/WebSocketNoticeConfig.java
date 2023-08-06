package org.zerock.moamoa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.zerock.moamoa.common.websocket.NoticeHandler;

@Configuration
@EnableWebSocketMessageBroker   // WebSocket 메시징 브로커 활성화: 서버-클라이언트 간 메시지 전달 단순화+관리
@RequiredArgsConstructor
public class WebSocketNoticeConfig implements WebSocketMessageBrokerConfigurer {
    private final NoticeHandler noticeHandler; // jwt 토큰 인증 핸들러
    // Stomp: WebSocket 기반 메시지 프로토콜, 클라이언트-서버 간 상호 작용을 단순화에 사용됨

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 연결할 수 있는 엔드포인트를 등록하는 메소드
        // /ws-stomp 경로로 WebSocket 연결이 가능하도록 설정
        registry.addEndpoint("/ws-stomp")    // 프론트가 stomp가 제공하는 시작점(endpoint)를 연결
//                .setHandshakeHandler(new UserHandShakeHandler)  // 프론트에서 websocket 연결할 때마다 서버 로그에 찍어줌
//                .setAllowedOriginPatterns("*")  // 프론트 주소나 로컬 주소. ""나 "*"는 모두 포함이라는 뜻
                .withSockJS();   // sockjs 사용하고 싶을 때
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커의 구성을 수행하는 메소드
        // /ws-stomp로 소켓 연결, /sub/... 을 구독하고 있으면 메세지를 전송
        registry.enableSimpleBroker("/sub");// /sub로 시작하는 주제를 구독하는 클라이언트에게 msg 브로드캐스팅
        registry.setApplicationDestinationPrefixes("/pub"); // /pub로 시작하는 메시지가 메시지 핸들러로 라우팅되도록 설정
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 클라이언트로 들어오는 인바운드 채널 구성 수행
        registration.interceptors(noticeHandler); // 핸들러 등록
    }
}
