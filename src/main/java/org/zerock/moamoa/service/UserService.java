package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.user.RandomNick;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.EmailUserPwRequest;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmailRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;


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
                user.updateNick(request.getNick());
                return userMapper.toDto(user);
            } else throw new AuthException(ErrorCode.USER_EMAIL_USED);
        } else {
            user = userMapper.toEntity(request);
            user.updatePw(request.getPassword());
            user.hashPassword(passwordEncoder);     // 비밀번호 암호화
            user.updateNick(request.getNick());
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

            String rNick = RandomNick.printRandNick();
            while (!Objects.equals(nickVerify(rNick), "OK")){
                rNick = RandomNick.printRandNick();
            }
            user.updateNick(rNick);  // 닉네임 랜덤설정
            userRepository.save(user);
            return userMapper.toDto(userRepository.save(user));
        }
    }

    @Transactional
    public ResultResponse removeUser(String username) {
        User user = userRepository.findByEmailOrThrow(username);
        if (!user.getActivate()) return ResultResponse.toDto("ALREADY");
        user.delete();
        return ResultResponse.toDto("OK");
    }

    @Transactional
    public ResultResponse updateProfile(UserProfileUpdateRequest request, String username) {
        User user = userRepository.findByEmailOrThrow(username);

        user.updateProfile(request);
        return ResultResponse.toDto("OK");
    }

    /**
     * 비로그인 상태에서 비밀번호 바꿔줌
     */
    @Transactional
    public ResultResponse updatePwEmail(EmailUserPwRequest req) {
        // req의 토큰으로 이메일 찾아서 유저 비밀번호 바꿈
        String email = emailRepository.findByTokenOrThrow(req.getToken()).getEmail();
        User user = userRepository.findByEmailOrThrow(email);
        if (user.getPassword() == null){
            return ResultResponse.toDto("소셜로그인한 회원입니다.");
        }
        user.updatePw(req.getPassword());
        user.hashPassword(passwordEncoder);

        return ResultResponse.toDto("OK");
    }

    /**
     * 로그인 상태에서 비밀번호 바꿔줌
     */
    @Transactional
    public ResultResponse updatePwLogin(UserPwChangeRequest req, String username) {
        // 1. Authentication이랑 req 이메일 같은지 비교
        if (!Objects.equals(req.getEmail(), username)){
            throw new RuntimeException();
        }
        User user = userRepository.findByEmailOrThrow(username);

        // 2. 기존 암호 복호화해서 같은지 비교 -> 다를 시 비밀번호가 틀립니다
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword()))
            return ResultResponse.toDto("INCORRECT_PW");

        // 3. 새 비밀번호랑 기존 비밀번호랑 같은지 확인 후 받은 newPw 암호화해서 저장
        if (!Objects.equals(req.getOldPassword(), req.getNewPassword())){
            return ResultResponse.toDto("SAME_PW");
        }
        user.updatePw(req.getNewPassword());
        user.hashPassword(passwordEncoder);

        return ResultResponse.toDto("OK");
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

    public ResultResponse printRandNick() {
        String rNick = RandomNick.printRandNick();
        return ResultResponse.toDto("nick: " + rNick);
    }

    public String nickVerify(String usernick) {
        Boolean nickcheck = userRepository.existsByNick(usernick);
        if (!nickcheck){
            return "OK";
        } else {
            return "이미 존재하는 닉네임입니다.";
        }
    }
}