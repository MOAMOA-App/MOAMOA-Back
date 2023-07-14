package org.zerock.moamoa.domain.DTO.user;

import java.time.Instant;
import java.util.List;

import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.WishList;

import lombok.Builder;
import lombok.Data;

@Data
public class UserResponse {
	private Long id;
	private String loginType;
	private String name;
	private String email;
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
	public UserResponse(Long id, String loginType, String name, String email,
		String nick, String profImg, String address,
		String detailAddress, Instant createdAt, List<Party> parties,
		List<Product> myPosts, List<Party> myParties, List<Notice> notices,
		List<WishList> wishLists) {
		this.id = id;
		this.loginType = loginType;
		this.name = name;
		this.email = email;
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
