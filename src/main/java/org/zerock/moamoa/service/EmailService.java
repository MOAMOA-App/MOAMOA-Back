package org.zerock.moamoa.service;

public interface EmailService {
    // 단순한 메시지 보내는 기능 제공 위한 메소드 sendSimpleMessage 선언
    // targetEmail 이라는 매개변수로 수신자의 이메일 주소를 받고, 문자열로 된 결과를 반환
    String sendSimpleMessage(String targetEmail) throws Exception;
}