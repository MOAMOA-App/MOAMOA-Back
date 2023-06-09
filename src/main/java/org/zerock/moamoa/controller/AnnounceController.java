package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.service.AnnounceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AnnounceController {
    private final AnnounceService announceService;

    @PostMapping("/product/{pid}/announce")
    public Object saveAnnounce(@PathVariable Long pid,
                               @RequestParam() String contents,
                               @RequestParam() boolean lock){
        Map<String, Long> response = new HashMap<>();
        Announce announce = new Announce();
        announce.setLock(lock);
        announce.setContents(contents);
        Long result = announceService.saveAnnounce(announce, pid);
        if(result == -1){
            return ResponseEntity.badRequest().body("Announce 저장에 실패했습니다.");
        }
        response.put("id", announceService.saveAnnounce(announce, pid));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{pid}/announce")
    public Object getList(@PathVariable Long pid, Model model){
        Map<String, List<AnnounceDTO>> response = new HashMap<>();
        List<AnnounceDTO> announceList = announceService.getByProduct(pid);

        response.put("announce", announceList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/announce/{aid}")
    public Object getById(@PathVariable Long aid) {
        AnnounceDTO announceDTO = announceService.getById(aid);

        if (announceDTO.getId() == null) {
            return ResponseEntity.noContent().build();
        }

        Map<String, AnnounceDTO> response = new HashMap<>();
        response.put("announce", announceDTO);

        return ResponseEntity.ok(response);
    }


}
