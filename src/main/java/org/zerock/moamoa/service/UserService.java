package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
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
    public UserResponse saveUser(UserSignupRequest userSignupRequest) throws Exception {
        if (this.isEmailExist(userSignupRequest.getEmail())) {
            throw new Exception("이미 존재하는 이메일입니다.");    // 이메일 중복 확인
        } else {
            // YJ: 이메일 인증 코드 보내는 코드 작성? (Controller에 있긴함 분리하는게 나을듯
            User user = userMapper.toEntity(userSignupRequest);
            log.info(user.getLoginType());
            user.hashPassword(passwordEncoder); // 비밀번호 암호화
            return userMapper.toDto(userRepository.save(user));
        }
    }

    public boolean removeUser(Long id) {
        User user = findById(id);
        user.delete();
        return !user.getActivate();
    }

    @Transactional
    public User updateUser(Long id, String name) {
        User user = findById(id);

        return this.userRepository.save(user);
    }

    @Transactional
    public UserResponse updateProfile(UserProfileUpdateRequest UP) {
        User temp = findById(UP.getId());
        temp.updateProfile(UP);
        return userMapper.toDto(temp);
    }

    @Transactional
    public UserResponse updatePw(UserPwUpdateRequest UPw) throws Exception {
        // 일단 유저 비밀번호 받아서 입력된 비밀번호와 맞는지 확인
        User temp = findById(UPw.getId());
        String encodePw = temp.getPassword();

        // 원래 비밀번호 뭐였는지 확인
        if (passwordEncoder.matches(UPw.getOldPw(), encodePw)) {
            // 맞을 시 새 비밀번호 해싱해서 저장
            UPw.setNewPw(passwordEncoder.encode(UPw.getOldPw())); // 비밀번호 암호화
            temp.updatePw(UPw);
            return userMapper.toDto(temp);
        } else {
            // 비밀번호 틀릴 시
            throw new Exception("비밀번호가 맞지 않습니다.");
        }
    }

    /**
     * 이메일 중복 확인
     *
     * @param email
     * @return
     */
    private boolean isEmailExist(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }
    //
    //    // myposts 확인
    //    public List<ProductResponse> getMyposts(Long uid){
    //        User user = findById(uid);
    //        List<Product> userposts = user.getMyPosts();
    //
    //
    //        List<ProductResponse> products = new ArrayList<>();
    //        for (Product product : userposts) {
    //            ProductResponse productResponse = productService.search(
    //                    product.getTitle(), null, null, null, "createdAt", "DESC", 0, 1
    //            ).getContent().get(0);
    //            products.add(productResponse);
    //        }
    //
    //        // 조회된 상품 리스트 반환
    //        return products;
    //    }
    //
    //    // myparties 확인
}