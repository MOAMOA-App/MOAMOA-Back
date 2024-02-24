package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.EmailUserPwRequest;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmailRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.common.fixture.UserTestFixture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.zerock.moamoa.common.fixture.UserTestFixture.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getMyProfile_유저res_리턴(){
        // Given
        User user = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // when
        UserResponse result = userService.getMyProfile(EMAIL);

        // Then
        assertEquals(UserMapper.INSTANCE.toDto(user), result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getNick(), result.getNick());
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);
    }

    @Test
    public void getMyProfile_유저_검색실패(){
        // Given
        when(userRepository.findByEmailOrThrow(anyString()))
                .thenThrow(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getMyProfile(EMAIL);
        });
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);
    }

    @Test
    public void saveUser_회원가입성공(){
        // Given
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);

        UserSignupRequest req = UserTestFixture.createUserSignupReq(EMAIL);
        User newUser = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        UserResponse result = userService.saveUser(req);

        // Then
        assertNotNull(result);
        assertEquals(result.getEmail(), newUser.getEmail());
        assertEquals(result.getNick(), newUser.getNick());

        verify(userRepository, times(1)).existsByEmail(EMAIL);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void saveUser_계정있음_일반회원가입아님(){
        // Given
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        UserSignupRequest req = UserTestFixture.createUserSignupReq(EMAIL);
        User existingUser = UserTestFixture.createTestUser_JoinSNS(EMAIL);

        when(userRepository.findByEmailOrThrow(req.getEmail())).thenReturn(existingUser);
        existingUser.updatePw(req.getPassword(), passwordEncoder);
        existingUser.updateCode(StringMaker.idto62Code(existingUser.getId()));

        // When
        userService.saveUser(req);

        // Then
        verify(userRepository, times(1)).existsByEmail(EMAIL);
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);

    }

    @Test
    public void saveUser_계정있음_회원가입실패(){
        // Given
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        UserSignupRequest req = UserTestFixture.createUserSignupReq(EMAIL);
        User existingUser = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(req.getEmail())).thenReturn(existingUser);

        // When
        // Then
        assertThrows(AuthException.class, () -> {
            userService.saveUser(req);
        });
        verify(userRepository, times(1)).existsByEmail(EMAIL);
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);

    }

    @Test
    public void oAuthSaveUser_회원가입성공(){
        // Given
        UserSignupRequest req = UserTestFixture.createUserSignupReq(EMAIL);

        User newUser = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        UserResponse result = userService.oAuthSaveUser(req);

        // Then
        assertNotNull(result);
        assertEquals(result.getEmail(), newUser.getEmail());
        assertEquals(result.getNick(), newUser.getNick());

        verify(userRepository, times(1)).existsByEmail(EMAIL);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void removeUser_회원탈퇴성공(){
        // Given
        User user = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // When
        ResultResponse response = userService.removeUser(EMAIL);

        // Then
        // 아 이거 검사가 이게맞나 YJ_TODO 이거 다시보기
        assertEquals("OK", response.getMessage());
        assertNotEquals("ALREADY", response.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));

        // Verify that user's activate flag is set to false and deletedAt is set to current time
//        assertFalse(user.getActivate());
//        assertNotNull(user.getDeletedAt());
    }

    @Test
    public void removeUser_이미탈퇴한유저(){
        // Given
        User user = UserTestFixture.createTestUser(EMAIL);
        user.delete();
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // When
        ResultResponse response = userService.removeUser(EMAIL);

        // Then
        assertNotEquals("OK", response.getMessage());
        assertEquals("ALREADY", response.getMessage());

    }

    @Test
    public void removeUser_유저검색실패(){
        // Given
        when(userRepository.findByEmailOrThrow(anyString()))
                .thenThrow(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.removeUser(EMAIL);
        });
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);
    }

    @Test
    public void updateProfile_유저정보업데이트성공(){
        // Given
        UserProfileUpdateRequest req = UserTestFixture.updateUserProfile(EMAIL);
        User user = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // When
        ResultResponse result = userService.updateProfile(req, EMAIL);

        // Then
        assertEquals("OK", result.getMessage());
        // 업데이트된 유저랑 비교... 같은것도되려나 모르겠다

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));

    }

    @Test
    public void updateProfile_유저검색실패(){
        // Given
        UserProfileUpdateRequest req = UserTestFixture.updateUserProfile(EMAIL);
        when(userRepository.findByEmailOrThrow(anyString()))
                .thenThrow(new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateProfile(req, EMAIL);
        });
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);
    }

    @Test
    public void updateProfile_정보수정접근권한없음(){
        // Given
        UserProfileUpdateRequest req = UserTestFixture.updateUserProfile(EMAIL);

        // When
        // Then
        assertThrows(AuthException.class, () -> {
            userService.updateProfile(req, UserTestFixture.WRONG_EMAIL);
        });
    }

    @Test
    public void updatePwEmail_비로그인_비밀번호변경성공(){
        // Given
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, NEW_PASSWORD);
        User user = UserTestFixture.createTestUser(EMAIL);
        Email email = UserTestFixture.createTestEmail(EMAIL, TOKEN);

        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);
        when(emailRepository.findByTokenOrThrow(TOKEN)).thenReturn(email);

        // When
        ResultResponse result = userService.updatePwEmail(request);

        // Then
        assertEquals("OK", result.getMessage());
        assertNotEquals("소셜로그인한 회원입니다.", result.getMessage());
        assertNotEquals("SAME_PASSWORD", result.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));
        verify(emailRepository, times(1)).findByTokenOrThrow(eq(TOKEN));

    }

    @Test
    public void updatePwEmail_변경실패_같은비밀번호(){
        // Given
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, PASSWORD);
        User user = UserTestFixture.createTestUser(EMAIL);

        Email email = UserTestFixture.createTestEmail(EMAIL, TOKEN);

        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);
        when(emailRepository.findByTokenOrThrow(TOKEN)).thenReturn(email);

        // 암호화 설정이 잘 안돼서 임시로 막음
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        // When
        ResultResponse result = userService.updatePwEmail(request);

        // Then
        assertNotEquals("OK", result.getMessage());
        // 이거 비밀번호 encode된거 비교 확인하기!!!!
        assertNotEquals("소셜로그인한 회원입니다.", result.getMessage());
        assertEquals("SAME_PASSWORD", result.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));
        verify(emailRepository, times(1)).findByTokenOrThrow(eq(TOKEN));
    }

    @Test
    public void updatePwEmail_변경실패_소셜로그인회원(){
        // Given
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, PASSWORD);
        User user = UserTestFixture.createTestUser_JoinSNS(EMAIL);
        user.updatePw(PASSWORD, passwordEncoder);
        Email email = UserTestFixture.createTestEmail(EMAIL, TOKEN);

        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);
        when(emailRepository.findByTokenOrThrow(TOKEN)).thenReturn(email);

        // When
        ResultResponse result = userService.updatePwEmail(request);

        // Then
        assertNotEquals("OK", result.getMessage());
        // 이거 비밀번호 encode된거 비교 확인하기!!!!
        assertEquals("소셜로그인한 회원입니다.", result.getMessage());
        assertNotEquals("SAME_PASSWORD", result.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));
        verify(emailRepository, times(1)).findByTokenOrThrow(eq(TOKEN));
    }

    @Test
    public void updatePwEmail_변경실패_이메일검색실패(){
        // Given
        EmailUserPwRequest request = new EmailUserPwRequest(TOKEN, PASSWORD);
        when(emailRepository.findByTokenOrThrow(anyString()))
                .thenThrow((new EntityNotFoundException(ErrorCode.INVALID_EMAIL_VALUE)));

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.updatePwEmail(request);
        });
        verify(emailRepository, times(1)).findByTokenOrThrow(TOKEN);
    }

    @Test
    public void updatePwLogin_로그인_비밀번호변경성공(){
        // Given
        UserPwChangeRequest request = new UserPwChangeRequest(EMAIL, PASSWORD, NEW_PASSWORD);
        User user = UserTestFixture.createTestUser(EMAIL);
        user.updatePw(user.getPassword(), passwordEncoder);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // 암호화 설정이 잘 안돼서 임시로 막아놓음...
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);

        // When
        ResultResponse result = userService.updatePwLogin(request, EMAIL);

        // Then
        assertEquals("OK", result.getMessage());
        assertNotEquals("INCORRECT_PW", result.getMessage());
        assertNotEquals("SAME_PW", result.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));
    }

    @Test
    public void updatePwLogin_변경실패_같은비밀번호(){
        // Given
        UserPwChangeRequest request = new UserPwChangeRequest(EMAIL, PASSWORD, PASSWORD);

        // When
        ResultResponse result = userService.updatePwLogin(request, EMAIL);

        // Then
        assertNotEquals("OK", result.getMessage());
        assertNotEquals("INCORRECT_PW", result.getMessage());
        assertEquals("SAME_PW", result.getMessage());
    }

    @Test
    public void updatePwLogin_변경실패_비밀번호틀림(){
        // Given
        UserPwChangeRequest request = new UserPwChangeRequest(EMAIL, WRONG_PASSWORD, NEW_PASSWORD);
        User user = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // When
        ResultResponse result = userService.updatePwLogin(request, EMAIL);

        // Then
        assertNotEquals("OK", result.getMessage());
        assertEquals("INCORRECT_PW", result.getMessage());
        assertNotEquals("SAME_PW", result.getMessage());

        verify(userRepository, times(1)).findByEmailOrThrow(eq(EMAIL));
    }

    @Test
    public void updatePwLogin_유저검색실패(){
        // Given
        UserPwChangeRequest request = new UserPwChangeRequest(EMAIL, PASSWORD, NEW_PASSWORD);
        when(userRepository.findByEmailOrThrow(anyString()))
                .thenThrow((new EntityNotFoundException(ErrorCode.USER_NOT_FOUND)));

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> userService.updatePwLogin(request, EMAIL));
        verify(userRepository, times(1)).findByEmailOrThrow(EMAIL);

    }

    @Test
    public void updatePwLogin_변경실패_접근권한없음(){
        // Given
        UserPwChangeRequest request = new UserPwChangeRequest(EMAIL, PASSWORD, NEW_PASSWORD);
        User user = UserTestFixture.createTestUser(EMAIL);
        when(userRepository.findByEmailOrThrow(EMAIL)).thenReturn(user);

        // When
        // Then
        assertThrows(AuthException.class, () -> userService.updatePwLogin(request, WRONG_EMAIL));
    }
}