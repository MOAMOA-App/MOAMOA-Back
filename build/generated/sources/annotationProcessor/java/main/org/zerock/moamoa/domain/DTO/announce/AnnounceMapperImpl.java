package org.zerock.moamoa.domain.DTO.announce;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.Announce;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T19:42:00+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class AnnounceMapperImpl implements AnnounceMapper {

    @Override
    public Announce toEntity(AnnounceRequest announceRequest) {
        if ( announceRequest == null ) {
            return null;
        }

        Announce.AnnounceBuilder announce = Announce.builder();

        announce.id( announceRequest.getId() );
        announce.lock( announceRequest.getLock() );
        announce.contents( announceRequest.getContents() );

        return announce.build();
    }

    @Override
    public AnnounceResponse toDto(Announce announce) {
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
}
