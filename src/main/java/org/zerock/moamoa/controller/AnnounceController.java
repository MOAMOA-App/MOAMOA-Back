package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.service.AnnounceService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class AnnounceController {
    private final AnnounceService announceService;

    @PostMapping("/{pid}/announce")
    public AnnounceResponse saveAnnounce(@PathVariable Long pid,
                                         @RequestBody AnnounceRequest announce) {

        return announceService.saveAnnounce(announce, pid);
    }

    @GetMapping("/{pid}/announce")
    public List<AnnounceResponse> getList(@PathVariable Long pid) {
        return announceService.getByProduct(pid);
    }

    @GetMapping("/{pid}/announce/{aid}")
    public AnnounceResponse getById(@PathVariable Long aid, @PathVariable Long pid) {
        return announceService.findOne(pid, aid);
    }

}
