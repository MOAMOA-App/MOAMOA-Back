package org.zerock.moamoa.domain.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;
import org.zerock.moamoa.domain.DTO.user.UserPwUpdateRequest;

import javax.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name= "users")
public class User extends BaseEntity {
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

    @Column(name = "activate")
    private Boolean activate = true;

    @Column(name = "deleted_at", nullable = false)
    private Instant deletedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Party> parties;    // 내가 참여한 공동구매?

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> myPosts;   // 내가 생성한 공동구매

    @OneToMany(mappedBy = "receiverID", cascade = CascadeType.ALL)  // 받는 쪽에 알림을 추가하기 위해 만듦
    private List<Notice> notices;   // Notice쪽도 receiverID 타입 User로 변경해줌!!!

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishList> wishLists;

    @Builder
    public User(Long id, String loginType, String token, String name, String email, String password,
                String nick, String profImg, String address, String detailAddress, Boolean activate,
                Instant deletedAt, Instant createdAt, List<Party> parties, List<Product> myPosts,
                List<Notice> notices, List<WishList> wishLists) {
        this.id = id;
        this.loginType = loginType;
        this.token = token;
        this.name = name;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.profImg = profImg;
        this.address = address;
        this.detailAddress = detailAddress;
        this.activate = activate;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.parties = parties;
        this.myPosts = myPosts;
        this.notices = notices;
        this.wishLists = wishLists;
    }

    public void delete() {
        this.activate = false;
        this.deletedAt = Instant.now();
    }

    public void updateProfile(UserProfileUpdateRequest UP){
        this.nick = UP.getNick();
        this.profImg = UP.getProfImg();
        this.email = UP.getEmail();
        this.address = UP.getAddress();
        this.detailAddress = UP.getDetailAddress();
    }

    public void updatePw(UserPwUpdateRequest UPw){
        this.password = UPw.getNewPw();
    }

    public void addParty(Party party) {
        parties.add(party);
        party.setUser(this);
    }

    public void removeParty(Party party) {
        parties.remove(party);
    }

    /**
     * 비밀번호 암호화
     * @param passwordEncoder 암호화 할 인코더 클래스
     * @return 변경된 유저 Entity
     */
    public User hashPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    /**
     * 비밀번호 확인
     * @param plainPassword 암호화 이전의 비밀번호
     * @param passwordEncoder 암호화에 사용된 클래스
     * @return true | false
     */
    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }
}