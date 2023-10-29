package org.zerock.moamoa.domain.DTO.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserSignupRequest userSignupRequest); // 매개변수로 받은 UserSignupRequest 객체의 필드 값을
    // User 엔티티 객체의 필드에 매핑하여 반환

    UserLoginResponse login(UserLoginRequest userLoginRequest);  // 해당 정보를 사용하여 로그인을 처리

    // 로그인에 성공한 경우 UserResponse 반환
    UserLoginResponse toLoginResponse(Auth auth);

    UserResponse toDto(User user);  // User 엔티티 객체를 UserResponse DTO로 변환

    UserProfileResponse toProfileDto(User user);

    CustomUserDetails toDetailsDto(User user);

    UserProductResponse toProductProfile(User user);
}