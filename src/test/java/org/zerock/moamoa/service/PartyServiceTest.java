package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.entity.*;
import org.zerock.moamoa.repository.PartyRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class PartyServiceTest {

    @Autowired
    PartyService partyService;
    @Autowired
    PartyRepository partyRepository;

    @Test
    void findById() {
        System.out.println(partyService.findById(Long.valueOf(2)));
    }
    @Test
    void  findAll(){
        System.out.println(partyService.findAll());
    }
    @Test
    void saveParty() {
        // Mocked Product and User
        int i = 1;
        String temp = Integer.toString(1);
        Product product = new Product();

        Party party = new Party();
        product.setId(22L);

        party.setProduct(product);


        Long productId = 21L; // productId 변수 선언 및 값 할당

        party.setAddress(temp);
        party.setCount(i);
        party.setBuyerId(41L);

        partyService.saveParty(party, 22L);

//        partyService.saveParty(party, product.getId());
    }
    @Test
    void removeParty() {
        partyService.removeParty(Long.valueOf(21));
    }

    @Test
    void updateParty() {
    }
}