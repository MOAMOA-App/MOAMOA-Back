package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    public void build(Product product, User seller, User user) {
        this.productId = product;
        this.sellerId = seller;
        this.userId = user;
    }
}
