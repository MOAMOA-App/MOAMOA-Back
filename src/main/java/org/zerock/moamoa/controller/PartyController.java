package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.party.PartyRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.service.PartyService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class PartyController {
    private final PartyService partyService;

    //TODO : 전체적으로 수정 필요

    @GetMapping("/{pid}/party")
    public List<PartyResponse> getByProductList(@PathVariable Long pid) {
        return partyService.getByProduct(pid);
    }

    @PostMapping("/{pid}/party")
    public PartyResponse setPartyMember(@PathVariable Long pid, @ModelAttribute("party") PartyRequest partyRequest) {
        PartyResponse response = partyService.saveParty(partyRequest, pid);
        return response;
    }

}
