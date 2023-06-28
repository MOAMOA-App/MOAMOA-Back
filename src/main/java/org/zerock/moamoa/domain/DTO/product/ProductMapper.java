package org.zerock.moamoa.domain.DTO.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	Product toEntity(ProductSaveRequest userSaveRequest);

	ProductResponse toDto(Product product);

}
