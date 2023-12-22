package org.zerock.moamoa.domain.DTO.party;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;

import java.util.List;

@Data
public class PartyResponse {
    private List<PartyUserInfoResponse> partylist;
    private ProductResponse product;

    @Builder
    public PartyResponse(List<PartyUserInfoResponse> buyer, ProductResponse product) {
        this.partylist = buyer;
        this.product = product;
    }
}