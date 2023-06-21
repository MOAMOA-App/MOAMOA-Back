package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.DTO.UserDTO;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<User> findById(Long id){
        return this.userRepository.findById(id);
    }

    @Transactional
    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    @Transactional
    public User saveUser(String logintype, String token,String name, String nick, String profImg, String email, String phone, String detailAddress){
        User user = new User();
        user.setLoginType(logintype);
        user.setToken(token);
        user.setName(name);
        user.setNick(nick);
        user.setProfImg(profImg);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDetailAddress(detailAddress);

        return saveUser(user);
    }
    @Transactional
    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    @Transactional
    public void removeUser(Long id){
        User user = findById(id).orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.userRepository.delete(user);
    }

    @Transactional
    public User updateUser(Long id, String name){
        User user = this.userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        user.setName(name);
        return this.userRepository.save(user);
    }

    @Transactional
    public boolean authenticateUser(String email, String password) {     // 사용자 인증
        // 주어진 이메일로 데이터베이스에서 사용자 찾음
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return true;
                    // checkPassword(password, user.getPassword());
        }

        throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다. email=" + email);
    }

    // 비밀번호 확인 메서드
//    public boolean checkPassword(String inputPassword, String storedPassword) {
//        // 입력된 비밀번호와 저장된 비밀번호를 BCrypt 알고리즘을 사용하여 비교 (임시코드)
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches(inputPassword, storedPassword);
//    }

    //임시 주석처리
//    public List<UserDTO> getMyUserParty(Long id) {
//        List<User> userList = userRepository.getMyUserParty(id);
//
//        if (userList == null || userList.isEmpty()) {
//            // 사용자 리스트가 없는 경우 예외 처리 로직 추가
//            // 예를 들어, 사용자가 없을 때 빈 리스트를 반환하거나 에러 메시지를 처리할 수 있습니다.
//            throw new IllegalArgumentException("해당 ID에 대한 사용자 리스트가 없습니다. id=" + id);
//        }
//
//        return userDTOS(userList);
//    }

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