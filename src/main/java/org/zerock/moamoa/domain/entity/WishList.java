package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name= "wish_lists")
@Entity
@Data
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id",nullable = false)
    private Product productId;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void setUserId(User user) {
        this.userId = user;
        user.getWishLists().add(this);
    }

    public void removeUserId() {
        if (userId != null) {
            userId.getWishLists().remove(this);
            userId = null;
        }
    }
}
