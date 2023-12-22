package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Data
public class PartyRequest {
    private String address;
    private Integer count;
    private User buyer;
    private Product product;
    private Boolean status;

    @Builder
    public PartyRequest(String address, Integer count, Product product) {
        this.address = address;
        this.count = count;
        this.product = product;
        this.status = false;
    }
}
