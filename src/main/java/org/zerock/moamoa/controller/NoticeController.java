package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.notice.NoticeReadUpdateRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.NoticeService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final SimpMessageSendingOperations messagingTemplate;

    /** 알림 조회 */
    // 페이지 형식으로 다 불러오게끔 함 나중에 무한스크롤하든 어쩌든 하면될듯
    @GetMapping("/{receiverid}")
    public Page<NoticeResponse> getNotices(@PathVariable Long receiverid,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "5") int pageSize) {

//        List<NoticeResponse> noticeResponses = noticeService.getReminderNotices(uid);
//        for (NoticeResponse noticeResponse : noticeResponses) {
//            User user = noticeResponse.getSenderID();
//            String senderNickname = user.getNick();    // 보내는 사람 닉네임 불러옴
//            Product product = noticeResponse.getReferenceID();
//            String referenceTitle = product.getTitle();    // 관련 공동구매의 제목 불러옴
//        }

        return noticeService.getNotices(receiverid, pageNo, pageSize);
    }


	/*	알림 줘야하는 상황
		1. Product
			: 게시글 변경/게시글 상태 변경/관심 게시글 변경/공지사항 추가 시 알림 전송
				-> 게시글이 변경되었습니다. 게시글 상태가 변경되었습니다. 관심 게시글이 변경되었습니다. 공지사항이 추가되었습니다.
	  		: 해당 이벤트 발생 시점에서 알림을 생성하고 저장
	  		: 알림 생성 시 알림의 수신자(receiverID)를 지정
	  	2. Chat
	  		: 새로운 채팅 추가 시 알림 전송 -> 새 채팅방이 생성되었습니다.
	  		: 해당 이벤트 발생 시점에서 알림을 생성하고 저장

	  	근데 생각해보니까 이거는 noticeService에서 만들고 해당 사건이 발생하는 데다 끼워넣어야 하는 거 아님...?
	 	함 조사해보기로.
	 */

    /** 알림 발신 */
    // @ApiOperation(value = "알림 구독", notes = "알림을 구독한다.")
    // text/event-stream이 SSE통신 위한 표준 MediaType
    // @AuthenticationPrincipal: 로그인 세션 정보 UserDetails에 접근할 수 있는 어노테이션
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                    String lastEventId) {
        return noticeService.subscribe(customUserDetails.getId(), lastEventId);
    }


    /** 알림 읽음 상태 변경 */
    @PutMapping("/{uid}/{noticeId}")
    public NoticeResponse updateRead(@PathVariable Long receiverId,
                                     @PathVariable Long noticeId,
                                     @ModelAttribute("profile") NoticeReadUpdateRequest NR) {
        // 알림 처리 로직 호출
        NoticeResponse response = noticeService.updateRead(NR);

        log.info("알림을 처리했습니다. Receiver ID: " + receiverId + ", Notice ID: " + noticeId);

        return response;

    }

    /** 알림 삭제 */
    @DeleteMapping("/{uid}/{noticeId}")
    public Object DeleteNotice(@PathVariable Long uid, @PathVariable Long noticeId) {
        noticeService.removeNotice(noticeId);
        return new OkResponse(SuccessMessage.NOTICE_DELETE).makeAnswer();
    }
}
