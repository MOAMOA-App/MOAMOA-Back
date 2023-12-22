package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.party.PartyRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.DTO.party.PartyUpdateRequest;
import org.zerock.moamoa.domain.DTO.party.PartytoClientResponse;
import org.zerock.moamoa.service.PartyService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/{pid}/party")
    public PartyResponse readByProductList(@PathVariable Long pid, Authentication auth) {
        return partyService.findUserByProduct(auth.getPrincipal().toString(), pid);
    }

    @PostMapping("/{pid}/party")
    public PartytoClientResponse addPartyMember(@PathVariable Long pid,
                                                Authentication auth,
                                                @RequestBody PartyRequest partyRequest) {
        return partyService.saveParty(auth.getPrincipal().toString(), partyRequest, pid);
    }

    @PutMapping("/{pid}/party")
    public ResultResponse updatePartyInfo(@PathVariable Long pid,
                                          Authentication auth,
                                          @RequestBody PartyUpdateRequest partyUpdateRequest) {
        return partyService.updateParty(auth.getPrincipal().toString(), pid, partyUpdateRequest);
    }

    @PutMapping("/{pid}/party/{partyid}")
    public ResultResponse updatePartyStatus(@PathVariable Long pid,
                                            @PathVariable Long partyid,
                                            Authentication auth) {
        return partyService.updatePartyStatus(auth.getPrincipal().toString(), pid, partyid);
    }

    @DeleteMapping("/{pid}/party")
    public ResultResponse deleteParty(@PathVariable Long pid, Authentication auth) {
        return partyService.removeParty(auth.getPrincipal().toString(), pid);
    }

}
