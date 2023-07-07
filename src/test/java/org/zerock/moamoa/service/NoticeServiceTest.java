package org.zerock.moamoa.service;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void saveNotice() {
//        // noticeId, senderId, receiverId, message, type, referenceId 값을 준비 (nullable한 값들)
//        Long senderID = 1L;
//        Long receiverID = 2L;
//        String message = "Test Message";
//        String type = "Test Type";
//        Boolean readOrNot = Boolean.valueOf("false");
//        Long referenceID = 3L;
//
//        // senderId에 해당하는 User를 생성
//        User sender = new User();
//        sender.setId(senderID);
//
//        // userRepository.findById() 메소드가 호출될 때 Optional로 감싸서 리턴하도록 Mockito.when() 메소드로 설정
//        Mockito.when(userRepository.findById(senderID)).thenReturn(Optional.of(sender));
//
//        Notice notice = new Notice();
//        notice.setSenderID(sender);
//        notice.setReceiverID(receiverID);
//        notice.setMessage(message);
//        notice.setType(type);
//        notice.setReadOrNot(readOrNot);
//        notice.setReferenceID(referenceID);
//
//        // noticeRepository.findById() 메소드가 호출될 때 Optional로 감싸서 리턴하도록 Mockito.when() 메소드로 설정
//        Mockito.when(noticeRepository.save(Mockito.any(Notice.class))).thenReturn(notice);
//
//        //when
//        // noticeService.saveNotice() 메소드를 호출: 공지사항 저장
//        Notice savedNotice = noticeService.saveNotice(senderID, receiverID, message, type, referenceID);
//
//        //then
//        assertEquals(senderID, savedNotice.getSenderID().getId());
//        assertEquals(receiverID, savedNotice.getReceiverID());
//        assertEquals(message, savedNotice.getMessage());
//        assertEquals(type, savedNotice.getType());
//        assertEquals(referenceID, savedNotice.getReferenceID());
//        assertEquals(false, savedNotice.getReadOrNot());
    }

    @Test
    void findAll() {
        System.out.println(noticeService.findAll());
    }

    @Test
    void findById() {
//        Long senderID = 1L;
//        Long receiverID = 2L;
//        String message = "Test Message";
//        String type = "Test Type";
//        Boolean readOrNot = Boolean.valueOf("false");
//        Long referenceID = 3L;
//
//        User sender = new User();
//        sender.setId(senderID);
//
//        Mockito.when(userRepository.findById(senderID)).thenReturn(Optional.of(sender));
//
//        Notice notice = new Notice();
//        notice.setSenderID(sender);
//        notice.setReceiverID(receiverID);
//        notice.setMessage(message);
//        notice.setType(type);
//        notice.setReadOrNot(readOrNot);
//        notice.setReferenceID(referenceID);
//
//        Mockito.when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
//
//        Optional<Notice> foundNotice = noticeService.findById(1L);
//
//        // Assertion
//        assertTrue(foundNotice.isPresent()); // 검색 결과가 존재함을 확인
//        assertEquals(senderID, foundNotice.get().getSenderID().getId()); // 검색된 공지사항의 송신자 ID 가 예상한대로인지 확인
//        assertEquals(receiverID, foundNotice.get().getReceiverID()); // 검색된 공지사항의 수신자 ID 가 예상한대로인지 확인
//        assertEquals(message, foundNotice.get().getMessage()); // 검색된 공지사항의 메시지가 예상한대로인지 확인
//        assertEquals(type, foundNotice.get().getType()); // 검색된 공지사항의 타입이 예상한대로인지 확인
//        assertEquals(readOrNot, foundNotice.get().getReadOrNot()); // 검색된 공지사항의 읽음 여부가 예상한대로인지 확인
//        assertEquals(referenceID, foundNotice.get().getReferenceID()); // 검색된 공지사항의 참조 ID 가 예상한대로인지 확인
    }

    @Test
    void removeNotice() {
//        // given
//        Long noticeId = 1L; // noticeId 변수를 선언하여 삭제할 공지사항의 ID 지정
//
//        // when
//        noticeService.removeNotice(noticeId);   // removeNotice() 메서드를 호출해 noticeId에 해당하는 공지사항을 삭제
//
//        // then
//        // findById() 메서드를 호출하여 noticeId에 해당하는 공지사항이 삭제되었는지 확인
//        Optional<Notice> removedNotice = noticeService.findById(noticeId);
//        // removedNotice 변수에 저장된 값이 Optional.empty() 인지 확인, 공지사항이 삭제되었는지 검증
//        assertFalse(removedNotice.isPresent());
    }

    @Test
    void updateNotice() {
//        // noticeId, senderId, receiverId, message, type, referenceId 값을 준비 (nullable한 값들)
//        Long noticeId = 1L;
//        Long senderId = 2L;
//        Long receiverId = 3L;
//        String message = "Updated Message";
//        String type = "Updated Type";
//        Long referenceId = 4L;
//
//        // senderId에 해당하는 User를 생성
//        User sender = new User();
//        sender.setId(senderId);
//
//        // userRepository.findById() 메소드가 호출될 때 Optional로 감싸서 리턴하도록 Mockito.when() 메소드로 설정
//        Mockito.when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
//
//        // 존재하는 공지사항(existingNotice)을 생성하고, noticeId 값을 설정
//        Notice existingNotice = new Notice();
//        existingNotice.setSenderID(new User());
//        existingNotice.setReceiverID(1L);
//        existingNotice.setMessage("Test Message");
//        existingNotice.setType("Test Type");
//        existingNotice.setReadOrNot(false);
//        existingNotice.setReferenceID(5L);
//
//        // 이 공지사항을 반환하도록 noticeRepository.findById() 메소드가 호출될 때 Mockito.when() 메소드로 설정
//        Mockito.when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
//        Mockito.when(noticeRepository.save(Mockito.any(Notice.class))).thenReturn(existingNotice);
//
//        // Act
//        // noticeService.updateNotice() 메소드를 호출: 공지사항을 수정, 수정된 공지사항 반환
//        Notice updatedNotice = noticeService.updateNotice(noticeId, senderId, receiverId, message, type, referenceId);
//
//        // Assert
//        // 반환된 공지사항(updatedNotice)이 null이 아니고, 기대하는 값과 일치하는지 assertEquals() 메소드 사용해 확인
//        assertNotNull(updatedNotice);
//        assertEquals(senderId, updatedNotice.getSenderID().getId());
//        assertEquals(receiverId, updatedNotice.getReceiverID());
//        assertEquals(message, updatedNotice.getMessage());
//        assertEquals(type, updatedNotice.getType());
//        assertEquals(referenceId, updatedNotice.getReferenceID());
    }
}