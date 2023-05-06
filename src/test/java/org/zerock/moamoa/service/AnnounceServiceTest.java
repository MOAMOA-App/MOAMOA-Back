package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.entity.Announce;
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
        for(int i = 0 ; i < 10 ; i ++){
            String temp = Integer.toString(i);
            Announce announce = new Announce();

            announce.setBuyerId(Long.valueOf(i));
            announce.setProductId(Long.valueOf(i));
            announce.setAnnounceContent(temp);



            announceService.saveAnnounce(announce);
        }



    }

    @Test
    void removeAnnounce() {
        announceService.removeAnnounce(Long.valueOf(2));
    }

    @Test
    void updateAnnounce() {
    }
}