package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.common.user.RandomNick;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "naver", length = 254)
    @Setter
    private String naver;

    @Column(name = "kakao", length = 254)
    @Setter
    private String kakao;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "nick", length = 32, nullable = false)
    private String nick;

    @Column(name = "prof_img", length = 128)
    private String profImg;

    @Column(name = "address", length = 254)
    private String address;

    @Column(name = "detailed_address", length = 254)
    private String detailAddress;

    @Column(name = "activate")
    private Boolean activate = true;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Builder
    public User(String email, String nick, String password) {
        this.email = email;
        this.password = password;
        this.nick = nick;
    }

//    @Builder
//    public User(String naver, String kakao, String name, String email,
//                String password, String nick, String profImg, String address, String detailAddress, Boolean activate, Instant deletedAt) {
//        this.naver = naver;
//        this.kakao = kakao;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.nick = nick;
//        this.profImg = profImg;
//        this.address = address;
//        this.detailAddress = detailAddress;
//        this.activate = activate;
//        this.deletedAt = deletedAt;
//    }

    public void delete() {
        this.activate = false;
        this.deletedAt = Instant.now();
    }

    public void updateProfile(UserProfileUpdateRequest UP) {
        this.nick = UP.getNick();
        this.email = UP.getEmail();
        this.address = UP.getAddress();
        this.detailAddress = UP.getDetailAddress();
    }

    public void updatePw(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateImage(String file) {
        profImg = file;
    }

    public void updateNick(String nick){
        this.nick = nick;
    }
}