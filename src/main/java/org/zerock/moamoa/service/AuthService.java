package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.user.UserLoginRequest;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.domain.DTO.user.UserMapper;
import org.zerock.moamoa.domain.DTO.user.UserRefreshResponse;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.AuthRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Auth getByUserId(Long userId) {
        return authRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTH_NOT_FOUND));
    }

    public Auth getByRefreshToken(String refreshToken) {
        return authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND));
    }

    /**
     * 로그인
     */
    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        // CHECK USERNAME AND PASSWORD
        User user = userService.findByEmail(request.getEmail());

        if (!user.getActivate()) return new UserLoginResponse("NOT_ACTIVITY_AUTH");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.AUTH_PASSWORD_UNEQUAL);
        }

        CustomUserDetails userDetails = CustomUserDetails.fromEntity(user);
        // GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // 이미 인증이 존재하는 경우
        if (authRepository.existsByUser(user)) {
            Auth auth = getByUserId(user.getId());
            auth.updateAccessToken(accessToken);
            auth.updateRefreshToken(refreshToken);
            return new UserLoginResponse("OK", auth, user);
        }

        // IF NOT EXISTS AUTH ENTITY, SAVE AUTH ENTITY AND TOKEN
        Auth auth = new Auth();
        auth.createEntity(user, "Bearer", accessToken, refreshToken);
        auth = authRepository.save(auth);
        return new UserLoginResponse("OK", auth, user);
    }

    public UserLoginResponse OAuthToken(CustomUserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());

        if (!user.getActivate()) return new UserLoginResponse("NOT_ACTIVITY_AUTH");

        // GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // 이미 인증이 존재하는 경우
        if (authRepository.existsByUser(user)) {
            Auth auth = getByUserId(user.getId());
            auth.updateAccessToken(accessToken);
            auth.updateRefreshToken(refreshToken);
            return new UserLoginResponse("OK", auth, user);
        }

        // IF NOT EXISTS AUTH ENTITY, SAVE AUTH ENTITY AND TOKEN
        Auth auth = new Auth();
        auth.createEntity(user, "Bearer", accessToken, refreshToken);
        auth = authRepository.save(auth);
        return new UserLoginResponse("OK", auth, user);
    }

    /**
     * Token 갱신
     */
    @Transactional
    public UserRefreshResponse refreshToken(String refreshToken) {
        if (this.jwtTokenProvider.validate(refreshToken)) {
            Auth auth = getByRefreshToken(refreshToken);

            User user = auth.getUser();
            CustomUserDetails userDetails = CustomUserDetails.fromEntity(user);
            String newAccessToken = this.jwtTokenProvider.generateAccessToken(userDetails);
            auth.updateAccessToken(newAccessToken);
            return UserRefreshResponse.toDto(newAccessToken, "OK");
        }

        return UserRefreshResponse.toDto("AUTH_FAIL");
    }
}
