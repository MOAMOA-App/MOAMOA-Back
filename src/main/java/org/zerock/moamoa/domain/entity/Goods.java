package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "goods")
@ToString
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UID;

    @Column(length = 11, nullable = false)
    private Long seller_id;
    @Column(length = 11, nullable = false)
    private Long category_id;

    @Column(length = 254, nullable = false)
    private String selling_area;

    @Column(length = 254, nullable = false)
    private String detail_area;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 32, nullable = false)
    private String status;

    @Column(length = 200, nullable = false)
    private int sell_price;

    @Column(length = 200, nullable = false)
    private int view_count;

    @Column(length = 254, nullable = false)
    private String description; //text

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @Column(name = "dead_date", nullable = false)
    private LocalDateTime deadDate;

    @PrePersist
    protected void onCreate2() {
        this.deadDate = LocalDateTime.now();
    }
    @Column(length = 200, nullable = false)
    private int sell_count;
    @Column(length = 200, nullable = false)
    private int max_count;
    @Column(length = 32, nullable = false)
    private String choice_send;


    @Id // 기본키로 설정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 데이터를 저장할 때 해당 속성에 따로 값을
    // 셋팅하지 않아도 1씩 자동으로 증가
    // strategy는 고유 번호를 생성하는 옵션
    // GenerationType.IDENTITY는 해당 컬럼만의
    // 독립적인 시퀀스를 생성하여 번호를 증가시킬 때
    // 사용한다.
    private Integer id;

    @Column(length=200)
    // Column의 세부 설정을 위해 사용
    private String content;

    @Column(nullable = false)
    private Boolean completed;


}