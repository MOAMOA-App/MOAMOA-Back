package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final SimpMessageSendingOperations messagingTemplate;

    /** 알림 조회 */
    // YJ: 알림 조회할 때 목록이 프론트에 전달할 리스트에 추가되어야 함
    // 필요한게 참여한 사람 리스트에 추가 -> 아니근데이거 서비스쪽에서해야될거같은데 어쨌든 DB에 save하면 리스트랑 알림보내기 할거니까
    // 그럼 양식을 어떻게 하지? 일단 제목 필요하고 type에 따른 메시지 필요하고 pid 필요하고 그냥 response로 불러와서 거기다가
    // 제목이랑 불러오기 같이하면될듯??
    // 그럼 User에 notices 리스트 만든거 지우고 service 차원에서 list 만들어서 거따 추가하면 되려나
    // 아닌가 로그인~로그아웃까지만 유지하고 싶은데 일단 이거 어케하는지 알아보기
    // 변경: 알림 조회할 때 페이지 형식으로 그냥 다 가져옴
    // 알림은 그냥 어케... 보낼수만있게하면될듯
    // 아마 알림 보내면 백엔드에서 pub으로 보내고 프론트에서 받아서 sub 구독한 놈들한테 보내서 처리하는 형식인가본데... 몰겟어요걍...
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
    // @PostMapping("/{referenceID}/{receiverID}")
    // stomp 테스트 화면
    @GetMapping("/alarm/stomp")
    public String stompAlarm() {
        return "noticefront";
    }

    @MessageMapping("/{referenceID}/{receiverID}")
    public void sendReminderNotice(@DestinationVariable("receiverID") Long receiverID,
                                   @RequestBody NoticeSaveRequest noticeSaveReq) {
        noticeService.sendNotice(noticeSaveReq);
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
