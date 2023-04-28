package org.zerock.moamoa.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name= "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //채팅방을 가리키는 외래키
    //fetch = 엔티티의 연관관계를 가져올 때 어떤 방식으로 가져올지를 설정하는 속성
    //fetchType.LAZY는 지연 로딩(실제 객체가 필요한 시점까지 데이터베이스에서 로딩을 지연시키는 방식)을 의미
    //연관된 엔티티가 많은 경우 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "read_or_not", nullable = false)
    private boolean readOrNot;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
