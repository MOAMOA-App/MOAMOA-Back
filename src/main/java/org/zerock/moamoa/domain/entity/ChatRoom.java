package org.zerock.moamoa.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.zerock.moamoa.common.domain.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User sellerId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
	private List<ChatMessage> messages;

}
