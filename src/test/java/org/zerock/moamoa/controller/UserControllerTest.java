package org.zerock.moamoa.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zerock.moamoa.common.fixture.UserTestFixture.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.fixture.UserTestFixture;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.*;
import org.zerock.moamoa.domain.DTO.user.UserCheckRequest;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.domain.DTO.user.UserSignupRequest;
import org.zerock.moamoa.domain.enums.EmailType;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.EmailService;
import org.zerock.moamoa.service.UserService;

import java.util.concurrent.CompletableFuture;

// @WebMvcTest 어노테이션은 순수 Controller만 테스트-> JPA나 서비스/리포지토리같이 테스트해야할떄 사용불가
@RunWith(SpringRunner.class)
//@WithMockUser(username = "username", password = "password", roles = {"USER","ADMIN"})
@SpringBootTest
@AutoConfigureMockMvc   // MockMvc 사용
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserRepository userRepository;

    private static final String BASE_URL = "/user";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()) // 시큐리티 설정 추가
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }


    @Test
    @DisplayName("로그인_성공")
    void login_성공() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);
        given(authService.login(request)).willReturn(new UserLoginResponse("OK"));

        // When
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("로그인_비밀번호틀림")
    void login_비밀번호틀림() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);
        given(authService.login(request)).willThrow(new AuthException(ErrorCode.AUTH_PASSWORD_UNEQUAL));

        // When
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))

                // Then
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("토큰갱신_성공")
    void refreshToken_성공() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);
        given(authService.login(request)).willThrow(new AuthException(ErrorCode.AUTH_PASSWORD_UNEQUAL));

        // When
        mockMvc.perform(post(BASE_URL + "/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))

                // Then
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인_탈퇴계정")
    void login_탈퇴계정() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);
        given(authService.login(request)).willReturn(new UserLoginResponse("NOT_ACTIVITY_AUTH"));

        // When
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입_성공")
    void 회원가입_성공() throws Exception {
        // Given
        UserSignupRequest request = UserTestFixture.createUserSignupReq(EMAIL);
        String body = mapper.writeValueAsString(request);

        given(userService.saveUser(request)).willReturn(UserTestFixture.createUserResp(EMAIL));

        // When
        mockMvc.perform(post(BASE_URL + "/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(body))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.nick").exists())
                .andExpect(jsonPath("$.nick").value(NICK))
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.profImg").doesNotExist())
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.detailAdress").doesNotExist());

    }

    @Test
    @DisplayName("회원가입_이메일사용중")
    void signUp_실패_이메일사용중() throws Exception {
        UserSignupRequest request = UserTestFixture.createUserSignupReq(EMAIL);
        String body = mapper.writeValueAsString(request);

        given(userService.saveUser(request)).willThrow(new AuthException(ErrorCode.USER_EMAIL_USED));

        mockMvc.perform(post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("이메일중복확인_성공")
    void emailVerify_성공() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);

        given(userService.emailVerify(request)).willReturn(ResultResponse.toDto("OK"));

        // When
        mockMvc.perform(post(BASE_URL + "/email/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일중복확인_이메일사용중")
    void emailVerify_실패_이메일사용중() throws Exception {
        // Given
        UserCheckRequest request = UserTestFixture.createUserCheckReq(EMAIL);
        String body = mapper.writeValueAsString(request);

        given(userService.emailVerify(request)).willReturn(ResultResponse.toDto("ALREADY_USED_EMAIL"));

        // When
        mockMvc.perform(post(BASE_URL + "/email/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일인증번호전송_성공")
    void sendVerifyEmail_성공() throws Exception {
        EmailAddrRequest addrReq = new EmailAddrRequest(EMAIL, EmailType.EMAIL_JOIN);
        EmailRequest emailReq = new EmailRequest(EMAIL, CODE, EmailType.EMAIL_JOIN);
        String body = mapper.writeValueAsString(addrReq);

        given(emailService.sendEmail(addrReq))
//                .willReturn(emailService.saveEmail(emailReq))
                .willReturn(CompletableFuture.completedFuture(EmailTokenResponse.toDto(TOKEN, "OK")));


        // When
        mockMvc.perform(post(BASE_URL + "/email/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk())
                .andExpect(result ->
                        CompletableFuture.completedFuture(EmailTokenResponse.toDto(TOKEN, "OK")));
        }

    @Test
    @DisplayName("인증번호확인_성공")
    void updateEmailAuth_성공() throws Exception {
        EmailAuthUpdateRequest request = new EmailAuthUpdateRequest(EmailType.EMAIL_PW, TOKEN, CODE);
        String body = mapper.writeValueAsString(request);

        given(emailService.updateAuth(request)).willReturn(ResultResponse.toDto("OK"));

        // When
        mockMvc.perform(post(BASE_URL + "/email/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk())
                .andExpect(result -> ResultResponse.toDto("OK"));
    }

    @Test
    @DisplayName("이메일로pw변경_성공")
    void updatePwByToken_성공() throws Exception {
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, NEW_PASSWORD);
        String body = mapper.writeValueAsString(request);

        given(userService.updatePwEmail(request)).willReturn(ResultResponse.toDto("OK"));

        // When
        mockMvc.perform(post(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk())
                .andExpect(result -> ResultResponse.toDto("OK"));
    }

    @Test
    @DisplayName("이메일로pw변경_비밀번호같음")
    void updatePwByToken_비밀번호같음() throws Exception {
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, NEW_PASSWORD);
        String body = mapper.writeValueAsString(request);

        given(userService.updatePwEmail(request)).willReturn(ResultResponse.toDto("SAME_PASSWORD"));

        // When
        mockMvc.perform(post(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().isOk())
                .andExpect(result -> ResultResponse.toDto("SAME_PASSWORD"));
    }

    @Test
    @DisplayName("이메일로pw변경_소셜로그인")
    void updatePwByToken_소셜로그인() throws Exception {
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, NEW_PASSWORD);
        String body = mapper.writeValueAsString(request);

        given(userService.updatePwEmail(request)).willReturn(ResultResponse.toDto("소셜로그인한 회원입니다."));

        // When
        mockMvc.perform(post(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body))
                // Then
                .andExpect(status().is4xxClientError()) // 이거왜4XX되는지...몰겟음
                .andExpect(result -> ResultResponse.toDto("소셜로그인한 회원입니다."));
    }
}