package org.zerock.moamoa.common.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import lombok.extern.slf4j.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;    // JavaMail로 이메일 보냄
    private final SpringTemplateEngine templateEngine;

    @Async
    public String sendMail(EmailMessage emailMessage) throws UnsupportedEncodingException, MessagingException {
        String authCode = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());               // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject());     // 메일 제목
            mimeMessageHelper.setText(setContext(authCode), true);  // 메일 본문 내용 설정, HTML 여부
            mimeMessageHelper.setFrom(new InternetAddress(emailMessage.getFrom(), "MOAMOA"));

            javaMailSender.send(mimeMessage);
            return authCode;

        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public static String createCode() {
        Random rand = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            key.append(rand.nextInt(10));
        }
        return key.toString();
    }

    // thymeleaf 통해 HTML 형식의 이메일 본문 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("emailform", context);    // 실제 .html 파일의 템플릿 이름 작성
    }
}