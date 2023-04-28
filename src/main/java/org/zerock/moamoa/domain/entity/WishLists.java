package org.zerock.moamoa.domain.entity;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@Entity
@Table(name= "wish_lists")
@ToString
public class WishLists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false)
    private Long product_id;

    @Column(length = 11, nullable = false)
    private Long user_id;
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
