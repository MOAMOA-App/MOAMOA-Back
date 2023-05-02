package org.zerock.moamoa.domain.entity;


import javax.persistence.*;

@Entity
@Table(name = "product_images")
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


