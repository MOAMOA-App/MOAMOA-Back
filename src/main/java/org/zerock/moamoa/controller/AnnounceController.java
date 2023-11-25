package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResultResponse;
import org.zerock.moamoa.service.AnnounceService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class AnnounceController {
    private final AnnounceService announceService;

    /**
     * 공지 단일 출력
     */
    @GetMapping("{pid}/announce/{aid}")
    public AnnounceResultResponse getById(@PathVariable Long aid, @PathVariable Long pid) {
        return announceService.findOne(pid, aid);
    }

    /**
     * 공지 리스트
     */
    @GetMapping("{pid}/announce")
    public List<AnnounceResponse> getList(@PathVariable Long pid) {
        return announceService.getByProduct(pid);
    }

    /**
     * 공지 생성
     */
    @PostMapping("{pid}/announce")
    public AnnounceResultResponse saveAnnounce(Authentication authentication,
                                               @PathVariable Long pid,
                                               @RequestBody AnnounceRequest announce) {

        return announceService.saveAnnounce(announce, pid, authentication.getPrincipal().toString());
    }

    /**
     * 공지 수정
     */
    @PutMapping("{pid}/announce")
    public AnnounceResultResponse updateAnnounce(Authentication authentication,
                                                 @PathVariable Long pid,
                                                 @RequestBody AnnounceRequest announce) {

        return announceService.updateInfo(announce, pid, authentication.getPrincipal().toString());
    }

    /**
     * 공지 삭제
     */
    @DeleteMapping("{pid}/announce/{aid}")
    public AnnounceResultResponse removeAnnounce(Authentication authentication,
                                                 @PathVariable Long pid,
                                                 @PathVariable Long aid) {

        return announceService.remove(aid, pid, authentication.getPrincipal().toString());
    }


}
