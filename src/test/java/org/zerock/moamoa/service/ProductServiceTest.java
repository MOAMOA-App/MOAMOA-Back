package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.repository.ProductRepository;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    void  findAll(){
        System.out.println(productService.findAll());
    }

    @Test
    void saveProduct() {
        for(int i = 0 ; i < 10 ; i ++){
            String temp = Integer.toString(i);
            Product product = new Product();


            product.setSeller_id(Long.valueOf(i));
            product.setCategory_id(Long.valueOf(i));

            product.setSelling_area(temp);
            product.setDetail_area(temp);
            product.setTitle(temp);
            product.setStatus(temp);
            product.setSell_price(i);
            product.setView_count(i);
            product.setDescription(temp);
            product.setSell_count(0);
            product.setMax_count(i);
            product.setChoice_send(temp);




            productService.saveProduct(product);
        }



    }

    @Test
    void removeProduct() {
        productService.removeProduct(Long.valueOf(2));
    }

    @Test
    void updateProduct() {
    }
}