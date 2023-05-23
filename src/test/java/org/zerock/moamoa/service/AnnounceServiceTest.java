package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.AnnounceRepository;

import javax.persistence.*;
import java.time.LocalDateTime;

@SpringBootTest
class AnnounceServiceTest {

    @Autowired
    AnnounceService announceService;
    @Autowired
    AnnounceRepository announceRepository;

    @Test
    void findById() {
        System.out.println(announceService.findById(Long.valueOf(2)));
    }
    @Test
    void  findAll(){
        System.out.println(announceService.findAll());
    }

    @Test
    void saveAnnounce() {
        // Mocked Product and User
        int i = 1;
        String temp = Integer.toString(1);
        Product product = new Product();
        User user = new User();
        Announce announce = new Announce();
        product.setId(22L);

        announce.setProduct(product);
        announce.setBuyerId(user);


        Long productId = 21L; // productId 변수 선언 및 값 할당

        announce.setAnnounceContent(temp);

        announceService.saveAnnounce(announce, 22L, 21L);

//        partyService.saveParty(party, product.getId());


    }

    @Test
    void removeAnnounce() {
        announceService.removeAnnounce(Long.valueOf(2));
    }

    @Test
    void updateAnnounce() {
    }
}