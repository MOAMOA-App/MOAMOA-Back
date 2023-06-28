package org.zerock.moamoa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.party.PartyRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.service.PartyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PartyController {
	private final PartyService partyService;

	@GetMapping("/product/{pid}/party")
	public List<PartyResponse> readByProductList(@PathVariable Long pid) {
		return partyService.getByProduct(pid);
	}

	@PostMapping("/product/{pid}/party")
	public PartyResponse addPartyMember(@PathVariable Long pid, @ModelAttribute("party") PartyRequest partyRequest) {
		PartyResponse response = partyService.saveParty(partyRequest, pid);
		return response;
	}

}
