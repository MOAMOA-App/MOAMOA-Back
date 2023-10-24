package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wishlists")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;


    @Builder
    public WishList(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}
