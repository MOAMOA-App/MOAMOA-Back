package org.zerock.moamoa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.NoticeRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;


    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    public Notice saveNotice(Long senderID, Long receiverID, String message, String type, Long referenceID) {
        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        User receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        Notice notice = new Notice();
        notice.setSenderID(sender);
        notice.setReceiverID(receiver);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReferenceID(referenceID);
        notice.setReadOrNot(false);

        receiver.addNotice(notice); // receiver의 notices에 알림이 추가되도록 함

        return noticeRepository.save(notice);
    }

    @Transactional
    public List<Notice> findAll() {
        return this.noticeRepository.findAll();
    }

    @Transactional
    public Optional<Notice> findById(Long id){
        return this.noticeRepository.findById(id);
    }

    public void removeNotice(Long id) {
//        noticeRepository.deleteById(id);  // 원래코드

        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            User receiver = notice.getReceiverID();
            if (receiver != null) {
                receiver.removeNotice(notice);  // user 엔티티의 notices에서 알림 제거
                userRepository.save(receiver);
            }
            noticeRepository.delete(notice);
        }

    }

    public Notice updateNotice(Long id, Long senderID, Long receiverID, String message,
                               String type, Long referenceID) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice ID"));

        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        User receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        notice.setSenderID(sender);
        notice.setReceiverID(receiver);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReferenceID(referenceID);
        return noticeRepository.save(notice);
    }

    // 읽을 시 상태 변경
    public void updateNoticeReadState(Long noticeId, Long receiverId) {
        Notice notice = noticeRepository.findByIdAndReceiverID(noticeId, receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice ID or receiver ID"));

        notice.setReadOrNot(true);
    }

    // 알림 조회
    public List<Notice> getReminderNotices(Long userId) {
        // 알림 줘야하는 상황
        /* 1. Product
            : 게시글 변경/게시글 상태 변경/관심 게시글 변경/공지사항 추가 시 알림 전송
            : 해당 상태 변동 이벤트가 발생하는 시점에서 알림을 생성하고 저장
            : 알림 생성 시 알림의 수신자(receiverID)를 지정
         */
        /* 2. Chat
            : 새로운 채팅 추가 시 알림 전송
            : 해당 채팅 이벤트가 발생하는 시점에서 알림을 생성하고 저장
         */

        // 나와 관련된 알림을 조회(받는 ID가 userId인 알림)해서 reminderNotices 리스트에 저장
        List<Notice> reminderNotices = noticeRepository.findReminderNoticesByReceiverID(userId);

        // 조회된 알림들을 읽은 상태로 변경해줌
        // --> 나중에 조회수 1 이상인 알림을 setReadOrNot(true) 해줘야됨!!!
        for (Notice notice : reminderNotices) {
            notice.setReadOrNot(true);
        }

        return reminderNotices;
    }
}
