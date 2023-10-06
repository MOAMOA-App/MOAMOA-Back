package org.zerock.moamoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   // WebSocket 서버 활성화
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    //STOMP 엔드포인트 설정용 메소드
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //클라이언트는 해당 엔드포인트를 통해 WebSocket 연결을 수립할 수 있다.
        registry.addEndpoint("/chatting")  // 연결될 엔드포인트
                .setAllowedOriginPatterns("*") // 허용하는 도메인 주소
                .withSockJS(); // 웹 소켓 사용
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
        registry.setApplicationDestinationPrefixes("/chat");    // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
        //registry.setApplicationDestinationPrefixes("/chat", "/topic");

        registry.enableSimpleBroker("/queue");  // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
        //SimpleBroker의 기능과 외부 Message Broker( RabbitMQ, ActiveMQ 등 )에 메세지를 전달하는 기능을 가짐

        // setApplicationDestinationPrefixes: 도착경로의 prefix 설정
        // -> /app이라고 설정해두면 /topic/hello라는 토픽에 대해 구독을 신청했을 때 실제경로는 /app/topic/hello가 됨

        // enableSimpleBroker: 메시지브로커 등록하는 코드. 해당하는 경로를 구독하는 클라이언트에게 메세지를 전달하는 작업 수행
        // /topic은 한 명이 msg 발행했을 때 해당 토픽을 구독하고 있는 n명에게 메시지를 뿌려야 할 때 사용
        // queue는 한 명이 msg 발횅했을 떄 발행한 한 명에게 다시 정보를 보내는 경우. 우리는 1:1이니까 queue 쓰면될듯
    }
}
