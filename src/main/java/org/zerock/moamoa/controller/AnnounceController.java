package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.service.AnnounceService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AnnounceController {
    private final AnnounceService announceService;

    @PostMapping("/product/{pid}/announce")
    public Long saveAnnounce(@PathVariable Long pid,
                               @RequestBody Announce announce){

        Long result = announceService.saveAnnounce(announce, pid);
        if(result != -1){
            return result;
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/product/{pid}/announce")
    public List<AnnounceDTO> getList(@PathVariable Long pid){
        return announceService.getByProduct(pid);
    }

    @GetMapping("/product/announce/{aid}")
    public AnnounceDTO getById(@PathVariable Long aid) {
        AnnounceDTO announceDTO = announceService.getById(aid);

        if (announceDTO.getId() != null) {
            return announceDTO;
        }else throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }


}
