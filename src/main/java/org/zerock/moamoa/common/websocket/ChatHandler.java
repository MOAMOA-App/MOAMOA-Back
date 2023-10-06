package org.zerock.moamoa.common.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {
    //현재 연결된 클라이언트들을 관리하기 위한 리스트
    //원래는 클라이언트들을 관리할 서비스를 만들어서 작업
    private final List<WebSocketSession> sessions = new ArrayList<>();

    //사용자 이름으로 세션을 구분하려면?
    // private Map<String, WebSocketSession> sessionByUsername;


    //WebSocketSession : 연결된 클라이언트를 나타내는 객체
    // afterConnectionEstablished: 웹소켓이 연결되면 호출되는 함수
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //방금 참여한 사용자를 저장
        sessions.add(session);
        log.info("connected with session id : {}, total sessions : {}", session.getId(), sessions.size());

    }


    //WebSocket 메세지를 받으면 실행
    // WebSocketSession session : 전송 주체 정보가 담긴 세션
    // TextMessage message : 전송 받은 메세지 정보
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("received : {}", payload);
        for (WebSocketSession connected : sessions) {
            connected.sendMessage(message);
        }
        super.handleTextMessage(session, message);
    }

    //WebSocket 연결이 종료되었을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection with {} closed", session.getId());
        sessions.remove(session);

        // super.afterConnectionClosed(session, status);
    }

    public void broadcast(String message) throws IOException {
        for (WebSocketSession connected : sessions) {
            connected.sendMessage(new TextMessage(message));
        }
    }
}
