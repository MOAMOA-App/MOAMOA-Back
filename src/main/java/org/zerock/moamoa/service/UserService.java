package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
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

}