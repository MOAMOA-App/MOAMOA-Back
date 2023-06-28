package org.zerock.moamoa.domain.DTO.announce;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Announce;

@Mapper(componentModel = "spring")
public interface AnnounceMapper {
	AnnounceMapper INSTANCE = Mappers.getMapper(AnnounceMapper.class);

	Announce toEntity(AnnounceRequest announceRequest);

	AnnounceResponse toDto(Announce announce);
}
