package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse findOne(Long id) {
        return userMapper.toDto(findById(id));
    }

    public UserProfileResponse getMyProfile(String email) {
        User user = findByEmail(email);
        return UserProfileResponse.builder(user);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User findById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse saveUser(UserSignupRequest request) throws Exception {
        if (isEmailExist(request.getEmail())) {
            User user = userRepository.findByEmailOrThrow(request.getEmail());
            if (user.getPassword() == null) {
                user.updatePw(request.getPassword());
                user.hashPassword(passwordEncoder); // 비밀번호 암호화
                return userMapper.toDto(user);
            } else throw new Exception("이미 존재하는 이메일입니다.");    // 이메일 중복 확인
        } else {
            User user = userMapper.toEntity(request);
            if (request.getNaver() != null && request.getKakao() != null) {
                user.hashPassword(passwordEncoder); // 비밀번호 암호화
            }
            user.randomNick();  // 닉네임 랜덤설정
            return userMapper.toDto(userRepository.save(user));
        }
    }

    public boolean removeUser(Long id) {
        User user = findById(id);
        user.delete();
        return !user.getActivate();
    }

    @Transactional
    public UserResponse updateProfile(UserProfileUpdateRequest UP) {
        User temp = findById(UP.getId());
        temp.updateProfile(UP);
        return userMapper.toDto(temp);
    }

    @Transactional
    public UserResponse updatePw(UserPwUpdateRequest request) throws Exception {
        // 일단 유저 비밀번호 받아서 입력된 비밀번호와 맞는지 확인
        User temp = findById(request.getId());
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
     * 로그인
     *
     * @param userLoginRequest
     * @return
     * @throws Exception
     */
    public UserLoginResponse login(UserLoginRequest userLoginRequest) throws Exception {

        // 이메일/비밀번호 모두 null-> 이메일을 입력해주세요.
        if (userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null)
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);

        Optional<User> userOptional = userRepository.findByEmail(userLoginRequest.getEmail());
        // 이메일 확인
        // 이메일이 db에 존재-> 비밀번호 확인으로
        if (userOptional.isPresent()) {
            String encodePw = userOptional.get().getPassword();

            // 비밀번호 확인
            if (passwordEncoder.matches(userLoginRequest.getPassword(), encodePw))  // 비밀번호 맞음
                return userMapper.login(userLoginRequest);
            else    // 비밀번호 틀림
                throw new InvalidValueException(ErrorCode.INVALID_PW_VALUE);
        }
        // 이메일이 db에 존재하지 않음-> 존재하지 않는 이메일입니다.
        else {
            throw new InvalidValueException(ErrorCode.INVALID_EMAIL_VALUE);
        }
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}