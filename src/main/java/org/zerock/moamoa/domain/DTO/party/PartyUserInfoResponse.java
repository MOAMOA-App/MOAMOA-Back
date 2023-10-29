package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Party;

import java.time.Instant;

@Data
public class PartyUserInfoResponse {
    private Long id;
    private String address;
    private Integer count;
    private Instant createdAt;
    private UserProfileResponse buyer;

//    @Builder
//    public PartyUserInfoResponse(Long id, String address, Integer count,
//                                 Instant createdAt, UserProfileResponse buyer) {
//        this.id = id;
//        this.address = address;
//        this.count = count;
//        this.createdAt = createdAt;
//        this.buyer = buyer;
//    }

    public static PartyUserInfoResponse toDto(Party party) {
        PartyUserInfoResponse res = new PartyUserInfoResponse();
        res.id = party.getId();
        res.address = party.getAddress();
        res.count = party.getCount();
        res.createdAt = party.getCreatedAt();
        res.buyer = UserProfileResponse.builder(party.getBuyer());
        return res;
    }
}
