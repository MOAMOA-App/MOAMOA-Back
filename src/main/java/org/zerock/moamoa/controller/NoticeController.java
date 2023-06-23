package org.zerock.moamoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.NoticeDTO;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.NoticeService;
import org.zerock.moamoa.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reminder")
public class NoticeController {
    private final NoticeService noticeService;

    private final UserService userService;

    private final UserRepository userRepository;

    public NoticeController(NoticeService noticeService, UserService userService, UserRepository userRepository) {
        this.noticeService = noticeService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * 알람 조회
     * @param userId
     * @return
     */
    @GetMapping("/{id}")
    public List<NoticeDTO> getUserNotices(@PathVariable("id") Long userId) {
        User user = userRepository.findById(userId) // 사용자 조회
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        List<Notice> notices = user.getNotices();   // notices 가져옴

        return notices.stream()
                .map(NoticeDTO::new)
                .collect(Collectors.toList());  // NoticeDTO 가져와 반환
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
        // NoticeService의 saveNotices에 리시버에 알림 추가되는 코드 추가햇음!!!

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
