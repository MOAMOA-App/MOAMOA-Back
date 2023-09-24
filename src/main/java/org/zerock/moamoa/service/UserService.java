package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;


    public UserProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmailOrThrow(email);
        return UserProfileResponse.builder(user);
    }


    /**
     * 회원가입
     */
    @Transactional
    public UserResponse saveUser(UserSignupRequest request) {
        User user;
        //이미 계정이 있는 경우
        if (isEmailExist(request.getEmail())) {
            user = userRepository.findByEmailOrThrow(request.getEmail());
            //일반 회원가입이 아닌 경우
            if (user.getPassword() == null) {
                user.updatePw(request.getPassword());
                user.hashPassword(passwordEncoder);     // 비밀번호 암호화
                return userMapper.toDto(user);
            } else throw new AuthException(ErrorCode.USER_EMAIL_USED);
        } else {
            user = userMapper.toEntity(request);
            user.updatePw(request.getPassword());
            user.hashPassword(passwordEncoder);     // 비밀번호 암호화\
            user.randomNick();  // 닉네임 랜덤설정
            return userMapper.toDto(userRepository.save(user));
        }
    }


    @Transactional
    public UserResponse oAuthSaveUser(UserSignupRequest request) {
        User user;
        log.info("oAuth 회원가입");
        //이미 계정이 있는 경우
        if (isEmailExist(request.getEmail())) {
            user = userRepository.findByEmailOrThrow(request.getEmail());

            if (request.getKakao() != null) user.setKakao(request.getKakao());
            else if (request.getNaver() != null) user.setNaver(request.getNaver());

            return userMapper.toDto(user);
        } else {

            user = userMapper.toEntity(request);
            user.randomNick();  // 닉네임 랜덤설정
            userRepository.save(user);
            return userMapper.toDto(userRepository.save(user));
        }
    }

    @Transactional
    public ResultResponse removeUser(String username) {
        if (userRepository.existsByEmail(username)) {
            User user = userRepository.findByEmailOrThrow(username);
            if (!user.getActivate()) return ResultResponse.toDto("ALREADY");
            user.delete();
            return ResultResponse.toDto("OK");
        }
        return ResultResponse.toDto("AUTH_FAIL");
    }

    @Transactional
    public UserResponse updateProfile(UserProfileUpdateRequest UP) {
        User temp = userRepository.findByIdOrThrow(UP.getId());
        temp.updateProfile(UP);
        return userMapper.toDto(temp);
    }

    @Transactional
    public UserResponse updatePw(UserPwUpdateRequest request) throws Exception {
        // 일단 유저 비밀번호 받아서 입력된 비밀번호와 맞는지 확인
        User temp = userRepository.findByIdOrThrow(request.getId());
        String encodePw = temp.getPassword();

        // 원래 비밀번호 뭐였는지 확인
        if (passwordEncoder.matches(request.getOldPw(), encodePw)) {
            // 맞을 시 새 비밀번호 해싱해서 저장
            request.setNewPw(passwordEncoder.encode(request.getOldPw())); // 비밀번호 암호화
            temp.updatePw(request.getNewPw());
            return userMapper.toDto(temp);
        } else {
            // 비밀번호 틀릴 시
            throw new InvalidValueException(ErrorCode.INVALID_PW_VALUE);
        }
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public ResultResponse emailVerify(VerifyRequest verifyRequest) {
        if (userRepository.existsByEmail(verifyRequest.getEmail()))
            return ResultResponse.toDto("ALREADY_USED_EMAIL");

        return ResultResponse.toDto("OK");
    }


    public ResultResponse passwordVerify(UserLoginRequest verifyRequest) {
        if (!userRepository.existsByEmail(verifyRequest.getEmail()))
            return ResultResponse.toDto("NOT_SIGNED_EMAIL");

        User user = userRepository.findByEmailOrThrow(verifyRequest.getEmail());

        if (passwordEncoder.matches(verifyRequest.getPassword(), user.getPassword()))
            return ResultResponse.toDto("SAME_PASSWORD");

        return ResultResponse.toDto("OK");
    }
}