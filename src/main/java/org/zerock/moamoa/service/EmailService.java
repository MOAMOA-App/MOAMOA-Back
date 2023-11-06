package org.zerock.moamoa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.zerock.moamoa.common.user.EmailMessage;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.EmailAddrRequest;
import org.zerock.moamoa.domain.DTO.email.*;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmailRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;    // JavaMail로 이메일 보냄
    private final SpringTemplateEngine templateEngine;
    private final String JASYPT_PW = "mypw123!";
    private final String JASYPT_ALGORITHM = "PBEWITHMD5ANDDES";

    // save
    public CompletableFuture<EmailtoClientResponse> saveEmail(EmailRequest request) {
        Email email = Email.toEntity(request);  // 여기서 토큰 저장
        emailRepository.save(email);
        return CompletableFuture.completedFuture(
                EmailtoClientResponse.toDto(tokenEncoder(email.getToken()), "OK"));
    }

    // updatecode
    @Transactional
    public CompletableFuture<EmailtoClientResponse> updateCode(EmailRequest req) {
        Email temp = emailRepository.findByEmailOrThrow(req.getEmail());
        temp.updateCode(req);   // 여기서 토큰 저장
        EmailResponse.toDto(temp);
        return CompletableFuture.completedFuture(
                EmailtoClientResponse.toDto(tokenEncoder(temp.getToken()), "OK"));
    }

    // updateauth
    @Transactional
    public ResultResponse updateAuth(EmailAuthUpdateRequest req) {
        // 1. 이메일로 JoinEmail 찾음 (존재하지 않을시 ElseThrow)
        // 2. 코드가 같을 시 authenticate를 true, 다를시 false로 설정
        Email temp = emailRepository.findByTokenOrThrow(tokenDecoder(req.getToken()));
        Instant expiredTime = temp.getUpdatedAt().plusSeconds(600);

        // req 들어온 시간이 10분보다 길 경우 EXPIRED
        if (req.getSubmissionTime().isAfter(expiredTime)){
            return ResultResponse.toDto("EXPIRED");
        } else {
            // code 틀릴 시 NOT_CORRECT
            if (!temp.getCode().equals(req.getCode())) {
                return ResultResponse.toDto("NOT_CORRECT");
            } else {
                // 여기서 type1일시 이메일로 유저 찾아서 소셜로그인한 회원인지 검사
                if (req.getType().getCode() == 1) {
                    User user = userRepository.findByEmailOrThrow(temp.getEmail());
                    if (user.getPassword() == null){
                        return ResultResponse.toDto("소셜로그인한 회원입니다.");
                    }
                }
                temp.updateAuth(req);
                return ResultResponse.toDto("OK");
            }
        }
    }

    @Async
    @Transactional
    public CompletableFuture<EmailtoClientResponse> sendEmail(EmailAddrRequest emailReq) throws UnsupportedEncodingException, MessagingException {
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

            EmailRequest emailRequest = new EmailRequest(emailMessage.getTo(), authCode, emailReq.type);

            if (!emailRepository.existsByEmail(emailRequest.getEmail())) {
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
    private String tokenEncoder(String token){
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(JASYPT_PW);
        jasypt.setAlgorithm(JASYPT_ALGORITHM);

        return jasypt.encrypt(token);
    }

    private String tokenDecoder(String token){
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(JASYPT_PW);
        jasypt.setAlgorithm(JASYPT_ALGORITHM);

        return jasypt.decrypt(token);
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
