package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wish_lists")
public class WishList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Builder
    public WishList(Long id, Product product, User user, Instant createdAt) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void addUserWish(User user) {
        this.user = user;
        if (!user.getWishLists().contains(this)) {
            user.getWishLists().add(this);
        }
    }

    public void removeUserWish() {
        if (user != null) {
            User user = this.user;
            user.getWishLists().remove(this);
            this.user = null;
        }
    }
}
