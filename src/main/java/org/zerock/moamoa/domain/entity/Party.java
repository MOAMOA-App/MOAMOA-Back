package org.zerock.moamoa.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= "party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "address", nullable = false, length = 254)
    private String address;

    @Column(name = "count", nullable = false)
    private int count;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
