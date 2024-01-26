package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import java.util.List;

//-> "/file/product/20230806_200920_동동주 (2).JPG",
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
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    public ChatRoom(Product product, User seller, User user) {
        this.product = product;
        this.seller = seller;
        this.user = user;
    }

    public void build(Product product, User seller, User user) {
        this.product = product;
        this.seller = seller;
        this.user = user;
    }
}
