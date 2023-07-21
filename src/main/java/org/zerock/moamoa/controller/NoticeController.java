package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/reminder")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;
    private final ProductService productService;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 알림 조회
     *
     * @param uid
     * @return
     */
    // 추후 알림 클릭 시 관련된 product나 chatroom으로 가는 코드도 필요할듯
    @GetMapping("/{uid}")
    public List<NoticeResponse> getNoticesByUserId(@PathVariable("uid") Long uid) {

        List<NoticeResponse> noticeResponses = noticeService.getReminderNotices(uid);

        for (NoticeResponse noticeResponse : noticeResponses) {
            User user = noticeResponse.getSenderID();
            String senderNickname = user.getNick();    // 보내는 사람 닉네임 불러옴
            Product product = noticeResponse.getReferenceID();
            String referenceTitle = product.getTitle();    // 관련 공동구매의 제목 불러옴
        }

        return noticeResponses;
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

    /**
     * 알림 발신
     * YJ: 이거 다시 해야됨
     */
    //
//    @PostMapping("/{referenceID}/{receiverID}")
//    public ResponseEntity<String> sendReminderNotice(@PathVariable("referenceID") Long referenceID,
//                                                     @PathVariable("receiverID") Long receiverID,
//                                                     @RequestParam("senderID") Long senderID) {
//
//        User sender = userService.findById(senderID);
//
//        String message = "";
//        String type = "";
//
////		Notice notice = noticeService.saveNotice(sender.getId(), receiverID, message, type, referenceID);
//        // NoticeService의 saveNotices에 리시버에 알림 추가되는 코드 추가햇음!!! <<언제......
//
//        return ResponseEntity.ok("알림이 성공적으로 발송되었습니다.");
//    }
    // stomp 테스트 화면
    @GetMapping("/alarm/stomp")
    public String stompAlarm() {
        return "/stomp";
    }

    @PostMapping("/messagetest")
    public void sendReminderNotice(@RequestBody NoticeSaveRequest noticeSaveReq) {
        noticeService.sendNotice(noticeSaveReq);
    }


    /**
     * 알림 읽음 상태 변경
     * @param receiverId
     * @param noticeId
     * @param NR
     * @return
     */
    @PutMapping("/{uid}/{noticeId}")
    public NoticeResponse updateRead(@PathVariable Long receiverId,
                                     @PathVariable Long noticeId,
                                     @ModelAttribute("profile") NoticeReadUpdateRequest NR) {
        // 알림 처리 로직 호출
        NoticeResponse response = noticeService.updateRead(NR);

        log.info("알림을 처리했습니다. Receiver ID: " + receiverId + ", Notice ID: " + noticeId);

        return response;

    }

    /**
     * 알림 삭제
     * @param uid
     * @param noticeId
     * @return
     */
    @DeleteMapping("/{uid}/{noticeId}")
    public Object DeleteNotice(@PathVariable Long uid, @PathVariable Long noticeId) {
        noticeService.removeNotice(noticeId);
        return new OkResponse(SuccessMessage.NOTICE_DELETE).makeAnswer();
    }
}
