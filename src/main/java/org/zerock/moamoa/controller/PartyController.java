package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.DTO.PartyDTO;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PartyController {
    private final PartyService partyService;
    @GetMapping("/product/{pid}/party")
    public Object getList(@PathVariable Long pid, Model model){
        Map<String, List<PartyDTO>> response = new HashMap<>();
        List<PartyDTO> partyDTOList = partyService.getList();

        response.put("party", partyDTOList);
        return ResponseEntity.ok(response);
    }


}
