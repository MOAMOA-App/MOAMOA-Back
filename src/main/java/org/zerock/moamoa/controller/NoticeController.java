package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.common.auth.CustomUserDetails;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.notice.NoticeReadUpdateRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.service.NoticeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminder")
public class NoticeController {
    private final NoticeService noticeService;

    /** 알림 조회 */
    // 페이지 형식으로 다 불러오게끔 함 나중에 무한스크롤하든 어쩌든 하면될듯
    @GetMapping("/{receiverid}")
    public Page<NoticeResponse> getNotices(Authentication auth,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "5") int pageSize) {
        return noticeService.getNotices(auth.getPrincipal().toString(), pageNo, pageSize);
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

	  	noticeService에서 만들고 해당 사건이 발생하는 곳에 끼워넣어주기.
	 */

    /** 알림 발신 */
    // @ApiOperation(value = "알림 구독", notes = "알림을 구독한다.")
    // text/event-stream이 SSE통신 위한 표준 MediaType
    // @AuthenticationPrincipal: 로그인 세션 정보 UserDetails에 접근할 수 있는 어노테이션
    // YJ: 이거 로그인안한사람한테도 보낼수잇어야댈텐데... 이거맞나  근데 뭐하러 절케하지 그냥 Authentication 넣으면 안되나
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                    String lastEventId) {
        return noticeService.subscribe(customUserDetails.getId(), lastEventId);
    }


    /** 알림 읽음 상태 변경 */
    @PutMapping("/{noticeId}")
    public NoticeResponse updateRead(Authentication auth,
                                     @PathVariable Long noticeId,
                                     @ModelAttribute("profile") NoticeReadUpdateRequest NR) {
        return noticeService.updateRead(NR);

    }

    /** 알림 삭제 */
    @DeleteMapping("/{noticeId}")
    public Object DeleteNotice(Authentication auth, @PathVariable Long noticeId) {
        noticeService.removeNotice(noticeId);
        return new OkResponse(SuccessMessage.NOTICE_DELETE).makeAnswer();
    }
}
