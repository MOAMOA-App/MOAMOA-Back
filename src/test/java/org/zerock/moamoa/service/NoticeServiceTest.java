package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.NoticeRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @MockBean
    private NoticeRepository noticeRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void saveNotice() {
        Long senderID = 1L;
        Long receiverID = 2L;
        String message = "Test Message";
        String type = "Test Type";
        Boolean readOrNot = Boolean.valueOf("false");
        Long referenceID = 3L;

        User sender = new User();
        sender.setId(senderID);

        Mockito.when(userRepository.findById(senderID)).thenReturn(Optional.of(sender));

        Notice notice = new Notice();
        notice.setSenderID(sender);
        notice.setReceiverID(receiverID);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReadOrNot(readOrNot);
        notice.setReferenceID(referenceID);

        Mockito.when(noticeRepository.save(Mockito.any(Notice.class))).thenReturn(notice);

        //when
        Notice savedNotice = noticeService.saveNotice(senderID, receiverID, message, type, referenceID);

        //then
        assertEquals(senderID, savedNotice.getSenderID().getId());
        assertEquals(receiverID, savedNotice.getReceiverID());
        assertEquals(message, savedNotice.getMessage());
        assertEquals(type, savedNotice.getType());
        assertEquals(referenceID, savedNotice.getReferenceID());
        assertEquals(false, savedNotice.getReadOrNot());
    }

    @Test
    void findAll() {
        Long senderID = 1L;
        Long receiverID = 2L;
        String message = "Test Message";
        String type = "Test Type";
        Boolean readOrNot = Boolean.valueOf("false");
        Long referenceID = 3L;

        User sender = new User();
        sender.setId(senderID);

        Mockito.when(userRepository.findById(senderID)).thenReturn(Optional.of(sender));

        Notice notice = new Notice();
        notice.setSenderID(sender);
        notice.setReceiverID(receiverID);
        notice.setMessage(message);
        notice.setType(type);
        notice.setReadOrNot(readOrNot);
        notice.setReferenceID(referenceID);

        Mockito.when(noticeRepository.save(Mockito.any(Notice.class))).thenReturn(notice);

        List<Notice> foundNotices = noticeService.findAll();
    }

    @Test
    void findById() {
    }

    @Test
    void removeNotice() {
    }

    @Test
    void updateNotice() {
    }
}