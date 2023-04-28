package org.zerock.moamoa.domain.entity;


import javax.persistence.*;

@Entity
@Table(name = "goods_image")
public class GoodsImage {

        @OneToOne
        @JoinColumn(name = "file_id", nullable = false)
        private File file;

        @ManyToOne
        @JoinColumn(name="product_id", nullable = false)
        private Goods goods;
    }

