package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

//file-users : N:1                유저 한명당 여러 파일
//my_categories-users : 1:1       유저 한명당 하나의 관심 카테고리
//categores-my_categories : N:M   카테고리당 여러 관심 카테고리, 관심 카테고리 안에 여러 카테고리
//files-goods_images : 1:1        이미지 하나당 파일 하나
//goods_images-goods : N:1        상품 하나당 이미지 여러개
@Entity
@Table(name = "files")
@Data

public class File {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User users;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "file")
    private ProductImage product_images;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
