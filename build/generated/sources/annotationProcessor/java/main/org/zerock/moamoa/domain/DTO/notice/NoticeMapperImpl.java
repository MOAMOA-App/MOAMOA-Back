package org.zerock.moamoa.domain.DTO.notice;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.Notice;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T17:53:33+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class NoticeMapperImpl implements NoticeMapper {

    @Override
    public Notice toEntity(NoticeSaveRequest noticeSaveRequest) {
        if ( noticeSaveRequest == null ) {
            return null;
        }

        Notice.NoticeBuilder notice = Notice.builder();

        notice.senderID( mapToUser( noticeSaveRequest.getSenderID() ) );
        notice.receiverID( mapToUser( noticeSaveRequest.getReceiverID() ) );
        notice.referenceID( mapToProduct( noticeSaveRequest.getReferenceID() ) );
        notice.message( noticeSaveRequest.getMessage() );
        notice.readOrNot( noticeSaveRequest.getReadOrNot() );
        notice.type( noticeSaveRequest.getType() );

        return notice.build();
    }

    @Override
    public NoticeResponse toDto(Notice notice) {
        if ( notice == null ) {
            return null;
        }

        NoticeResponse.NoticeResponseBuilder noticeResponse = NoticeResponse.builder();

        noticeResponse.id( notice.getId() );
        noticeResponse.senderID( notice.getSenderID() );
        noticeResponse.receiverID( notice.getReceiverID() );
        noticeResponse.message( notice.getMessage() );
        noticeResponse.readOrNot( notice.getReadOrNot() );
        noticeResponse.type( notice.getType() );
        noticeResponse.referenceID( notice.getReferenceID() );
        noticeResponse.createdAt( notice.getCreatedAt() );

        return noticeResponse.build();
    }

    @Override
    public List<NoticeResponse> toDtoList(List<Notice> notices) {
        if ( notices == null ) {
            return null;
        }

        List<NoticeResponse> list = new ArrayList<NoticeResponse>( notices.size() );
        for ( Notice notice : notices ) {
            list.add( toDto( notice ) );
        }

        return list;
    }
}
