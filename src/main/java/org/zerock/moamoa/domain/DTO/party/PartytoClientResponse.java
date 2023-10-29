package org.zerock.moamoa.domain.DTO.party;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.ResultResponse;

@Data
public class PartytoClientResponse {
    private Long pid;
    private Boolean status;
    private String message;

    public static PartytoClientResponse toDto(Long pid, Boolean status, String message) {
        PartytoClientResponse res = new PartytoClientResponse();
        res.pid = pid;
        res.status = status;
        res.message = message;
        return res;
    }
}
