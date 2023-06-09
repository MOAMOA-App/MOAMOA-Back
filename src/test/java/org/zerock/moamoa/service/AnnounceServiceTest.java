package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.repository.AnnounceRepository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AnnounceServiceTest {

    @Autowired
    AnnounceService announceService;
    @Autowired
    AnnounceRepository announceRepository;
    @Autowired
    ProductService productService;

    @Test
    @Transactional
    void findByProduct() {
        List<AnnounceDTO> announces = announceService.getAnnounce(11L);
        for(AnnounceDTO announce : announces)
            System.out.println(announce.getId() + ":" + announce.getContents());
    }
    @Test
    @Transactional
    void findById() {
        Announce announce = announceService.findById(12L);
        System.out.println(announce.getId() + ":" + announce.getContents());
    }
    @Test
    @Transactional
    void  findAll(){
        System.out.println(announceService.findAll());
    }

    @Test
    @Transactional
    void saveAnnounce() {
        Announce announce = new Announce();

        announce.setLock(1);
        announce.setContents("");
        announceService.saveAnnounce(announce, 11L);

    }

    @Test
    void removeAnnounce() {
        announceService.removeAnnounce(11L);
    }

    @Test
    void updateAnnounce() {
    }


}