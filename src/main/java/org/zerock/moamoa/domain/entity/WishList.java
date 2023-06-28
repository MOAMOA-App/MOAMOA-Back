package org.zerock.moamoa.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.zerock.moamoa.common.domain.entity.BaseEntity;

import lombok.Data;

@Table(name = "wish_lists")
@Entity
@Data
public class WishList extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "product_id", nullable = false)
	private Product productId;

	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	private User userId;

	public void setUserId(User user) {
		this.userId = user;
		user.getWishLists().add(this);
	}

	public void removeUserId() {
		if (userId != null) {
			userId.getWishLists().remove(this);
			userId = null;
		}
	}
}
