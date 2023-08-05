package org.zerock.moamoa.domain.DTO.productImage;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.utils.file.dto.FileResponse;

@Component
@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    ProductImages toEntity(FileResponse response);

    FileResponse toDto(ProductImages productImages);
}
