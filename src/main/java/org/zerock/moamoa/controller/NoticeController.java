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
    @GetMapping("")
    public Page<NoticeResponse> getNotices(Authentication auth,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "5") int pageSize) {
        return noticeService.getNotices(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /** SSE 구독
     * text/event-stream이 SSE통신 위한 표준 MediaType
     * 이전에 받지 못한 정보가 있다면 Last-Event-ID라는 헤더와 함께 날아온다 (항상오는것X)
     * <p>
     * 알림 주는 상황
     *  1. Product: 참여게시글+관심게시글 내용 변경/상태 변경
     *  2. Chat: 새로운 채팅 추가
     *  -> 해당 이벤트 발생 시점에서 알림을 생성하고 저장, 알림 생성 시 알림의 수신자(receiverID)를 지정
     */
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
