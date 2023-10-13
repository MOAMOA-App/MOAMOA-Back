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

    @PostMapping("{pid}/announce")
    public AnnounceResultResponse saveAnnounce(Authentication authentication,
                                               @PathVariable Long pid,
                                               @RequestBody AnnounceRequest announce) {

        return announceService.saveAnnounce(announce, pid, authentication.getPrincipal().toString());
    }

    @PutMapping("{pid}/announce")
    public AnnounceResultResponse updateAnnounce(Authentication authentication,
                                                 @PathVariable Long pid,
                                                 @RequestBody AnnounceRequest announce) {

        return announceService.updateInfo(announce, pid, authentication.getPrincipal().toString());
    }

    @DeleteMapping("{pid}/announce")
    public AnnounceResultResponse removeAnnounce(Authentication authentication,
                                                 @PathVariable Long pid,
                                                 @RequestBody AnnounceRequest announce) {

        return announceService.remove(announce, pid, authentication.getPrincipal().toString());
    }

    @GetMapping("{pid}/announce")
    public List<AnnounceResponse> getList(@PathVariable Long pid) {
        return announceService.getByProduct(pid);
    }

    @GetMapping("{pid}/announce/{aid}")
    public AnnounceResultResponse getById(@PathVariable Long aid, @PathVariable Long pid) {
        return announceService.findOne(pid, aid);
    }

}
