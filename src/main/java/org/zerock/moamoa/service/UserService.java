package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.UserDTO;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Transactional
//    public UserDTO getById(Long id) {
//        return convertToDTO(findById(id));
//    }

    @Transactional
    public User findById(Long id){
        return this.userRepository.findById(id).orElse(new User());
    }

    @Transactional
    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    @Transactional
    public User saveUser(String logintype, String token,String name, String nick, String profImg, String email, String detailAddress){
        User user = new User();
        user.setLoginType(logintype);
        user.setToken(token);
        user.setName(name);
        user.setNick(nick);
        user.setProfImg(profImg);
        user.setEmail(email);
        user.setDetailAddress(detailAddress);

        return saveUser(user);
    }
    @Transactional
    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    @Transactional
    public void removeUser(Long id){
        User user = findById(id);

        this.userRepository.delete(user);
    }

    @Transactional
    public User updateUser(Long id, String name){
        User user = this.userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        user.setName(name);
        return this.userRepository.save(user);
    }

    /**
     * 회원가입할때 사용
     * @param email 입력받은 이메일
     * @param name 입력받은 이름
     * @param password 입력받은 Pw
     * @return 정보 db에 저장
     */
    // TODO(YJ): 비밀번호 해싱하는 코드 따로 빼기 (회원가입뿐만 아니라 정보수정할 때도 필요)
    @Transactional
    public User registerUser(String email, String name, String password){   // 계정 최초등록
        String encodePassword = passwordEncoder.encode(password);   // 비밀번호 해싱
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(encodePassword);
        return this.userRepository.save(user);
    }

    /**
     * 비밀번호 확인
     * @param inputPassword 입력한 비밀번호
     * @param storedPassword 저장된 비밀번호
     * @return 같은 값이면 true 반환
     */
    public boolean checkPassword(String inputPassword, String storedPassword) {
        if(!passwordEncoder.matches(inputPassword, storedPassword)){
            throw new PasswordWrongException();
        }
        return true;
//        // 입력된 비밀번호와 저장된 비밀번호를 BCrypt 알고리즘을 사용하여 비교
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches(inputPassword, storedPassword);
    }
    public class PasswordWrongException extends RuntimeException {
        PasswordWrongException(){
            super("비밀번호가 틀렸습니다.");
        }
    }

    private List<UserDTO> userDTOS(List<User> users){
        List<UserDTO> UserDTOList = new ArrayList<>();
        for (User user: users) {
            UserDTOList.add(convertToDTO(user));
        }
        return UserDTOList;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user);
    }
}