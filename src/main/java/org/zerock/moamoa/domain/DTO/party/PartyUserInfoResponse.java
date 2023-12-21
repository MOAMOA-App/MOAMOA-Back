package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
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
    private UserResponse buyer;

    @Builder
    public PartyUserInfoResponse(Long id, String address, Integer count,
                                 Instant createdAt, UserResponse buyer) {
        this.id = id;
        this.address = address;
        this.count = count;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
        this.buyer = buyer;
    }
}
