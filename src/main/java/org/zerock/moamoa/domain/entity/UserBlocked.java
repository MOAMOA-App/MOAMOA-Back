package org.zerock.moamoa.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "block_users")
@Entity
@Data
public class UserBlocked {
	//DB상에서의 id명이 user_id로 되어 있어서 맞출 필요가 있음
	//id에 user_id를 그대로 넣는 경우
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "target_id")
	User target;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
