package org.zerock.moamoa.domain.entity;

import java.util.List;

import org.zerock.moamoa.common.domain.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
