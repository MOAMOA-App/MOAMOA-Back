package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.WishList;

@SpringBootTest
class WishListServiceTest {
    @Autowired
    WishListService wishListService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Test
    void findById() {
        WishList wishList = new WishList();
//        wishList = wishListService.findById(1L).orElseThrow(()->new IllegalArgumentException("해당 위시리스트가 없습니다. id=" + 1L));;
    }

    @Test
    void findAll() {
//        List<WishList> wishListList =  wishListService.findAll();
//        System.out.println(wishListList.size());
    }

    @Test
    void saveWish() {
//        wishListService.saveWish(31L, 11L);
    }

    @Test
    void removeWish() {
    }
}