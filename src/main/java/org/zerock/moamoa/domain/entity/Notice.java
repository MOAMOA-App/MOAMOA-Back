package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.enums.NoticeType;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notices")
public class Notice extends BaseEntity {
    @Id // 기본키로 설정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 값 1씩 자동으로 증가
    private Long id;

    @ManyToOne  // 외래키 어노테이션
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;    // 보내는 회원

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;    // 받는 회원. 0622 Long->User로 타입 변경해줌

    @Column(name = "read_or_not", nullable = false) // id가 아닌 다른 칼럼에는 @Column 붙여 표현할 수 있음
    private Boolean readOrNot = false;

    @Column(name = "type", length = 20, nullable = false)
    private NoticeType type;

    @ManyToOne
    @JoinColumn(name = "reference_id", nullable = false)
    private Product reference;    // 게시글

    @Builder
    public Notice(User sender, User receiver, NoticeType type, Product reference) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.reference = reference;
    }

    public void updateRead(Boolean readOrNot) {
        this.readOrNot = readOrNot;
    }
}