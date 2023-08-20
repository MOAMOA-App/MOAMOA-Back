package org.zerock.moamoa.common.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.service.NoticeService;
import org.zerock.moamoa.service.PartyService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeListener {
    private final NoticeService noticeService;
    private final PartyService partyService;

    @TransactionalEventListener
    @Async
    public void handleNotice(NoticeSaveRequest request) {
        // partyService에서 product의 파티 리스트 불러옴, 각각 알림 보내준다
        List<PartyResponse> partyResponseList = partyService.getByProduct(request.getReferenceID());
        partyResponseList.forEach(partyResponse -> {
            request.setReceiverID(partyResponse.getBuyer().getId());    // null값이었던 receiverID에 uid값 넣어줌
            noticeService.saveAndSend(request);
        });
    }
}
