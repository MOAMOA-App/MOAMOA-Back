package org.zerock.moamoa.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Many = Board, User = One 한명의 유저는 여러개의 게시글을 쓸 수 있다.
    @JoinColumn(name="seller_id") // foreign key (userId) references User (id)
    private User user; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다. //참조 할 테이블

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "selling_area", nullable = false, length = 254)
    private String sellingArea;

    @Column(name = "detail_area", nullable = false, length = 254)
    private String detailArea;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "sell_price", nullable = false)
    private int sellPrice;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "description", nullable = false, length = 254)
    private String description;

    @Column(name = "sell_count", nullable = false)
    private int sellCount;

    @Column(name = "max_count", nullable = false)
    private int maxCount;

    @Column(name = "choice_send", nullable = false, length = 32)
    private String choiceSend;

    @Column(name = "count_image")
    private int countImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Announce> announces;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Party> parties;

    @Column(name = "activate")
    private Boolean activate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.finishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addAnnounce(Announce announce) {
        announces.add(announce);
        announce.setProduct(this);
    }

    public void removeAnnounce(Announce announce) {
        announces.remove(announce);
    }
    public void addParty(Party party) {
        parties.add(party);
        party.setProduct(this);
    }

    public void removeParty(Party party) {
        parties.remove(party);
    }
}
