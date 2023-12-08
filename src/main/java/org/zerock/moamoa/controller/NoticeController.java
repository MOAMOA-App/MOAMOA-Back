package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.service.EmitterService;
import org.zerock.moamoa.service.NoticeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminder")
public class NoticeController {
    private final NoticeService noticeService;
    private final EmitterService emitterService;

    /** 알림 조회 */
    // 페이지 형식으로 다 불러오게끔 함 나중에 무한스크롤하든 어쩌든 하면될듯
    @GetMapping("")
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

    /** SSE 구독 */
    // @ApiOperation(value = "알림 구독", notes = "알림을 구독한다.")
    // text/event-stream이 SSE통신 위한 표준 MediaType
    // 로그인시 구독할 수 있도록 해야됨 (그래야 알림 받으니까)
    // 이전에 받지 못한 정보가 있다면 Last-Event-ID라는 헤더와 함께 날아온다 (항상오는것X)
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SseEmitter> subscribe(Authentication auth,
                                               @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        SseEmitter emitter = emitterService.subscribe(auth.getPrincipal().toString(), lastEventId);
        return ResponseEntity.ok(emitter);
    }

    /** 전체 알림 읽음 상태 변경 */
    @PutMapping("")
    public ResultResponse updateReadAll(Authentication auth) {
        return noticeService.updateReadAll(auth.getPrincipal().toString());
    }

    /** 단일 알림 읽음 상태 변경 */
    @PutMapping("/{nid}")
    public ResultResponse updateRead(Authentication auth, @PathVariable Long nid) {
        return noticeService.updateRead(auth.getPrincipal().toString(), nid);
    }

    /** 알림 삭제 */
    @DeleteMapping("/{nid}")
    public ResultResponse DeleteNotice(Authentication auth, @PathVariable Long nid) {
        return noticeService.removeNotice(auth.getPrincipal().toString(), nid);
    }
}
