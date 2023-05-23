package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.AnnounceRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AnnounceService {
    private final AnnounceRepository announceRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    public AnnounceService(AnnounceRepository announceRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.announceRepository = announceRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

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
    public Announce saveAnnounce(Announce announce, Long productId, Long buyerId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("productId not found"));
        User user = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("productId not found"));

        announce.setProduct(product);
        announce.setBuyerId(user);

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