package org.zerock.moamoa.common.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.PartyRepository;
import org.zerock.moamoa.repository.WishListRepository;
import org.zerock.moamoa.service.NoticeService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeListener {
    private final NoticeService noticeService;
    private final PartyRepository partyRepository;
    private final WishListRepository wishListRepository;

    @TransactionalEventListener
    @Async
    public CompletableFuture<Void> handleNotice(NoticeSaveRequest req) {
        // 채팅알람일시
        if (req.getType().getCode() == 0) {
            noticeService.saveAndSend(req);
        } else {
            List<Party> partyList = partyRepository.findByProductAndStatus(req.getReferenceID(), true);
            if (!partyList.isEmpty()){
                for (Party party : partyList) {
                    req.setReceiverID(party.getBuyer());    // null값이었던 receiverID에 uid값 넣어줌
                    noticeService.saveAndSend(req);
                }
            }
            List<WishList> wishList =  wishListRepository.findByProduct(req.getReferenceID());
            if (!wishList.isEmpty()){
                wishList.forEach(wish -> {
                    req.setReceiverID(wish.getUser());    // null값이었던 receiverID에 uid값 넣어줌
                    noticeService.saveAndSend(req);
                });
            }
        }
        return CompletableFuture.completedFuture(null);
    }

}
