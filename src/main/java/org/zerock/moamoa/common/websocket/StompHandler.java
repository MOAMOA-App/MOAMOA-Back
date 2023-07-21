package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.config.security.JwtTokenProvider;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;    // JWT 토큰을 생성하고 검증하는데 사용되는 유틸리티 클래스

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //  메시지가 채널로 전송되기 전에 호출, 메시지를 가로채고 처리
        // preSend를 오버라이딩해서 CONNECT하는 상황이라면 토큰 검증, 토큰이 유효하지 않다면 예외 발생

        // StompHeaderAccessor: STOMP 프로토콜 메시지의 헤더 정보를 읽고 조작하는데 사용되는 유틸리티 클래스
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 클라이언트가 WebSocket에 연결할 때 호출되는 CONNECT 메시지를 가로채고 토큰의 유효성 검증
        if(accessor.getCommand() == StompCommand.CONNECT) { // CONNECT 메시지인지 확인
            if(!jwtTokenProvider.validate(accessor.getFirstNativeHeader("token")))
                // 메시지에서 "token" 헤더 값을 가져와서 토큰의 유효성을 검증, 유효하지 않으면 클라이언트의 연결 거부
                throw new AccessDeniedException("");
        }
        return message;
    }
}