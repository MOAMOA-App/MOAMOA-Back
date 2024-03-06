package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.email.EmailUserPwRequest;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmailRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getMyProfile(String email) {
        User user = userRepository.findByEmailOrThrow(email);
        return userMapper.INSTANCE.toDto(user);
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
                user.updatePw(request.getPassword(), passwordEncoder);
                user.updateNick(StringMaker.verifyEmptyNick(request.getNick()));
                user.updateCode(StringMaker.idto62Code(user.getId()));

                return userMapper.INSTANCE.toDto(user);
            } else throw new AuthException(ErrorCode.USER_EMAIL_USED);
        } else {
            user = userMapper.INSTANCE.toEntity(request);
            user.updatePw(request.getPassword(), passwordEncoder);
            user.updateNick(StringMaker.verifyEmptyNick(request.getNick()));

            User savedUser = userRepository.save(user);
            savedUser.updateCode(StringMaker.idto62Code(savedUser.getId()));

            return userMapper.INSTANCE.toDto(savedUser);
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

            return userMapper.INSTANCE.toDto(user);
        } else {
            user = userMapper.INSTANCE.toEntity(request);
            user.updateNick(StringMaker.verifyEmptyNick(request.getNick()));

            User savedUser = userRepository.save(user);
            savedUser.updateCode(StringMaker.idto62Code(user.getId()));

            return userMapper.INSTANCE.toDto(savedUser);
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
        if (!Objects.equals(request.getEmail(), username))
            throw new AuthException(ErrorCode.USER_ACCESS_REJECTED);

        User user = userRepository.findByEmailOrThrow(username);
        user.updateProfile(request);
        return ResultResponse.toDto("OK");
    }

    /**
     * 비로그인 상태에서 비밀번호 바꿔줌
     */
    @Transactional
    public ResultResponse updatePwEmail(EmailUserPwRequest req) {
        String email = emailRepository.findByTokenOrThrow(req.getToken()).getEmail();
        User user = userRepository.findByEmailOrThrow(email);
        if (user.getPassword() == null){
            return ResultResponse.toDto("소셜로그인한 회원입니다.");
        }
        if (passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return ResultResponse.toDto("SAME_PASSWORD");
        user.updatePw(req.getPassword(), passwordEncoder);

        return ResultResponse.toDto("OK");
    }

    /**
     * 로그인 상태에서 비밀번호 바꿔줌
     */
    @Transactional
    public ResultResponse updatePwLogin(UserPwChangeRequest req, String username) {
        if (!Objects.equals(req.getEmail(), username))
            throw new AuthException(ErrorCode.USER_ACCESS_REJECTED);
        if (Objects.equals(req.getOldPassword(), req.getNewPassword())){
            return ResultResponse.toDto("SAME_PW");
        }

        User user = userRepository.findByEmailOrThrow(username);
        System.out.println("User: "+user.getId() + " " + user.getEmail());
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword()))
            return ResultResponse.toDto("INCORRECT_PW");

        user.updatePw(req.getNewPassword(), passwordEncoder);

        return ResultResponse.toDto("OK");
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public ResultResponse emailVerify(UserCheckRequest userCheckRequest) {
        if (userRepository.existsByEmail(userCheckRequest.getEmail()))
            return ResultResponse.toDto("ALREADY_USED_EMAIL");
        return ResultResponse.toDto("OK");
    }

    // 아이디 중복검사로 바꿔야됨
    public String verifyRepeatedNick(String usernick) {
        Boolean nickcheck = userRepository.existsByNick(usernick);
        if (!nickcheck){
            return "OK";
        } else {
            return "이미 존재하는 닉네임입니다.";
        }
    }

//    UserResponse getUserResponse(User user){
//        return UserResponse.builder()
//                .id(user.getId())
//                .nick(user.getNick())
//                .email(user.getEmail())
//                .address(user.getAddress())
//                .detailAddress(user.getDetailAddress())
//                .profImg(user.getProfImg())
//                .uuid(String.valueOf(UUID.nameUUIDFromBytes(user.getNick().getBytes())))
//                .build();
//    }
}