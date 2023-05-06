package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.repository.AnnounceRepository;
import org.zerock.moamoa.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AnnounceService {
    private final AnnounceRepository announceRepository;

    @Autowired
    public AnnounceService(AnnounceRepository announceRepository) {
        this.announceRepository = announceRepository;
    }



    @Transactional
    public Optional<Announce> findById(Long id){
        return this.announceRepository.findById(id);
    }

    @Transactional
    public List<Announce> findAll(){
        return this.announceRepository.findAll();
    }

    @Transactional
    public Announce saveAnnounce(Announce announce){
        return this.announceRepository.save(announce);
    }

    @Transactional
    public void removeAnnounce(Long id){
        Announce announce = announceRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.announceRepository.delete(announce);
    }

    @Transactional
    public Announce updateAnnounce(Long id, String productId){
        Announce announce = announceRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        announce.setId(id);
        return this.announceRepository.save(announce);
    }

}