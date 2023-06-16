package org.zerock.moamoa.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
        @Id
        private String id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        //private File file;
        public void setProduct(Product product) {
                this.product = product;
                if (!product.getProductImages().contains(this)) {
                        product.getProductImages().add(this);
                }
        }
}