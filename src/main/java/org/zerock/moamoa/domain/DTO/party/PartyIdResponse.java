package org.zerock.moamoa.domain.DTO.party;

import lombok.Data;

@Data
public class PartyIdResponse {
    private Long pid;
    private String message;

    public static PartyIdResponse toDto(Long pid, String message) {
        PartyIdResponse res = new PartyIdResponse();
        res.pid = pid;
        res.message = message;
        return res;
    }
}
