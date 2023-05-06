package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.repository.PartyRepository;

import javax.persistence.*;
import java.time.LocalDateTime;

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
        for(int i = 0 ; i < 10 ; i ++){
            String temp = Integer.toString(i);
            Party party = new Party();

            party.setProductId(Long.valueOf(i));
            party.setBuyerId(Long.valueOf(i));

            party.setAddress(temp);
            party.setCount(i);


            partyService.saveParty(party);
        }



    }

    @Test
    void removeParty() {
        partyService.removeParty(Long.valueOf(2));
    }

    @Test
    void updateParty() {
    }
}