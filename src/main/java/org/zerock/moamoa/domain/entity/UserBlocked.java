package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "block_users")
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

    @Builder
    public UserBlocked(Long id, User user, User target, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.target = target;
        this.createdAt = createdAt;
    }
}