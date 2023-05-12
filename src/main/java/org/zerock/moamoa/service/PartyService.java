package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.*;
import org.zerock.moamoa.repository.PartyRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final ProductService productService;

    @Autowired
    public PartyService(PartyRepository partyRepository, ProductRepository productRepository, UserRepository userRepository, ProductService productService) {
        this.partyRepository = partyRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @Transactional
    public Optional<Party> findById(Long id){
        return this.partyRepository.findById(id);
    }

    @Transactional
    public List<Party> findAll(){
        return this.partyRepository.findAll();
    }


    @Transactional
    public Party saveParty(Party party, Long prodouctId) {
        Product product = productRepository.findById(prodouctId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        party.setProduct(product);

        return partyRepository.save(party); }

    @Transactional
    public void removeParty(Long id){
        Party party = partyRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.partyRepository.delete(party);
    }

    @Transactional
    public Party updateParty(Long id, String productId){
        Party party = partyRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        party.setId(id);
        return this.partyRepository.save(party);
    }

}