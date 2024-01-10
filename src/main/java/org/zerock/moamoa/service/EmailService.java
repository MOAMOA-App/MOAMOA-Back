package org.zerock.moamoa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.zerock.moamoa.common.user.EmailMessage;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.EmailAddrRequest;
import org.zerock.moamoa.domain.DTO.email.EmailAuthUpdateRequest;
import org.zerock.moamoa.domain.DTO.email.EmailRequest;
import org.zerock.moamoa.domain.DTO.email.EmailTokenResponse;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.EmailType;
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

    @Async
    @Transactional
    public CompletableFuture<EmailTokenResponse> sendEmail(EmailAddrRequest emailAddrReq){
        try {
            EmailMessage emailMessage = EmailMessage.builder().to(emailAddrReq.getEmail()).build();
            String authCode = createCode(6);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());               // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject());     // 메일 제목
            mimeMessageHelper.setText(setContext(authCode), true);  // 메일 본문 내용 설정, HTML 여부
            mimeMessageHelper.setFrom(new InternetAddress(emailMessage.getFrom(), "MOAMOA"));

            javaMailSender.send(mimeMessage);
            return saveEmail(new EmailRequest(emailMessage.getTo(), authCode, emailAddrReq.type));

        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<EmailTokenResponse> saveEmail(EmailRequest emailReq) {
        Email email;
        if (!emailRepository.existsByEmail(emailReq.getEmail())) {
            email = Email.toEntity(emailReq);
            emailRepository.save(email);
        } else {
            email = emailRepository.findByEmailOrThrow(emailReq.getEmail());
            email.updateCode(emailReq);
        }
        return CompletableFuture.completedFuture(
                EmailTokenResponse.toDto(tokenEncoder(email.getToken()), "OK"));
    }

    @Transactional
    public ResultResponse updateAuth(EmailAuthUpdateRequest req) {
        Email temp = emailRepository.findByTokenOrThrow(tokenDecoder(req.getToken()));
        Instant expiredTime = temp.getUpdatedAt().plusSeconds(600);

        if (req.getSubmissionTime().isAfter(expiredTime)) return ResultResponse.toDto("EXPIRED");
        if (!temp.getCode().equals(req.getCode())) return ResultResponse.toDto("NOT_CORRECT");
        if (req.getType() == EmailType.EMAIL_PW) {
            User user = userRepository.findByEmailOrThrow(temp.getEmail());
            if (user.getPassword() == null) return ResultResponse.toDto("소셜로그인한 회원입니다.");
        }
        temp.updateAuth(req);
        return ResultResponse.toDto("OK");
    }

    public static String createCode(int num) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < num; i++) {
            key.append(new Random().nextInt(10));
        }
        return key.toString();
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

    // thymeleaf 통해 HTML 형식의 이메일 본문 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("emailform", context);    // 실제 .html 파일의 템플릿 이름 작성
    }
}
