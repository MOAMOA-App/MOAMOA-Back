package org.zerock.moamoa.domain.DTO.userblocked;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.UserBlocked;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T19:42:00+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class UserBlockedMapperImpl implements UserBlockedMapper {

    @Override
    public UserBlockedResponse toDto(UserBlocked userBlocked) {
        if ( userBlocked == null ) {
            return null;
        }

        UserBlockedResponse userBlockedResponse = new UserBlockedResponse();

        return userBlockedResponse;
    }
}
