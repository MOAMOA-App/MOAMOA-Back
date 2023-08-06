package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.common.auth.JwtTokenProvider;

@RequiredArgsConstructor
@Component

public class NoticeHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;    // JWT 토큰을 생성하고 검증하는데 사용되는 유틸리티 클래스

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // WebSocket 연결 요청을 가로채고, 연결하기 전에 JWT 토큰의 유효성을 검사하여 인증 절차를 수행
        // preSend를 오버라이딩해서 CONNECT하는 상황이라면 토큰 검증, 토큰이 유효하지 않다면 예외 발생

        // StompHeaderAccessor: STOMP 프로토콜 메시지의 헤더 정보를 읽고 조작하는데 사용되는 유틸리티 클래스
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 클라이언트가 WebSocket에 연결할 때 호출되는 CONNECT 메시지를 가로채고 토큰의 유효성 검증
        if(accessor.getCommand() == StompCommand.CONNECT) { // CONNECT 메시지인지 확인
            // 임시코드
            if(!jwtTokenProvider.validate(accessor.getFirstNativeHeader("token")))
                // 메시지에서 "token" 헤더 값을 가져와서 토큰의 유효성을 검증, 유효하지 않으면 클라이언트의 연결 거부
                // 근데이게... 메시지토큰이 아니라 유저의 토큰을 검사해야되는거 아닌가?
                // ㄴ 유저랑은 상관없는코드고 WebSocket 연결 요청에 포함된 "token"이라는 이름의 네이티브 헤더를 읽어오는 메서드임

                // WebSocket 클라이언트가 서버로 연결 요청을 보낼 때 헤더 정보가 포함
                // WebSocket 연결 요청의 헤더에서 "token"이라는 이름의 네이티브 헤더 값을 가져오는 역할
                // -> 클라이언트에서 token이라는 헤더 포함시킨걸 가져온다는 뜻인듯
                // 클라이언트가 웹 소켓 연결 시 특정한 정보를 네이티브 헤더에 담아서 서버로 전송하고자 할 때 사용
                // Ex)  JWT 토큰을 WebSocket 연결 요청 헤더의 "token"이라는 네이티브 헤더에 담아서 서버로 전송하는 상황
                // -> WebSocket 클라이언트가 전송한 "token"이라는 네이티브 헤더에 포함된 JWT 토큰을 가져와서 유효성 검사
                // -> 클라이언트의 연결 요청이 인증되었는지를 확인, 인증되지 않은 클라이언트의 연결 요청에 대해서는 접근 거부 가능
                // token이라는 헤더는 어떻게 설정하는가? -> 만약 웹소켓 라이브러리 이용하는거면
                // const socket = io('ws://your-websocket-server-url', {
                //  extraHeaders: {
                //    'token': 'your-jwt-token-here'
                //  }
                // }); 이런식으로 하는게아닐까싶음...
                // 클라이언트에게 알림을 보내려면 클라이언트 측에서 해당 채널을 구독하는 코드를 작성해야 함
                throw new AccessDeniedException("");
        }
        return message;
    }
}