package org.zerock.moamoa.domain.DTO.product;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T17:48:27+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductSaveRequest userSaveRequest) {
        if ( userSaveRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

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
        productResponse.user( userToUserProfileResponse( product.getUser() ) );
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

    protected UserProfileResponse userToUserProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfileResponse.UserProfileResponseBuilder userProfileResponse = UserProfileResponse.builder();

        userProfileResponse.id( user.getId() );
        userProfileResponse.nick( user.getNick() );
        userProfileResponse.profImg( user.getProfImg() );
        userProfileResponse.email( user.getEmail() );
        userProfileResponse.address( user.getAddress() );
        userProfileResponse.detailAddress( user.getDetailAddress() );

        return userProfileResponse.build();
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
