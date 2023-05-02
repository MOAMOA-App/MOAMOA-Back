package org.zerock.moamoa.domain.entity;


import lombok.Data;

import javax.persistence.*;

//product_id : N:1      유저 한명당 여러 파일
@Entity
@Data
@Table(name = "goods_image")
public class ProductImage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne
        @JoinColumn(name = "file_id", nullable = false)
        private File file;

        @ManyToOne
        @JoinColumn(name="product_id", nullable = false)
        private Product product;
    }

