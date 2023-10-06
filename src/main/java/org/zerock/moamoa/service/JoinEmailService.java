package org.zerock.moamoa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.zerock.moamoa.common.email.EmailMessage;
import org.zerock.moamoa.common.email.EmailRequest;
import org.zerock.moamoa.domain.DTO.joinEmails.*;
import org.zerock.moamoa.domain.entity.JoinEmail;
import org.zerock.moamoa.repository.JoinEmailRepository;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinEmailService {
    private final JoinEmailRepository joinEmailRepository;
    private final JavaMailSender javaMailSender;    // JavaMail로 이메일 보냄
    private final SpringTemplateEngine templateEngine;

    // save
    public CompletableFuture<JoinEmailtoClientResponse> saveEmail(JoinEmailRequest request) {
        JoinEmail joinEmail = JoinEmail.toEntity(request);
        joinEmailRepository.save(joinEmail);
        return CompletableFuture.completedFuture(JoinEmailtoClientResponse.toDto(joinEmail.getToken(), "OK"));
    }

    // updatecode
    @Transactional
    public CompletableFuture<JoinEmailtoClientResponse> updateCode(JoinEmailRequest req) {
        JoinEmail temp = joinEmailRepository.findByTokenOrThrow(req.getToken());
        temp.updateCode(req);
        JoinEmailResponse.toDto(temp);
//        return JoinEmailResponse.toDto(temp);
        return CompletableFuture.completedFuture(JoinEmailtoClientResponse.toDto(temp.getToken(), "OK"));
    }

    // updateauth
    @Transactional
    public JoinEmailtoClientResponse updateAuth(JoinEmailAuthUpdateRequest req) {
        // 1. 이메일로 JoinEmail 찾음 (존재하지 않을시 ElseThrow)
        // 2. 코드가 같을 시 authenticate를 true, 다를시 false로 설정
        JoinEmail temp = joinEmailRepository.findByTokenOrThrow(req.getToken());
        Instant expiredTime = temp.getUpdatedAt().plusSeconds(600);

        // req 들어온 시간이 10분보다 길 경우 EXPIRED
        if (req.getSubmissionTime().isAfter(expiredTime)){
            return JoinEmailtoClientResponse.toDto("EXPIRED");
        } else {
            // code 틀릴 시 NOT_CORRECT
            if (!temp.getCode().equals(req.getCode())) {
                return JoinEmailtoClientResponse.toDto("NOT_CORRECT");
            } else {
                temp.updateAuth(req);
                return JoinEmailtoClientResponse.toDto("OK");
            }
        }
    }

    @Async
    @Transactional
    public CompletableFuture<JoinEmailtoClientResponse> sendEmail(EmailRequest emailReq) throws UnsupportedEncodingException, MessagingException {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailReq.getEmail())
                .subject("모아모아에서 발급된 이메일 인증 코드입니다.")
                .build();

        String authCode = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());               // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject());     // 메일 제목
            mimeMessageHelper.setText(setContext(authCode), true);  // 메일 본문 내용 설정, HTML 여부
            mimeMessageHelper.setFrom(new InternetAddress(emailMessage.getFrom(), "MOAMOA"));

            // 메일 보냄, db에 저장
            javaMailSender.send(mimeMessage);

//            String token = UUID.randomUUID().toString();
            JoinEmailRequest emailRequest = new JoinEmailRequest(emailMessage.getTo(), authCode, false);

            if (!joinEmailRepository.existsByEmail(emailRequest.getEmail())) {
                // 이메일 존재하지 않을 시 새로 저장
                return saveEmail(emailRequest);
            } else {
                // 이메일 존재할 시 재전송
                return updateCode(emailRequest);
            }
//            return CompletableFuture.completedFuture(JoinEmailtoClientResponse.toDto(token, "OK"));
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
