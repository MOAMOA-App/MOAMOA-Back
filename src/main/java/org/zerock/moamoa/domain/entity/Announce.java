package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name= "announces")
@Data
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "is_lock", nullable = false)
    private Boolean lock;


    @Column(name = "contents", nullable = false)
    private String contents;


    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        if (!product.getAnnounces().contains(this)) {
            product.getAnnounces().add(this);
        }
    }
}