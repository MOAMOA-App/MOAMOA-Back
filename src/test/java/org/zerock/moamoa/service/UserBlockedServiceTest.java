package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserBlockedRepository;
import org.zerock.moamoa.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserBlockedServiceTest {
    @Autowired
    UserBlockedService userBlockedService;
    @Autowired
    UserBlockedRepository userBlockedRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    void findById() {
        userBlockedService.findById(1L);
    }

    @Test
    void findAll() {
        userBlockedService.findAll();
    }

    @Test
    void saveUserBlocked() {
        userBlockedService.saveUserBlocked(31L,32L);
    }

    @Test
    void removeUserBlocked(){
        userBlockedService.removeUserBlocked(2L);
    }
}