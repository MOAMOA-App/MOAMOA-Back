package org.zerock.moamoa.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;


@Table(name= "block_users")
@Entity
@Data
public class UserBlocked extends BaseEntity {
    //DB상에서의 id명이 user_id로 되어 있어서 맞출 필요가 있음
    //id에 user_id를 그대로 넣는 경우
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "target_id")
    private User target;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
