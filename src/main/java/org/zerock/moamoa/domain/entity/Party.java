package org.zerock.moamoa.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "parties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address", nullable = false, length = 254)
    private String address;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void setProduct(Product product) {
        this.product = product;
        if (!product.getParties().contains(this)) {
            product.getParties().add(this);
        }
    }
    public void setUser(User user) {
        this.buyer = user;
        if (!user.getParties().contains(this)) {
            user.getParties().add(this);
        }
    }


}
