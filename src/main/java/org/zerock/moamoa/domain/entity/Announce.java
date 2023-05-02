package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "announce")
@ToString
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false)
    private Long productId;
    @Column(name = "announce_content", nullable = false)
    private String announceContent;


    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}