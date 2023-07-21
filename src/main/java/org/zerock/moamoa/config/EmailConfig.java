package org.zerock.moamoa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.zerock.moamoa.common.email.EmailMessage;

import java.util.Properties;

@Configuration
public class EmailConfig {  // 이메일 발송을 위한 JavaMailSender 설정 제공
    @Value("${mail.username}")  // 환경 변수로부터 아이디 값 가져오기
    private String emailUsername;

    @Value("${mail.password}")  // 환경 변수로부터 비밀번호 값 가져오기
    private String emailPassword;

    // JavaMailSender 인터페이스를 구현한 JavaMailSenderImpl 객체를 생성하고 필요한 속성들을 설정하여 반환
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername(emailUsername);
        javaMailSender.setPassword(emailPassword);

        javaMailSender.setPort(465);

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
        properties.setProperty("mail.smtp.ssl.enable","true");
        return properties;
    }

    @Bean
    public EmailMessage emailMessage() {
        return EmailMessage.builder().build();
    }
}
