package org.zerock.moamoa.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.zerock.moamoa.common.auth.JwtTokenFilter;
import org.zerock.moamoa.common.auth.oAuth.OAuth2SuccessHandler;
import org.zerock.moamoa.common.auth.oAuth.OAuth2UserServiceImpl;

@Configuration
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserServiceImpl oAuth2UserService;

    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2UserServiceImpl oAuth2UserService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean //메소드의 결과를 @Bean 객체로 등록해주는 어노테이션
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
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
                                ).anonymous()        //인증되지 않은 사용자만 허용
                                .anyRequest().permitAll()
                )

                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy((SessionCreationPolicy.STATELESS))
                ).addFilterBefore(
                        jwtTokenFilter, AuthorizationFilter.class
                ).exceptionHandling(i -> i
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler())
                );


        return httpSecurity.build();

    }

    // 1. OAuth2SuccessHandler는 UserDetailsManager를 필요하게 바뀌었다.
    // 2. UserDetailsManager는 WebSecurityConfig에 정의해둔 PasswordEncoder
    //      Bean 객체가 필요했다.
    // 3. WebSecurityConfig는 OAuth2SuccessHandler가 필요했다.
    //      (Circular Dependency)
    // 4. WebSecurityConfig에서 PasswordEncoder를 분리했다.
    //   -> UserDetailsManager는 더이상 WebSecurityConfig를 필요로 하지 않게 된다.
    // 5. Circular Dependency가 해소된다.
}