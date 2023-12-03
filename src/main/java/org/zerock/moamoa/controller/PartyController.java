package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.party.*;
import org.zerock.moamoa.service.PartyService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/{pid}/party")
    public List<PartyUserInfoResponse> readByProductList(@PathVariable Long pid, Authentication auth) {
        // product 기준으로 참여한 유저들 보는 건 seller가 할 수 있음
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
        // pid랑 auth 받아서 party 찾으면 될듯
        return partyService.removeParty(auth.getPrincipal().toString(), pid);
    }

}
