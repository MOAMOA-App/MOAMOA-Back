package org.zerock.moamoa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.zerock.moamoa.domain.entity.UserBlocked;

public class UserBlockedRepositoryImpl implements UserBlockedInterface {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserBlocked> findByUserList(Long UserId) {

		return null;
	}
}
