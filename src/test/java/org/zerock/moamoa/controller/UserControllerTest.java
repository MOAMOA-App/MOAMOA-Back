package org.zerock.moamoa.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.common.fixture.WithMockCustomUser;

@WithMockCustomUser
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUpMOckMvc(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

//    @Test
//    public void signUp_성공() throws Exception {
//
//        UserSignupRequest request = UserTestFixture.createUserSignupReq(EMAIL);
//        given(userService.saveUser(request))
//                .willReturn(UserResponse.builder()
//                        .email(EMAIL)
//                        .nick(NICK)
//                        .build());
//
//        mockMvc.perform(put("/contents/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                        .content(
//                                "{ \"email\" : \"test@example.com\", \"nick\" : \"testnick\"}"
//                        ))
//                .andExpect(status().isOk());
//
//    }
}
