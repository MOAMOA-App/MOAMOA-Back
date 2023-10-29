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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        // 채팅알람일시
        if (req.getType().getCode() == 4) {
            noticeService.saveAndSend(req);
        } else {
//             채팅알람 아닐시 파티랑 위시리스트 목록 다 끌어모아서 알림 보내줌
//            List<User> userList = partyService.findByProduct(req.getReferenceID());
//            List<User> wishUserList = wishListService.findByProduct(req.getReferenceID());

            List<Long> userList = new ArrayList<>(partyService.findByProductLong(req.getReferenceID()));
            List<Long> wishUserList = new ArrayList<>(wishListService.findByProductLong(req.getReferenceID()));

            userList.removeAll(wishUserList);
            userList.addAll(wishUserList);

            if (!userList.isEmpty()){
                for (Long uid : userList) {
                    req.setReceiverID(uid);    // null값이었던 receiverID에 uid값 넣어줌
                    noticeService.saveAndSend(req);
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }

}
