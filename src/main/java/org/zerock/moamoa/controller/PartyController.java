package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.party.*;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.PartyService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class PartyController {
    private final PartyService partyService;
    private final UserRepository userRepository;

    /* 있어야되는거
        1. 파티 유저추가
        2. 파티 유저리스트 불러오기
        3. 파티 유저삭제
        4. 파티 정보 업데이트 (주소, count)
     */

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

    @DeleteMapping("/{pid}/party")
    public ResultResponse deleteParty(@PathVariable Long pid, Authentication auth) {
        // pid랑 auth 받아서 party 찾으면 될듯
        return partyService.removeParty(auth.getPrincipal().toString(), pid);
    }

}
