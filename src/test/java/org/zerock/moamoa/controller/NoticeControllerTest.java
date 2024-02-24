package org.zerock.moamoa.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NoticeControllerTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    NoticeController noticeController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("SSE에 연결을 진행한다.")
    public void subscribe() throws Exception {
//        CustomUserDetails userDetails = new CustomUserDetails(1L, "name", "pw");
//        String lastEventId = "123";  // 이전 이벤트 ID 설정

//        ResponseEntity<String> response = restTemplate.getForEntity("/subscribe?lastEventId=" + lastEventId, String.class);

//        // 테스트 결과 검증
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertEquals("text/event-stream;charset=UTF-8", Objects.requireNonNull(response.getHeaders().getContentType()).toString());

        // 테스트 결과 검증
        // response.getStatusCode(), response.getBody() 등을 검증하여 원하는 결과 확인

        //

        //given
        User user = new User(1L, "name", "email@email.com", "password", "nick");
        CustomUserDetails userDetails = CustomUserDetails.fromEntity(user);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "name", "pw");
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        System.out.println("Generated Access Token: " + accessToken);
        String lastEventId = "123";  // 이전 이벤트 ID 설정

        //when, then
        mockMvc.perform(get("/subscribe")
                        .header("X-AUTH-TOKEN", accessToken))
                .andExpect(status().isOk());
    }
}