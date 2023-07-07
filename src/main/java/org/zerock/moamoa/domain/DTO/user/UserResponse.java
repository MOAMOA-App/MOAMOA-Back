package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.WishList;

import java.time.Instant;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String loginType;
    private String token;
    private String name;
    private String email;
    private String password;
    private String nick;
    private String profImg;
    private String address;
    private String detailAddress;
    private Instant createdAt;
    private List<Party> parties;
    private List<Product> myPosts;
    private List<Party> myParties;
    private List<Notice> notices;
    private List<WishList> wishLists;

    @Builder
    public UserResponse(Long id, String loginType, String token, String name, String email,
                        String password, String nick, String profImg, String address,
                        String detailAddress, Instant createdAt, List<Party> parties,
                        List<Product> myPosts, List<Party> myParties, List<Notice> notices,
                        List<WishList> wishLists) {
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
        this.createdAt = createdAt;
        this.parties = parties;
        this.myPosts = myPosts;
        this.myParties = myParties;
        this.notices = notices;
        this.wishLists = wishLists;
    }
}
