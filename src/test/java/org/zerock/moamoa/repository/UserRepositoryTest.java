package org.zerock.moamoa.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.common.fixture.UserTestFixture;

import static org.junit.jupiter.api.Assertions.*;
import static org.zerock.moamoa.common.fixture.UserTestFixture.EMAIL;
import static org.zerock.moamoa.common.fixture.UserTestFixture.WRONG_EMAIL;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;  // DB 트랜잭션 시작/커밋/롤백에 사용

    private TransactionStatus status;   // 현재 트랜잭션의 상태

    private User user;

    @BeforeEach
    void before() {
        // 새 트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        // 유저 데이터 생성
        user = UserTestFixture.createTestUser(1L, EMAIL);
        userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        // 시작한 트랜잭션 롤백
        transactionManager.rollback(status);
    }

    @Test
    public void findByIdOrThrow_유저존재시_유저리턴() {
        // Given

        // When
        User result = userRepository.findByIdOrThrow(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void findByIdOrThrow_유저존재하지않을시_Exception() {
        assertThrows(EntityNotFoundException.class, () -> {
            userRepository.findByIdOrThrow(999999L);
        });
    }

    @Test
    public void findByEmailOrThrow_유저존재시_유저리턴() {
        // Given

        // When
        User result = userRepository.findByEmailOrThrow(EMAIL);

        // Then
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void findByEmailOrThrow_유저존재하지않을시_false() {
        assertThrows(EntityNotFoundException.class, () -> {
            userRepository.findByEmailOrThrow(WRONG_EMAIL);
        });
    }

    @Test
    public void findByCodeOrThrow_유저존재시_유저리턴() {
        // Given

        // When
        User result = userRepository.findByCodeOrThrow(StringMaker.idto62Code(1L));

        // Then
        assertNotNull(result);
        assertEquals(user.getCode(), result.getCode());
    }

    @Test
     public void findByCodeOrThrow_유저존재하지않을시_false() {
        assertThrows(EntityNotFoundException.class, () -> {
            userRepository.findByCodeOrThrow(StringMaker.idto62Code(999999L));
        });
    }

    @Test
    public void existsByEmail_유저존재시_true(){
        Boolean result = userRepository.existsByEmail(EMAIL);
        assertEquals(result, true);
    }

    @Test
    public void existsByEmail_유저존재하지않을시_false(){
        Boolean result = userRepository.existsByEmail(WRONG_EMAIL);
        assertEquals(result, false);
    }


}