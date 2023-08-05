package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "naver", length = 254)
    private String naver;

    @Column(name = "kakao", length = 254)
    private String kakao;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "nick", length = 32, nullable = false)
    private String nick;

    @Column(name = "prof_img", length = 32)
    private String profImg;

    @Column(name = "address", length = 254)
    private String address;

    @Column(name = "detailed_address", length = 254)
    private String detailAddress;

    @Column(name = "activate")
    private Boolean activate = true;

    @Column(name = "deleted_at")
    private Instant deletedAt;


    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Party> parties;    // 내가 참여한 공동구매?

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> myPosts;   // 내가 생성한 공동구매

    @OneToMany(mappedBy = "receiverID", cascade = CascadeType.ALL)  // 받는 쪽에 알림을 추가하기 위해 만듦
    private List<Notice> notices;   // Notice쪽도 receiverID 타입 User로 변경해줌!!!

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishList> wishLists;

    @Builder
    public User(Long id, String naver, String kakao, String name, String email,
                String password, String nick, String profImg, String address, String detailAddress, Boolean activate, Instant deletedAt) {
        this.id = id;
        this.naver = naver;
        this.kakao = kakao;
        this.name = name;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.profImg = profImg;
        this.address = address;
        this.detailAddress = detailAddress;
        this.activate = activate;
        this.deletedAt = deletedAt;
    }


    public User(Long id) {
        this.id = id;
    }

    public void delete() {
        this.activate = false;
        this.deletedAt = Instant.now();
    }

    public void updateProfile(UserProfileUpdateRequest UP) {
        this.nick = UP.getNick();
        this.profImg = UP.getProfImg();
        this.email = UP.getEmail();
        this.address = UP.getAddress();
        this.detailAddress = UP.getDetailAddress();
    }

    public void updatePw(String password) {
        this.password = password;
    }


    /**
     * 비밀번호 암호화
     *
     * @param passwordEncoder 암호화 할 인코더 클래스
     */
    public void hashPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    /**
     * 비밀번호 확인
     *
     * @param plainPassword   암호화 이전의 비밀번호
     * @param passwordEncoder 암호화에 사용된 클래스
     * @return true | false
     */
    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }


    /**
     * 닉네임 설정
     */
    // 가입할때 함 설정해주면 될거같은데 나머지는 프로필에서 뭐 알아서 랜덤으로 하든 설정을 하든 하겠지...
    // 일케 해놓고보니 닉네임 랜덤설정도 따로빼야되나싶기도함
    // 아니 생각해보니 프사설정 생각도못햇음 이런젠장멍충이;
    // YJ: 프사설정!!!!
    public void randomNick() {
        ArrayList<String> nickarr1 = new ArrayList<>(
                Arrays.asList("무지개", "분홍", "오렌지", "개나리", "연두", "해변의", "퍼렁", "보라", "갈색", "하얀"));
        ArrayList<String> nickarr2 = new ArrayList<>(
                Arrays.asList("웨옹", "곰돌이", "귀긴곰", "꽥", "고양이", "냥이", "곰", "토끼", "오리"));

        Random rnd = new Random();
        int rnum1 = rnd.nextInt(nickarr1.size());
        int rnum2 = rnd.nextInt(nickarr2.size());

        this.nick = nickarr1.get(rnum1) + nickarr2.get(rnum2);
    }
}