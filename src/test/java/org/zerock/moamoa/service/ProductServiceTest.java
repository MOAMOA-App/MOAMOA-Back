package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductRepository;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Test
    void findById() {
        System.out.println(productService.findById(Long.valueOf(2)));
    }

    @Test
    void findAll() {
        System.out.println(productService.findAll());
    }

    @Test
    void saveProduct() {
        int i = 1;
        String temp = Integer.toString(1);
        Product product = new Product();

        // Mocked User
        User user = new User();
        user.setId(41L);
        //
        // product.setUser(user);
        // product.setCategoryId(Long.valueOf(i));
        // product.setSellingArea(temp);
        // product.setDetailArea(temp);
        // product.setTitle(temp);
        // product.setStatus(temp);
        // product.setSellPrice(i);
        // product.setViewCount(i);
        // product.setDescription(temp);
        // product.setSellCount(0);
        // product.setMaxCount(i);
        // product.setChoiceSend(temp);
        //
        // productService.saveProduct(product, user.getId());
    }

//	@Test
//	void removeProduct() {
//		productService.remove(Long.valueOf(21));
//	}

    @Test
    void updateProduct() {
    }
}