package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "parties")
public class Party extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address", nullable = false, length = 254)
    private String address;

    @Column(name = "count", nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public Party(Long id, String address, Integer count, User buyer, Product product) {
        this.id = id;
        this.address = address;
        this.count = count;
        this.buyer = buyer;
        this.product = product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (!product.getParties().contains(this)) {
            product.getParties().add(this);
        }
    }

}
