package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    void findById() {
        System.out.println(userService.findById(Long.valueOf(2)));
    }
    @Test
    void  findAll(){
        System.out.println(userService.findAll());
    }

    @Test
    void saveUser() {
        for(int i = 0 ; i < 10 ; i ++){
            String temp = Integer.toString(i);
            User user = new User();
            user.setLoginType(temp);
            user.setToken(temp);
            user.setName(temp);
            user.setEmail(temp);
            user.setNick(temp);
            user.setProfImg(temp);

            userService.saveUser(user);
        }



    }

    @Test
    void removeUser() {
        userService.removeUser(Long.valueOf(2));
    }

    @Test
    void updateUser() {
    }
}