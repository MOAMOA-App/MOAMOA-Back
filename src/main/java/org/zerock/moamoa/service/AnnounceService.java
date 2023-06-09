package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.repository.AnnounceRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class AnnounceService {
    private final AnnounceRepository announceRepository;
    private final ProductService productService;

    @Autowired
    public AnnounceService(AnnounceRepository announceRepository, ProductService productService) {
        this.announceRepository = announceRepository;
        this.productService = productService;
    }

    @Transactional
    public AnnounceDTO getById(Long id) {
        return convertToDTO(findById(id));
    }
    @Transactional
    public Announce findById(Long id){
        return announceRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<AnnounceDTO> getList() {
        return announceDTOS(findAll());
    }
    @Transactional
    public List<Announce> findAll(){
        return announceRepository.findAll();
    }

    public List<AnnounceDTO> getByProduct(Long pid){
        List<Announce> announceList = productService.findById(pid).getAnnounces();
        List<AnnounceDTO> announceDTOs = new ArrayList<>();
        for (Announce announce : announceList) {
            AnnounceDTO announceDTO = convertToDTO(announce);
            announceDTOs.add(announceDTO);
        }
        return announceDTOs;
    }

    public Long saveAnnounce(Announce announce, Long pid){
        Product product = productService.findById(pid);
        announce.setProduct(product);
        try {
            return announceRepository.save(announce).getId();
        }catch (Exception e) {
            String errorMessage = e.getMessage();
            // 에러 메시지를 사용하여 예외 처리 로직 수행
            return -1L;
        }
    }

    public boolean removeAnnounce(Long id){
        Announce announce = findById(id);
        if(announce.getId()!=null){
            announce.getProduct().removeAnnounce(announce);
            announceRepository.delete(announce);
            return true;
        }
        return false;
    }

    private List<AnnounceDTO> announceDTOS(List<Announce> announces){
        List<AnnounceDTO> announceDTOList = new ArrayList<>();
        for (Announce announce: announces) {
            announceDTOList.add(convertToDTO(announce));
        }
        return announceDTOList;
    }

    private AnnounceDTO convertToDTO(Announce announce) {
        return new AnnounceDTO(announce);
    }


}