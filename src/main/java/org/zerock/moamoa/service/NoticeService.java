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

        Notice notice = new Notice();
        notice.setSenderID(sender);
        notice.setReceiverID(receiverID);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReferenceID(referenceID);
        notice.setReadOrNot(false);
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
        noticeRepository.deleteById(id);
    }

    public Notice updateNotice(Long id, Long senderID, Long receiverID, String message,
                               String type, Long referenceID) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice ID"));

        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        notice.setSenderID(sender);
        notice.setReceiverID(receiverID);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReferenceID(referenceID);
        return noticeRepository.save(notice);
    }
}
