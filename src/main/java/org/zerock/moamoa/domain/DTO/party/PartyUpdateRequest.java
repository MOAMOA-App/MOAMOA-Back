package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;

@Data
public class PartyUpdateRequest {
    private String address;
    private Integer count;

    @Builder
    public PartyUpdateRequest(String address, Integer count) {
        this.address = address;
        this.count = count;
    }
}
