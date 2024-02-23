package org.zerock.moamoa.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.fixture.ProductFixture;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.ProductStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    Product product;
    Product finishedProduct;
    User user;

    @BeforeEach
    void setUp() {
        String username = "testid@naver.com";
        user = userRepository.findByEmailOrThrow(username);
        product = productRepository.save(ProductFixture.createProduct(user));
    }

    @AfterEach
    void tearDown() {
        productRepository.delete(product);
        if (finishedProduct != null) productRepository.delete(finishedProduct);
    }

    @Test
    void findByUser() {
        //given
        Pageable itemPage = PageRequest.of(0, 30);

        //when
        Page<Product> products = productRepository.findByUser(user, itemPage);

        //then
        assertThat(products.getTotalElements()).isNotZero();
    }

    @Test
    void findByIdOrThrow_Existing() {
        // given
        Long pid = product.getId();

        // when
        Product selectedProduct = productRepository.findByIdOrThrow(pid);

        // then
        assertEquals(product.getId(), selectedProduct.getId());
    }

    @Test
    void findByIdOrThrow_NotExisting() {
        // given
        Long pid = Long.MAX_VALUE;

        // when & then
        assertThrows(EntityNotFoundException.class, () -> productRepository.findByIdOrThrow(pid));
    }

    @Test
    void findAllByStatusIsAndFinishedAtBefore() {
        //given
        finishedProduct = ProductFixture.createProduct(user);

        ProductStatus status = ProductStatus.READY;
        Instant testTime = finishedProduct.getFinishedAt().plus(Duration.ofDays(3));

        finishedProduct.updateStatus(status);
        finishedProduct = productRepository.save(finishedProduct);

        //when
        List<Product> products = productRepository.findAllByStatusIsAndFinishedAtBefore(status, testTime);

        //then
        assertThat(products.size()).isNotZero();

    }

    @Test
    public void basicCRUDTest() {
        /* CREATE */
        //given
        Product befProduct = ProductFixture.createProduct(user);

        //when
        Product savedProduct = productRepository.save(product);
        //then
        assertNotNull(savedProduct);
        assertThat(befProduct.getTitle()).isEqualTo(savedProduct.getTitle());
        assertThat(befProduct.getDescription()).isEqualTo(savedProduct.getDescription());
        assertThat(befProduct.getChoiceSend()).isEqualTo(savedProduct.getChoiceSend());
        assertThat(befProduct.getDetailArea()).isEqualTo(savedProduct.getDetailArea());
        assertThat(befProduct.getSellingArea()).isEqualTo(savedProduct.getSellingArea());
        assertThat(befProduct.getFinishedAt()).isEqualTo(savedProduct.getFinishedAt());
        assertThat(befProduct.getMaxCount()).isEqualTo(savedProduct.getMaxCount());
        assertThat(befProduct.getSellPrice()).isEqualTo(savedProduct.getSellPrice());
        assertThat(befProduct.getCategory()).isEqualTo(savedProduct.getCategory());
        assertThat(savedProduct.getCreatedAt()).isNotNull();
        assertThat(savedProduct.getStatus()).isEqualTo(ProductStatus.READY);

        /* READ */
        //given
        Long pid = savedProduct.getId();

        //when
        Product selectedProduct = productRepository.findById(pid).orElseThrow(RuntimeException::new);

        //then
        assertNotNull(savedProduct);
        assertThat(selectedProduct.getTitle()).isEqualTo(savedProduct.getTitle());
        assertThat(selectedProduct.getDescription()).isEqualTo(savedProduct.getDescription());
        assertThat(selectedProduct.getChoiceSend()).isEqualTo(savedProduct.getChoiceSend());
        assertThat(selectedProduct.getDetailArea()).isEqualTo(savedProduct.getDetailArea());
        assertThat(selectedProduct.getSellingArea()).isEqualTo(savedProduct.getSellingArea());
        assertThat(selectedProduct.getFinishedAt()).isEqualTo(savedProduct.getFinishedAt());
        assertThat(selectedProduct.getMaxCount()).isEqualTo(savedProduct.getMaxCount());
        assertThat(selectedProduct.getSellPrice()).isEqualTo(savedProduct.getSellPrice());
        assertThat(selectedProduct.getCategory()).isEqualTo(savedProduct.getCategory());
        assertThat(selectedProduct.getCreatedAt()).isNotNull();
        assertThat(selectedProduct.getStatus()).isEqualTo(ProductStatus.READY);

        /* DELETE */
        //when
        productRepository.delete(savedProduct);

        //then
        assertEquals(productRepository.findById(pid), Optional.empty());
    }

}