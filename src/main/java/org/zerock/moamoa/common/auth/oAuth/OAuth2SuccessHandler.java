package org.zerock.moamoa.common.auth.oAuth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.domain.DTO.user.UserSignupRequest;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.UserService;

import java.io.IOException;

@Slf4j
@Component
// OAuth2 통신이 성공적으로 끝났을 때, 사용하는 클래스
// JWT를 활용한 인증 구현하고 있기 때문에
// ID Provider에게 받은 정보를 바탕으로 JWT를 발급하는 역할을 하는 용도
// JWT를 발급하고 클라이언트가 저장할 수 있도록 특정 URL로 리다이렉트 시키자.
public class OAuth2SuccessHandler
        // 인증 성공 후 특정 URL로 리다이렉트 시키고 싶을 때 활용할 수 있는
        // successHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsManager userDetailsManager;
    private final AuthService authService;
    private final UserService userService;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserDetailsManager userDetailsManager,
                                AuthService authService, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsManager = userDetailsManager;
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    // 인증 성공시 호출되는 메소드
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String name = oAuth2User.getAttribute("name");
        String username = oAuth2User.getAttribute("email");
        String loginType = oAuth2User.getAttribute("loginType");
        String token = oAuth2User.getAttribute("id").toString();
        log.info(username + "님이 " + loginType + "로 회원가입 하셨습니다.");
        // 처음으로 소셜 로그인한 사용자를 데이터베이스에 등록
        String nick = StringMaker.printRandNick(); // 닉네임 랜덤설정
        UserSignupRequest saveRequest = new UserSignupRequest(nick, username, null);
        switch (loginType) {
            case "kakao" -> saveRequest.setKakao(token);
            case "naver" -> saveRequest.setNaver(token);
        }
        userService.oAuthSaveUser(saveRequest);

        // 데이터베이스에서 사용자 회수
        CustomUserDetails details
                = (CustomUserDetails) userDetailsManager.loadUserByUsername(username);
        UserLoginResponse userLoginResponse = authService.OAuthToken(details);
        String accessToken = userLoginResponse.getAccessToken();
        String refreshToken = userLoginResponse.getRefreshToken();


        // 목적지 URL 설정
        // 우리 서비스의 Frontend 구성에 따라 유연하게 대처해야 한다.
        String targetUrl =
                "http://localhost:8080/user/oauth?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        // 실제 Redirect 응답 생성
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
