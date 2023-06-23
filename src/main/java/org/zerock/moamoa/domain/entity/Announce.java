package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name= "announces")
@Data
public class Announce extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_lock", nullable = false)
    private Boolean lock;


    @Column(name = "contents", nullable = false)
    private String contents;


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