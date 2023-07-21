package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>//, NoticeInterface
 {
    List<Notice> findByReceiverID(User receiverID);

//    Map<String, SseEmitter> emitterMap = new HashMap<>();
//
//    // Notice 엔티티를 SseEmitter로 변환하는 커스텀 메서드
//    default SseEmitter convertToSseEmitter(Notice notice) {
//        SseEmitter emitter = new SseEmitter();
//
//        // SseEmitter 설정 및 초기 데이터 전송 로직을 구현
//
//        return emitter;
//    }
}
