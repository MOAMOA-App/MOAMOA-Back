package org.zerock.moamoa.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "notice")
@ToString
public class Notice {
    @Id // 기본키로 설정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 값 1씩 자동으로 증가
    private Long NID;

    @ManyToOne  // 외래키 어노테이션
    @JoinColumn(name="id")
    private User senderID;    // 회원ID

    @Column(name = "receiver_id", nullable = false) // id가 아닌 다른 칼럼에는 @Column 붙여 표현할 수 있음
    private Long receiverID;    // 근데얘도 User에서 받아와야되는거아니여 아닌가? 아니면 상품쪽 테이블이라던가

    @Column(name = "message", length = 255, nullable = false)
    private String message;

    @Column(name = "read_or_not", nullable = false)
    private Boolean readOrNot;

    @Column(name = "type", length = 8, nullable = false)
    private String type;

    @Column(name = "reference_id", nullable = false)
    private Long referenceID;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @PrePersist // DB에 해당 테이블의 insert 연산을 실행할 때 같이 실행하라는 의미.
    protected void onCreate() { // 현재 시간의 정보 주입해주는 함수 생성. insert할 때 현재시간정보 DB에 삽입 가능
        this.sendDate = LocalDateTime.now();
    }
}
