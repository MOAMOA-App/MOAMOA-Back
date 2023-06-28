package org.zerock.moamoa.domain.entity;


import lombok.*;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

import javax.persistence.*;
import java.time.Instant;

//@AllArgsConstructor
//@NoArgsConstructor

@Entity
@Table(name= "notice")
@Data   // @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor @Value 합침
        // @EqualsAndHashCode: equals()메소드와 hashCode()메소드를 생성해준다.
        // @RequiredArgsConstructor: 초기화안된 final 필드나 @NonNull이 붙은 필드에 대해 생성자를 만들어 준다
public class Notice extends BaseEntity {
    @Id // 기본키로 설정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 값 1씩 자동으로 증가
    private Long id;

    @ManyToOne  // 외래키 어노테이션
    @JoinColumn(name="sender_id")
    private User senderID;    // 회원ID(보내는)

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverID;    // 받는 회원ID. 0622 Long->User로 타입 변경해줌

    @Column(name = "message", length = 255, nullable = false)
    private String message;

    @Column(name = "read_or_not", nullable = false) // id가 아닌 다른 칼럼에는 @Column 붙여 표현할 수 있음
    private Boolean readOrNot;

    @Column(name = "type", length = 8, nullable = false)
    private String type;

    @Column(name = "reference_id", nullable = false)
    private Long referenceID;   // 게시글ID

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}