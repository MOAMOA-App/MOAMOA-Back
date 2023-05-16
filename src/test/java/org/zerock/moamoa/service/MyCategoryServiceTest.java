package org.zerock.moamoa.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.Category;
import org.zerock.moamoa.domain.entity.MyCategory;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.CategoryRepository;
import org.zerock.moamoa.repository.MyCategoryRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
class MyCategoryServiceTest {
    @Autowired
    private MyCategoryService myCategoryService;

    @Autowired
    private MyCategoryRepository myCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Clock clock;

    @BeforeEach
    void setUp() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 5, 16, 10, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        this.clock = Clock.fixed(fixedDateTime.atZone(zoneId).toInstant(), zoneId);
    }

    @AfterEach
    void tearDown() {
        myCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testSaveMyCategory() {
        // 가짜 시간으로 설정된 Clock 사용
        LocalDateTime fakeDateTime = LocalDateTime.now(clock);

        // 테스트에 필요한 데이터 생성
        Category category1 = new Category();
        category1.setName("Category 1");
        category1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Category 2");
        category2 = categoryRepository.save(category2);

        User user = new User();
        user.setLoginType("Type");
        user.setToken("Token");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setNick("john");
        user.setProfImg("profile.jpg");
        user.setJoinDate(fakeDateTime); // 가짜 시간으로 설정
        user = userRepository.save(user);

        List<Category> categories = Arrays.asList(category1, category2);

        // MyCategory 저장
        myCategoryService.saveMyCategory(categories, user);

        // 저장된 MyCategory 확인
        List<MyCategory> savedMyCategories = myCategoryRepository.findAll();
        assertEquals(2, savedMyCategories.size());

        MyCategory myCategory1 = savedMyCategories.get(0);
        MyCategory myCategory2 = savedMyCategories.get(1);

        assertEquals(user, myCategory1.getUsers());
        assertEquals(user, myCategory2.getUsers());
        assertEquals(category1, myCategory1.getCategories());
        assertEquals(category2, myCategory2.getCategories());

        // 예상값과 실제값 사이의 초 단위 차이 허용
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getJoinDate());

        // joinDate 값을 확인
        assertEquals(fakeDateTime, savedUser.getJoinDate());
    }

//    @Test
//    void findAllMyCategory() {// given
//        User user = new User();
//        user.setLoginType("test");
//        user.setToken("test");
//        user.setName("test");
//        user.setEmail("test@test.com");
//        user.setNick("test");
//        user.setProfImg("test");
//        userRepository.save(user);
//
//        System.out.println(myCategoryService.findAll(user));
//    }
}