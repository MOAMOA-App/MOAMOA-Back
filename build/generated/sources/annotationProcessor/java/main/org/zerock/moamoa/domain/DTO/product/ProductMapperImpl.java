package org.zerock.moamoa.domain.DTO.product;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.DTO.UserDTO;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T19:42:00+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductSaveRequest userSaveRequest) {
        if ( userSaveRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.user( userSaveRequest.getUser() );
        product.categoryId( userSaveRequest.getCategoryId() );
        product.sellingArea( userSaveRequest.getSellingArea() );
        product.detailArea( userSaveRequest.getDetailArea() );
        product.title( userSaveRequest.getTitle() );
        product.status( userSaveRequest.getStatus() );
        product.sellPrice( userSaveRequest.getSellPrice() );
        product.viewCount( userSaveRequest.getViewCount() );
        product.description( userSaveRequest.getDescription() );
        product.sellCount( userSaveRequest.getSellCount() );
        product.maxCount( userSaveRequest.getMaxCount() );
        product.choiceSend( userSaveRequest.getChoiceSend() );
        product.countImage( userSaveRequest.getCountImage() );
        product.activate( userSaveRequest.getActivate() );
        product.finishedAt( userSaveRequest.getFinishedAt() );

        return product.build();
    }

    @Override
    public ProductResponse toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.user( userToUserDTO( product.getUser() ) );
        productResponse.categoryId( product.getCategoryId() );
        productResponse.sellingArea( product.getSellingArea() );
        productResponse.detailArea( product.getDetailArea() );
        productResponse.title( product.getTitle() );
        productResponse.status( product.getStatus() );
        productResponse.sellPrice( product.getSellPrice() );
        productResponse.viewCount( product.getViewCount() );
        productResponse.description( product.getDescription() );
        productResponse.sellCount( product.getSellCount() );
        productResponse.maxCount( product.getMaxCount() );
        productResponse.choiceSend( product.getChoiceSend() );
        productResponse.countImage( product.getCountImage() );
        productResponse.createdAt( product.getCreatedAt() );
        productResponse.finishedAt( product.getFinishedAt() );
        productResponse.updatedAt( product.getUpdatedAt() );
        productResponse.announces( announceListToAnnounceResponseList( product.getAnnounces() ) );
        List<Party> list1 = product.getParties();
        if ( list1 != null ) {
            productResponse.parties( new ArrayList<Party>( list1 ) );
        }

        return productResponse.build();
    }

    protected UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.name( user.getName() );
        userDTO.password( user.getPassword() );
        userDTO.nick( user.getNick() );
        userDTO.profImg( user.getProfImg() );
        userDTO.email( user.getEmail() );
        userDTO.address( user.getAddress() );
        userDTO.detailAddress( user.getDetailAddress() );

        return userDTO.build();
    }

    protected AnnounceResponse announceToAnnounceResponse(Announce announce) {
        if ( announce == null ) {
            return null;
        }

        AnnounceResponse.AnnounceResponseBuilder announceResponse = AnnounceResponse.builder();

        announceResponse.id( announce.getId() );
        announceResponse.lock( announce.getLock() );
        announceResponse.contents( announce.getContents() );
        announceResponse.createdAt( announce.getCreatedAt() );
        announceResponse.updatedAt( announce.getUpdatedAt() );

        return announceResponse.build();
    }

    protected List<AnnounceResponse> announceListToAnnounceResponseList(List<Announce> list) {
        if ( list == null ) {
            return null;
        }

        List<AnnounceResponse> list1 = new ArrayList<AnnounceResponse>( list.size() );
        for ( Announce announce : list ) {
            list1.add( announceToAnnounceResponse( announce ) );
        }

        return list1;
    }
}
