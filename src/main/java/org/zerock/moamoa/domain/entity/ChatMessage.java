package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "message", nullable = false, length = 254)
    private String message;

    @Column(name = "read_or_not", nullable = false)
    private Boolean readOrNot;

}
