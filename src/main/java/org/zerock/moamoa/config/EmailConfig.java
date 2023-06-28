package org.zerock.moamoa.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {  // 이메일 발송을 위한 JavaMailSender 설정을 제공
    // JavaMailSender 인터페이스를 구현한 JavaMailSenderImpl 객체를 생성하고 필요한 속성들을 설정하여 반환

    @Value("${mail.smtp.port}") // @Value 어노테이션: properties 값 읽어옴
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;
    @Value("${AdminMail.id}")
    private String id;
    @Value("${AdminMail.password}")
    private String password;
    @Bean
    public JavaMailSender javaMailService() {   // JavaMailSenderImpl 객체를 생성하고 설정한 후 반환
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");   // 호스트(host)를 smtp.gmail.com로 설정
        javaMailSender.setUsername(id);         // 사용자 이름(username)과 비밀번호(password)를
        javaMailSender.setPassword(password);   // id와 password 변수로부터 주입받은 값으로 설정
        javaMailSender.setPort(port);   // 포트(port)를 port 변수로부터 주입받은 값으로 설정
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8"); // 이메일의 기본 인코딩을 UTF-8로 설정
        return javaMailSender;
    }
    private Properties getMailProperties() {    // 이메일 전송에 필요한 속성들을 설정한 Properties 객체 반환
        Properties pt = new Properties();
        // 이메일 서버와의 연결 및 보안 설정을 위해 사용되는 속성들
        pt.put("mail.smtp.socketFactory.port", socketPort); // 소켓 포트
        pt.put("mail.smtp.auth", auth); // 인증 여부
        pt.put("mail.smtp.starttls.enable", starttls);  // STARTTLS 사용 여부
        pt.put("mail.smtp.starttls.required", startlls_required);   // STARTTLS 필수 여부
        pt.put("mail.smtp.socketFactory.fallback",fallback);    // 소켓 포트 폴백
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  // 소켓 팩토리 클래스
        return pt;
    }
}
