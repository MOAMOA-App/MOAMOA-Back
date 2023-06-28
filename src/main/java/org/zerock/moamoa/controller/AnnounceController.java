package org.zerock.moamoa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.service.AnnounceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AnnounceController {
	private final AnnounceService announceService;

	@PostMapping("/product/{pid}/announce")
	public AnnounceResponse saveAnnounce(@PathVariable Long pid,
		@RequestBody AnnounceRequest announce) {

		return announceService.saveAnnounce(announce, pid);
	}

	@GetMapping("/product/{pid}/announce")
	public List<AnnounceResponse> getList(@PathVariable Long pid) {
		return announceService.getByProduct(pid);
	}

	@GetMapping("/product/announce/{aid}")
	public AnnounceResponse getById(@PathVariable Long aid) {
		return announceService.findOne(aid);
	}

}
