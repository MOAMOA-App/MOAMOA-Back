package org.zerock.moamoa.domain.entity;

import lombok.ToString;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name= "user")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UID;

    @Column(length = 11, nullable = false)
    private String login_type;
    @Column(length = 254, nullable = false)
    private String token;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 32, nullable = false)
    private String phone;

    @Column(length = 32, nullable = false)
    private String nick;

    @Column(length = 32, nullable = false)
    private String prof_img;

    @Column(length = 200, nullable = false)
    private String joindate;


//
//    @Id // 기본키로 설정한다.
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    // 데이터를 저장할 때 해당 속성에 따로 값을
//    // 셋팅하지 않아도 1씩 자동으로 증가
//    // strategy는 고유 번호를 생성하는 옵션
//    // GenerationType.IDENTITY는 해당 컬럼만의
//    // 독립적인 시퀀스를 생성하여 번호를 증가시킬 때
//    // 사용한다.
//    private Integer id;
//
//    @Column(length=200)
//    // Column의 세부 설정을 위해 사용
//    private String content;
//
//    @Column(nullable = false)
//    private Boolean completed;


}