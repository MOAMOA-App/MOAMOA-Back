package org.zerock.moamoa.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.MoamoaRepository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MoamoaService {
    private final MoamoaRepository moamoaRepository;

    public List<User> getList(){
        return this.moamoaRepository.findAll();
    }

    public void create(String content){
        User user = new User();
        user.setContent(content);
        user.setCompleted(false);
        this.moamoaRepository.save(user);
    }

    @Transactional
    public void delete(Integer id){
        User user = moamoaRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.moamoaRepository.delete(user);
    }

    @Transactional
    public void update(Integer id, String content){
        User user = moamoaRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        user.setContent(content);
        this.moamoaRepository.save(user);
    }
}