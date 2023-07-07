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
@Table(name = "notice")
public class Notice extends BaseEntity {
    @Id // 기본키로 설정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 값 1씩 자동으로 증가
    private Long id;

    @ManyToOne  // 외래키 어노테이션
    @JoinColumn(name = "sender_id", nullable = false)
    private User senderID;    // 보내는 회원

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverID;    // 받는 회원. 0622 Long->User로 타입 변경해줌

    @Column(name = "message", length = 255, nullable = false)
    private String message;

    @Column(name = "read_or_not", nullable = false) // id가 아닌 다른 칼럼에는 @Column 붙여 표현할 수 있음
    private Boolean readOrNot = false;

    @Column(name = "type", length = 8, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "reference_id", nullable = false)
    private Product referenceID;    // 게시글

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Builder
    public Notice(Long id, User senderID, User receiverID, String message, Boolean readOrNot, String type,
                  Product referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = createdAt;
    }

    public void setUserNotice(User user) {
        this.receiverID = user;
        if (!user.getNotices().contains(this)) {
            user.getNotices().add(this);
        }
    }

    public void removeUserNotice() {
        if (receiverID != null) {
            User user = this.receiverID;
            user.getWishLists().remove(this);
            this.receiverID = null;
        }
    }

    public void updateRead(Boolean readOrNot) {
        this.readOrNot = readOrNot;
    }
}