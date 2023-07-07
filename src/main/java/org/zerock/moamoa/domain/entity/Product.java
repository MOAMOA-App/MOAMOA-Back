package org.zerock.moamoa.domain.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne // Many = Board, User = One 한명의 유저는 여러개의 게시글을 쓸 수 있다.
	@JoinColumn(name = "seller_id") // foreign key (userId) references User (id)
	private User user; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다. //참조 할 테이블

	@Column(name = "category_id", nullable = false)
	private Category categoryId;

	@Column(name = "selling_area", nullable = false, length = 254)
	private String sellingArea;

	@Column(name = "detail_area", nullable = false, length = 254)
	private String detailArea;

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Column(name = "sell_price", nullable = false)
	private Integer sellPrice;

	@Column(name = "view_count")
	private Integer viewCount = 0;

	@Column(name = "description", nullable = false, length = 254)
	private String description;

	@Column(name = "sell_count")
	private Integer sellCount = 0;

	@Column(name = "max_count", nullable = false)
	private Integer maxCount;

	@Column(name = "choice_send", nullable = false, length = 32)
	private String choiceSend;

	@Column(name = "count_image")
	private Integer countImage;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Announce> announces;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Party> parties;

	@Column(name = "activate")
	private Boolean activate = true;

	@Column(name = "finished_at", nullable = false)
	private Instant finishedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	public void delete() {
		this.activate = false;
		this.deletedAt = Instant.now();
	}

	public void updateStatus(ProductStatus status) {
		this.status = status;
	}

	public void updateInfo(ProductUpdateRequest product) {
		this.categoryId = product.getCategoryId();
		this.sellingArea = product.getSellingArea();
		this.detailArea = product.getDetailArea();
		this.title = product.getTitle();
		this.description = product.getDescription();
		this.sellCount = product.getSellPrice();
		this.finishedAt = product.getFinishedAt();
		this.maxCount = product.getMaxCount();
		this.choiceSend = product.getChoiceSend();
		this.countImage = product.getCountImage();
	}

	@Builder
	public Product(
		Long id, User user, Category categoryId, String sellingArea, String detailArea, String title,
		ProductStatus status, Integer sellPrice, Integer viewCount, String description, Integer sellCount,
		Integer maxCount,
		String choiceSend, Integer countImage, List<Announce> announces, List<Party> parties, Boolean activate,
		Instant finishedAt, Instant deletedAt) {
		this.id = id;
		this.user = user;
		this.categoryId = categoryId;
		this.sellingArea = sellingArea;
		this.detailArea = detailArea;
		this.title = title;
		this.status = status;
		this.sellPrice = sellPrice;
		this.viewCount = viewCount;
		this.description = description;
		this.sellCount = sellCount;
		this.maxCount = maxCount;
		this.choiceSend = choiceSend;
		this.countImage = countImage;
		this.announces = announces;
		this.parties = parties;
		this.activate = activate;
		this.finishedAt = finishedAt;
		this.deletedAt = deletedAt;
	}

	public void addUser(User user) {
		this.user = user;
	}

	public void addAnnounce(Announce announce) {
		announces.add(announce);
		announce.setProduct(this);
	}

	public void removeAnnounce(Announce announce) {
		announces.remove(announce);
	}

	public void addParty(Party party) {
		parties.add(party);
		party.setProduct(this);
	}

	public void removeParty(Party party) {
		parties.remove(party);
	}

	public void  addUserPosts(User user){
		this.user = user;
		if (!user.getMyPosts().contains(this)){
			user.getMyPosts().add(this);
		}
	}

	public void removeUserPosts(User user){
		if (user != null){
			user.getMyPosts().remove(this);
			this.user = null;
		}
	}
}
