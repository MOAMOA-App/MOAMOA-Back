package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    }

    @Test
    void findAll() {
    }

    @Test
    void saveUserBlocked() {
        userBlockedService.saveUserBlocked(2L,4L);
    }
}