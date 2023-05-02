package org.zerock.moamoa.domain.entity;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name= "wish_list")
@Entity
@Data
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Goods productId;

    @Column(name = "user_id",length = 11, nullable = false)
    private Long userId;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
