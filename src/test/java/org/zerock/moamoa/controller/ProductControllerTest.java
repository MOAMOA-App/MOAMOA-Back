package org.zerock.moamoa.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.zerock.moamoa.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("상품 검색 테스트")
    void searchProducts() {
        //given

        //when

        //then
    }

    @Test
    void getById() {
    }

    @Test
    void getTopN() {
    }

    @Test
    void save() {
    }

    @Test
    void updateContents() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void deleteContents() {
    }
}