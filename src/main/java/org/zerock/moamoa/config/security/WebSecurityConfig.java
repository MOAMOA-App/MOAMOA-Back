package org.zerock.moamoa.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	@Bean //메소드의 결과를 @Bean 객체로 등록해주는 어노테이션
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			//CSRF : 사이트 사이간 위조 방지 해제 (disable)
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				authHttp -> authHttp
					.requestMatchers(
						"/product/**",
						"/announce/**"
					).permitAll()        //누구든 허용
					.requestMatchers(
						"/myinfo/**"
					).authenticated()    //인증된 사용자만 허용
					.requestMatchers(
						"/user/**"
					).anonymous()    //인증되지 않은 사용자만 허용
					.anyRequest().permitAll()
			)
			.sessionManagement(
				sessionManagement -> sessionManagement
					.sessionCreationPolicy((SessionCreationPolicy.STATELESS))
			);

		return http.build();

	}

	@Bean   // @Bean 어노테이션-> @Autowired 통해 PasswordEncoder 선언할때 자동으로 클래스가 바인딩됨
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}