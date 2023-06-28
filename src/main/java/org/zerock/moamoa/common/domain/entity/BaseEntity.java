package org.zerock.moamoa.common.domain.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
//다른 엔티티들이 상속받기 위한 기반 클래스
//공유 속성을 모아두는 상위 엔티티임을 나타내는 어노테이션
@EntityListeners(AuditingEntityListener.class)
//Entity의 변화를 지켜보는 클래스 정의
public class BaseEntity {
	@CreatedDate
	@Column(updatable = false)
	private Instant createdAt;

	@LastModifiedDate
	@Column(updatable = true)
	private Instant updatedAt;
}
