package org.zerock.moamoa.domain.DTO.party;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Party;

@Mapper(componentModel = "spring")
public interface PartyMapper {
	PartyMapper INSTANCE = Mappers.getMapper(PartyMapper.class);

	Party toEntity(PartyRequest partyRequest);

	PartyResponse toDto(Party party);

	PartyUserInfoResponse toUserDto(Party party);

}
