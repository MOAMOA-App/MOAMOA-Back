package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name= "products")
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false)
    private Long seller_id;
    @Column(length = 11, nullable = false)
    private Long category_id;

    @Column(length = 254, nullable = false)
    private String selling_area;

    @Column(length = 254, nullable = false)
    private String detail_area;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 32, nullable = false)
    private String status;

    @Column(length = 200, nullable = false)
    private int sell_price;

    @Column(length = 200, nullable = false)
    private int view_count;

    @Column(length = 254, nullable = false)
    private String description; //text

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Column(name = "dead_date", nullable = false)
    private LocalDateTime deadDate;


    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;


    @Column(length = 200, nullable = false)
    private int sell_count;
    @Column(length = 200, nullable = false)
    private int max_count;
    @Column(length = 32, nullable = false)
    private String choice_send;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.deadDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

}
