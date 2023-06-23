package org.zerock.moamoa.domain.entity;

import lombok.*;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(name= "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_type", length = 11, nullable = false)
    private String loginType;

    @Column(name = "token", length = 254)
    private String token;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "nick", length = 32, nullable = false)
    private String nick;

    @Column(name = "prof_img", length = 32)
    private String profImg;

    @Column(name = "address", length=254)
    private String address;

    @Column(name = "detailed_address", length=254)
    private String detailAddress;

    @Column(name = "created_at")
    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Party> parties;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)z
//    private List<Product> myPost;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Product> myParty;

    @OneToMany(mappedBy = "receiverID", cascade = CascadeType.ALL)  // 받는 쪽에 알림을 추가하기 위해 만듦
    private List<Notice> notices;   // Notice쪽도 receiverID 타입 User로 변경해줌!!!

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<WishList> wishLists;

    @PrePersist
    protected void onCreate() {
        this.joinDate = LocalDateTime.now();
    }

    public void addParty(Party party) {
        parties.add(party);
        party.setUser(this);
    }

    public void removeParty(Party party) {
        parties.remove(party);
    }

    // 위시리스트 추가/제거
    public void addWishList(WishList wishList) {
        if (wishList != null) {
            wishLists.add(wishList);
            wishList.setUserId(this);
        }
    }
    public void removeWishList(WishList wishList) {
        wishLists.remove(wishList);
        if (wishList.getUserId() == this) {
            wishList.setUserId(null);
        }
    }

    // 알림 추가/제거
    public void addNotice(Notice notice) {
        if (notice != null) {
            notices.add(notice);
            notice.setReceiverID(this);
        }
    }

    public void removeNotice(Notice notice) {
        if (notice != null) {
            notices.remove(notice);
            notice.setReceiverID(null);
        }
    }

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


//=======
//import lombok.*;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
////@AllArgsConstructor // 전체 필드에 대한 생성자 만듦(모든 파라미터 받는 생성자 만듦)......이라는데 아이고 모르겠다 일단 킵
////@NoArgsConstructor  // 해당 클래스에 기본 생성자 만듦
////                    // 보통 access = AccessLevel.PROTECTED을 같이 써서 접근 조건 건다고 함(JPA가 받아들일 수 있는 최대 수준)
////                    // @Builder로 빌더 패턴 사용-> 이미 있는 생성자에 접근 (->프로텍트로 막혀 있다면 오류 발생 ->@All~ 사용)
//@Getter // @Getter와 @Setter는 Lombok(어노테이션 기반으로 코드를 자동완성 해주는 라이브러리)에셔 사용하는 라이브러리
//        // 인프런에서는 직접 만들어주고 투두리스트 만들땐 요 어노테이션 사용했네
//@Setter // 예를 들어 xxx라는 필드에 선언하면 자동으로 getXxx()(boolean 타입: isXxx())와 setXxx() 메소드를 생성
//        // 필드 레벨이 아닌 클래스 레벨에 선언 시 모든 필드에 접근자와 설정자가 자동으로 생성
//        // 데이터베이스의 실제 데이터와 그 엔티티가 일관된 상태를 유지하고 있어야 하기 때문에 @Setter는 붙이지 않는다고 함?
//@Entity // DB 테이블과 일대일로 매칭되는 객체 단위
//@Table(name= "users")    // 테이블과 연결 위해 사용, 엔티티와 테이블네임을 일치시킬 수 없을때 테이블과 엔티티를 매칭시킴
//@ToString   // 변수 값들을 리턴해주는 ToString 메소드를 자동 생성 (객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴)
//@Data   // @Getter, @Setter
//public class User {
//    @Id // 기본키로 설정한다.
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 값 1씩 자동으로 증가
//    private Long uid;
//
//    // id가 아닌 다른 칼럼에는 @Column 붙여 표현할 수 있음(쓰지 않아도 컬럼으로 자동 인식), 컬럼의 조건들은 괄호 안에
//    @Column(name = "login_type", length = 11, nullable = false)
//    private String loginType;
//
//    @Column(name = "token", length = 254, nullable = false)
//    private String loginToken;
//
//    @Column(name = "name", length = 11, nullable = false)
//    private String name;
//
//    @Column(name = "email", length = 50, nullable = false)
//    private String email;
//
//    @Column(name = "phone", length = 11, nullable = false)
//    private String phoneNum;
//
//    @Column(name = "nick", length = 11, nullable = false)
//    private String nickName;
//
//    @Column(name = "prof_img", length = 50, nullable = false)
//    private String profImg;
//
//    @Column(name = "user_addr", length = 254, nullable = false)
//    private String userAddr;
//
//    @Column(name = "detail_addr", length = 254, nullable = false)
//    private String userDetailAddr;
//
//    @Column(name = "active", length = 254, nullable = false)
//    private String active;
//
//    @CreatedDate
//    @Column(name = "joindate", nullable = false)
//    private LocalDateTime joinDate;
//
//    @PrePersist
//    protected void onCreate() {
//        this.joinDate = LocalDateTime.now();
//    }
}