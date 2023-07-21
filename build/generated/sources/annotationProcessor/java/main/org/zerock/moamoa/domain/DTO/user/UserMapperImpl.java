package org.zerock.moamoa.domain.DTO.user;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.config.security.CustomUserDetails;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T17:48:27+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserSignupRequest userSignupRequest) {
        if ( userSignupRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( userSignupRequest.getName() );
        user.email( userSignupRequest.getEmail() );
        user.password( userSignupRequest.getPassword() );

        return user.build();
    }

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        if ( userLoginRequest == null ) {
            return null;
        }

        UserLoginResponse.UserLoginResponseBuilder userLoginResponse = UserLoginResponse.builder();

        return userLoginResponse.build();
    }

    @Override
    public UserLoginResponse toLoginResponse(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        UserLoginResponse.UserLoginResponseBuilder userLoginResponse = UserLoginResponse.builder();

        return userLoginResponse.build();
    }

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.loginType( user.getLoginType() );
        userResponse.name( user.getName() );
        userResponse.email( user.getEmail() );
        userResponse.nick( user.getNick() );
        userResponse.profImg( user.getProfImg() );
        userResponse.address( user.getAddress() );
        userResponse.detailAddress( user.getDetailAddress() );
        userResponse.createdAt( user.getCreatedAt() );
        List<Party> list = user.getParties();
        if ( list != null ) {
            userResponse.parties( new ArrayList<Party>( list ) );
        }
        List<Product> list1 = user.getMyPosts();
        if ( list1 != null ) {
            userResponse.myPosts( new ArrayList<Product>( list1 ) );
        }
        List<Notice> list2 = user.getNotices();
        if ( list2 != null ) {
            userResponse.notices( new ArrayList<Notice>( list2 ) );
        }
        List<WishList> list3 = user.getWishLists();
        if ( list3 != null ) {
            userResponse.wishLists( new ArrayList<WishList>( list3 ) );
        }

        return userResponse.build();
    }

    @Override
    public CustomUserDetails toDetailsDto(User user) {
        if ( user == null ) {
            return null;
        }

        CustomUserDetails.CustomUserDetailsBuilder customUserDetails = CustomUserDetails.builder();

        customUserDetails.id( user.getId() );
        customUserDetails.password( user.getPassword() );

        return customUserDetails.build();
    }
}
