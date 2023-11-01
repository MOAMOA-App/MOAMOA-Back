package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PartyUserInfoResponse {
    private Long id;
    private String address;
    private Integer count;
    private LocalDateTime createdAt;
    private UserProfileResponse buyer;

    @Builder
    public PartyUserInfoResponse(Long id, String address, Integer count,
                                 Instant createdAt, UserProfileResponse buyer) {
        this.id = id;
        this.address = address;
        this.count = count;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
        this.buyer = buyer;
    }
}
