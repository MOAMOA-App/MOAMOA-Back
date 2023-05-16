package org.zerock.moamoa.domain.entity;


import lombok.Data;

import javax.persistence.*;

//product_id : N:1      유저 한명당 여러 파일
@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
        @Id
        private Long id;

        @ManyToOne(targetEntity = File.class)
        @JoinColumn(name="file_id", nullable = false)
        private File file;
}