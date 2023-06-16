package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.DTO.PartyDTO;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.*;
import org.zerock.moamoa.repository.PartyRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProductService productService;

    @Autowired
    public PartyService(PartyRepository partyRepository, ProductService productService) {
        this.partyRepository = partyRepository;
        this.productService = productService;
    }

    @Transactional
    public PartyDTO getById(Long id) {
        return convertToDTO(findById(id));
    }

    @Transactional
    public List<PartyDTO> getList() {
        return partyDTOS(findAll());
    }

    @Transactional
    public Party findById(Long id) {
        return partyRepository.findById(id).orElse(new Party());
    }

    @Transactional
    public List<Party> findAll(){
        return this.partyRepository.findAll();
    }

    public List<PartyDTO> getByProduct(Long pid){
        List<Party> parties = productService.findById(pid).getParties();
        List<PartyDTO> partyDTOS = new ArrayList<>();
        for (Party party : parties) {
            PartyDTO partyDTO = convertToDTO(party);
            partyDTOS.add(partyDTO);
        }
        return partyDTOS;
    }



    @Transactional
    public Party saveParty(Party party, Long pid) {
        Product product = productService.findById(pid);
        party.setProduct(product);
        return partyRepository.save(party);
    }

    @Transactional
    public boolean removeParty(Long id){
        Party party = findById(id);
        if(party.getId() != null){
            party.getBuyer().removeParty(party);
            party.getProduct().removeParty(party);
            partyRepository.delete(party);
            return true;
        }
        return false;
    }

    @Transactional
    public Party updateParty(Party party){
        Party temp = findById(party.getId());
        temp.setAddress(party.getAddress());
        temp.setCount(party.getCount());
        return this.partyRepository.save(temp);
    }

    public List<PartyDTO> getByBuyer(User buyer) {
        return partyDTOS(partyRepository.findByBuyer(buyer));
    }

    private List<PartyDTO> partyDTOS(List<Party> parties){
        List<PartyDTO> partyDTOList = new ArrayList<>();
        for (Party party: parties) {
            partyDTOList.add(convertToDTO(party));
        }
        return partyDTOList;
    }

    private PartyDTO convertToDTO(Party party) {
        return new PartyDTO(party);
    }
}