package org.zerock.moamoa.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
        @Id
        private String id;


}