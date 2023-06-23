package org.zerock.moamoa.domain.DTO;

import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;

public class PartyDTO {
    private Long id;
    private String address;
    private Integer count;
    private Instant createdAt;
    private User buyer;

    public PartyDTO(Party party){
        id = party.getId();
        address = party.getAddress();
        count = party.getCount();
        createdAt = party.getCreatedAt();
        buyer = party.getBuyer();
    }
}
