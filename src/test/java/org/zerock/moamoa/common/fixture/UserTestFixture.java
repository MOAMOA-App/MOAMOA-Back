package org.zerock.moamoa.common.fixture;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.DTO.email.EmailUserPwRequest;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.EmailType;

public class UserTestFixture {
    public final static String NICK = "testnick";
    public final static String EMAIL = "test@example.com";
    public final static String WRONG_EMAIL = "wrongUser@email.com";
    public final static String WRONG_PASSWORD = "wrongtestpassword";
    public final static String PASSWORD = "testpassword";
    public final static String ENCODED_PASSWORD = "testencodedpassword";
    public final static String TOKEN = "testtoken";
    public final static String NEW_PASSWORD = "newtestpassword";
    public final static String CODE = "testcode";
    public final static String TYPE = "testtype";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    public static User createTestUser(Long id) {
        return User.builder()
                .id(id)
                .email(EMAIL)
                .nick(NICK)
                .password(ENCODED_PASSWORD)
                .code(StringMaker.idto62Code(id))
                .build();
    }

    public static User createTestUser(String email) {

        return User.builder()
                .email(email)
                .nick(NICK)
                .password(ENCODED_PASSWORD)
                .code("testcode")
                .build();
    }

    public static User createTestUser(Long id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .nick(NICK)
                .password(ENCODED_PASSWORD)
                .code(StringMaker.idto62Code(id))
                .build();
    }

    public static User createTestUser_JoinSNS(String email) {
        return User.builder()
                .email(email)
                .nick(NICK)
                .password(null)
                .code(null)
                .build();
    }

    public static User createTestUser_ActFalse(String email) {
        return User.builder()
                .email(email)
                .nick(NICK)
                .nick(NICK)
                .password(ENCODED_PASSWORD)
                .build();
    }

    public static UserSignupRequest createUserSignupReq(String email){
        return UserSignupRequest.builder()
                .email(email)
                .nick(NICK)
                .password(ENCODED_PASSWORD)
                .build();
    }

    public static UserProfileUpdateRequest updateUserProfile(String email) {
        return UserProfileUpdateRequest.builder()
                .email(email)
                .nick("변경된닉네임")
                .address("변경된주소")
                .detailAddress("변경된세부주소")
                .build();
    }

    public static Email createTestEmail(String email, String token) {
        return Email.builder()
                .email(email)
                .token(token)
                .type(EmailType.EMAIL_PW)
                .code(CODE)
                .authenticate(true)
                .build();
    }

    public static EmailUserPwRequest updateUserPwByEmail(String token){
        return EmailUserPwRequest.builder()
                .token(token)
                .password(NEW_PASSWORD)
                .build();
    }

    public static UserPwChangeRequest updateUserPw(String email){
        return UserPwChangeRequest.builder()
                .email(email)
                .oldPassword(PASSWORD)
                .newPassword(NEW_PASSWORD)
                .build();
    }

    public static UserResponse createUserResp(String email) {
        return UserResponse.builder()
                .email(email)
                .nick(NICK)
                .code(null)
                .profImg(null)
                .address(null)
                .detailAddress(null)
                .build();
    }

    public static UserCheckRequest createUserCheckReq(String email) {
        return UserCheckRequest.builder()
                .email(email)
                .password(PASSWORD)
                .build();
    }

    public static Auth createTestAuth(){
        return Auth.builder()
                .user(createTestUser(100L))
                .accessToken(TOKEN)
                .refreshToken(TOKEN)
                .tokenType(TYPE)
                .build();
    }

    public static Product createTestProduct(Long id, String name, User user) {
        return Product.builder()
                .user(createTestUser("test@test.com"))
                .category(Category.LIFE.getLabel())
                .sellingArea("경기도 뉴욕")
                .detailArea("선샤인시티")
                .title("testtitle")
                .sellPrice(100000)
                .description("testdescription")
                .maxCount(10000)
                .choiceSend("택배")
                .finishedAt("2024-03-31 03:00:00.000000")
                .build();
    }
}
