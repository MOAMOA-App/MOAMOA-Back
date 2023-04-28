package org.zerock.moamoa.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "block_users")
@ToString
public class UserBlocked {
    @ManyToOne  // 외래키 어노테이션 뭐써야되는지 모르곘음... 이거맞겟지
    @JoinColumn(name="id")
    private User userID;    // 회원ID

    @ManyToOne // 진짜어케씀...
    @JoinColumn(name="id")
    private User targetID;  // 대상 회원ID

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
