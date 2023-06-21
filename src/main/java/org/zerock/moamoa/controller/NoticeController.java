package org.zerock.moamoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.NoticeDTO;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reminder")
public class NoticeController {
    private final NoticeService noticeService;

    private final UserRepository userRepository;

    public NoticeController(NoticeService noticeService, UserRepository userRepository) {
        this.noticeService = noticeService;
        this.userRepository = userRepository;
    }

    /**
     * 알람 조회
     * @param userId
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<NoticeDTO>> getReminderNotices(@PathVariable("id") Long userId) {
        // noticeService를 통해 getReminderNotices 메서드를 호출
        // -> 해당 사용자의 알림 리스트를 받아옴
        List<Notice> reminderNotices = noticeService.getReminderNotices(userId);

        // reminderNotices 리스트를 스트림으로 변환, map() 메서드를 사용해 Notice 객체를 NoticeDTO 객체로 변환
        // collect() 메서드를 사용하여 변환된 NoticeDTO 객체들을 리스트로 수집
        List<NoticeDTO> noticeDTOs = reminderNotices.stream()
                .map(NoticeDTO::new)
                .collect(Collectors.toList());

        // 변환된 NoticeDTO 객체들을 ResponseEntity 객체에 담아 클라이언트에게 반환
        // HTTP 상태 코드: 200 OK
        return ResponseEntity.ok(noticeDTOs);
    }

    /**
     * 알림 발신
     * @param referenceID
     * @param receiverID
     * @param senderID
     * @return
     */
    @PostMapping("/{referenceID}/{receiverID}")
    public ResponseEntity<String> sendReminderNotice(@PathVariable("referenceID") Long referenceID,
                                                     @PathVariable("receiverID") Long receiverID,
                                                     @RequestParam("senderID") Long senderID) {

        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String message = "";
        String type = "";

        Notice notice = noticeService.saveNotice(sender.getId(), receiverID, message, type, referenceID);

        return ResponseEntity.ok("알림이 성공적으로 발송되었습니다.");
    }

    /**
     * 알림 읽음 상태 변경
     * @param noticeId
     * @param receiverId
     * @return
     */
    @PutMapping("/{noticeId}/{receiverId}")
    public ResponseEntity<String> updateNoticeReadState(@PathVariable("noticeId") Long noticeId,
                                                        @PathVariable("receiverId") Long receiverId) {
        noticeService.updateNoticeReadState(noticeId, receiverId);
        return ResponseEntity.ok("Notice read state updated successfully");
    }

    /**
     * 알람 삭제
     * @param noticeId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeNotice(@PathVariable("id") Long noticeId) {
        noticeService.removeNotice(noticeId);
        return ResponseEntity.ok("Notice deleted successfully");
    }
}
