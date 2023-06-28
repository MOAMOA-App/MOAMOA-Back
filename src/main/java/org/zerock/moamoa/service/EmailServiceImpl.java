package org.zerock.moamoa.service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {  // EmailService 인터페이스 구현
    @Autowired
    JavaMailSender emailSender; // JavaMail을 사용하여 이메일을 보내는 데 사용

    // 인증 코드를 저장하기 위한 변수, createKey() 메소드를 호출해 8자리의 랜덤한 인증 코드 생성
    public static final String EmailAuthCode = createKey();

    private MimeMessage createMessage(String targetEmail) throws Exception {  // 이메일 메시지 생성하는 메소드
        // 이메일 수신자, 제목, 내용 등 설정하고 MimeMessage 객체 반환
        // MimeMessage: JavaMail API에서 이메일 메시지 나타내는 클래스. 이메일의 제목/본문/수신자/발신자 등의 속성 설정 가능

        System.out.println("보내는 대상 : " + targetEmail);
        System.out.println("인증 번호 : " + EmailAuthCode);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, targetEmail);   //보내는 대상
        message.setSubject("이메일 인증 테스트");   //제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<p>아래 코드를 복사해 입력해 주세요.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += EmailAuthCode + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("properties에 입력한 이메일", "MOAMOA"));   //보내는 사람

        return message;
    }

    public static String createKey() {  // 8자리의 랜덤한 인증 코드 생성
        StringBuilder key = new StringBuilder();    // key: 인증 코드 저장 목적
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0부터 2까지의 랜덤한 정수 값 저장

            switch (index) {    // index 값에 따라 다른 문자를 key 객체에 추가
                case 0: // 소문자 알파벳(a~z) 중 하나 추가 (ex. 1+97=98 => (char)98 = 'b')
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1: // 대문자 알파벳(A~Z) 중 하나 추가
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2: // 0부터 9까지의 숫자 중 하나 추가
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();  // key 객체를 문자열로 변환하여 반환
    }

    @Override
    public String sendSimpleMessage(String targetEmail) throws Exception {  // EmailService 인터페이스의 메소드 구현
        // targetEmail 매개변수를 받아 createMessage 메소드를 호출하여 MimeMessage 객체 생성,
        MimeMessage message = createMessage(targetEmail);
        try {    // 예외처리
            emailSender.send(message);  // emailSender.send(message) 사용해 이메일 보냄
        } catch (MailException es) {
            es.printStackTrace();   // 예외 발생 시 MailException 처리하고 예외 던짐
            throw new IllegalArgumentException();
        }
        return EmailAuthCode;   // EmailAuthCode 값 반환
    }
}