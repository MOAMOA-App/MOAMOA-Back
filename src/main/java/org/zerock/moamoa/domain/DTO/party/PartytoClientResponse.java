package org.zerock.moamoa.domain.DTO.party;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.ResultResponse;

@Data
public class PartytoClientResponse {
    private Long pid;
    private String message;

    public static PartytoClientResponse toDto(Long pid, String message) {
        PartytoClientResponse res = new PartytoClientResponse();
        res.pid = pid;
        res.message = message;
        return res;
    }
}
