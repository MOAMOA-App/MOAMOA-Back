package org.zerock.moamoa.domain.DTO.party;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.Party;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T17:48:27+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class PartyMapperImpl implements PartyMapper {

    @Override
    public Party toEntity(PartyRequest partyRequest) {
        if ( partyRequest == null ) {
            return null;
        }

        Party.PartyBuilder party = Party.builder();

        party.id( partyRequest.getId() );
        party.address( partyRequest.getAddress() );
        party.count( partyRequest.getCount() );
        party.buyer( partyRequest.getBuyer() );
        party.product( partyRequest.getProduct() );

        return party.build();
    }

    @Override
    public PartyResponse toDto(Party party) {
        if ( party == null ) {
            return null;
        }

        Party party1 = null;

        PartyResponse partyResponse = new PartyResponse( party1 );

        partyResponse.setId( party.getId() );
        partyResponse.setAddress( party.getAddress() );
        partyResponse.setCount( party.getCount() );
        partyResponse.setCreatedAt( party.getCreatedAt() );
        partyResponse.setBuyer( party.getBuyer() );
        partyResponse.setProduct( party.getProduct() );

        return partyResponse;
    }
}
