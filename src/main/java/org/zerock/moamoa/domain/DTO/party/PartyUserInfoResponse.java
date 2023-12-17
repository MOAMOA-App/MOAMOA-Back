package org.zerock.moamoa.domain.DTO.party;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.user.UserResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PartyUserInfoResponse {
    private Long id;
    private String address;
    private Integer count;
    private LocalDateTime createdAt;
    private UserResponse buyer;
}
