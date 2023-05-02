package org.zerock.moamoa.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Table(name= "chat_rooms")
@Entity
@Data
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)

    private List<ChatMessage> messages;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
