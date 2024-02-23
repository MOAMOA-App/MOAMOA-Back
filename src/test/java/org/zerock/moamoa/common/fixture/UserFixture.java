package org.zerock.moamoa.common.fixture;

import org.zerock.moamoa.domain.DTO.user.UserProductResponse;
import org.zerock.moamoa.domain.entity.User;

public class UserFixture {
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_CODE = "771D";
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "test123456";
    private static final String TEST_NICK_NAME = "닉네임";
    private static final String TEST_ADDRESS = "경기도 수원시 권선구 길동";
    private static final String TEST_DETAIL_ADDRESS = "202-1017";

    public static User createUser() {
        return User.builder()
                .nick(TEST_NICK_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .code(TEST_CODE)
                .build();
    }

    public static final User TEST_USER = User.builder()
            .nick(TEST_NICK_NAME)
            .email(TEST_EMAIL)
            .password(TEST_PASSWORD)
            .code(TEST_CODE)
            .build();

    public static final UserProductResponse TEST_USER_RESPONSE = UserProductResponse.builder()
            .nick(TEST_NICK_NAME)
            .email(TEST_EMAIL)
            .code(TEST_CODE)
            .build();
}
