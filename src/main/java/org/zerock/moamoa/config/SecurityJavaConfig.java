package org.zerock.moamoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable() // cors 방지-> CORS: 한 출처에 있는 자원에서 다른 출처에 있는 자원에 접근
            .csrf().disable() // csrf 방지-> CSRF: 인터넷 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위
                                                // (수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 만드는 공격
            .formLogin().disable()	// 기본 로그인 페이지 없애기
            .headers().frameOptions().disable();
    }

    @Bean   // @Bean 어노테이션-> @Autowired 통해 PasswordEncoder 선언할때 자동으로 클래스가 바인딩됨
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}