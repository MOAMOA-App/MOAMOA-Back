package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "announces")
public class Announce extends BaseEntity {
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

    public void updateInfo(AnnounceRequest announce) {
        this.lock = announce.getLock();
        if (!announce.getContents().isEmpty()) this.contents = announce.getContents();
    }

    public void setProduct(Product product) {
        this.product = product;
        if (!product.getAnnounces().contains(this)) {
            product.getAnnounces().add(this);
        }
    }


    @Builder
    public Announce(Long id, Boolean lock, String contents, Product product) {
        this.id = id;
        this.lock = lock;
        this.contents = contents;
        this.product = product;
    }
}