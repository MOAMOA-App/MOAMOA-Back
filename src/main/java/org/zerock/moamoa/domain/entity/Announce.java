package org.zerock.moamoa.domain.entity;

import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "announces")
public class Announce extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "is_lock", nullable = false)
	private Boolean lock;

	@Column(name = "contents", nullable = false)
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	public void updateInfo(AnnounceRequest announce) {
		this.lock = announce.getLock();
		this.contents = announce.getContents();
	}

	public void setProduct(Product product) {
		this.product = product;
		if (!product.getAnnounces().contains(this)) {
			product.getAnnounces().add(this);
		}
	}

	@Builder
	public Announce(Long id, Boolean lock, String contents, Product product) {
		this.id = id;
		this.lock = lock;
		this.contents = contents;
		this.product = product;
	}
}