package org.zerock.moamoa.common.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.NoticeService;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.WishListService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeListener {
    private final NoticeService noticeService;
    private final PartyService partyService;
    private final WishListService wishListService;

    @TransactionalEventListener
    @Async
    public CompletableFuture<Void> handleNotice(NoticeSaveRequest req) {
        log.info("notice handler");

        // partyService에서 product의 파티 리스트 불러옴, 각각 알림 보내준다
        List<PartyResponse> partyResponseList = partyService.getByProduct(req.getReferenceID());
        List<User> wishResponseList = wishListService.findByProduct(req.getReferenceID());
        log.info("req.getReferenceID() : " + req.getReferenceID());
        log.info("notice handler : " + partyResponseList + wishResponseList);

        partyResponseList.forEach(partyResponse -> {
            req.setReceiverID(partyResponse.getBuyer().getId());    // null값이었던 receiverID에 uid값 넣어줌
            noticeService.saveAndSend(req);
        });
        wishResponseList.forEach(wishListResponse -> {
            req.setReceiverID(wishListResponse.getId());
            noticeService.saveAndSend(req);
        });

        log.info("success");
        return CompletableFuture.completedFuture(null);
    }


}
