package org.zerock.moamoa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.zerock.moamoa.domain.entity.UserBlocked;

import java.util.List;

public class UserBlockedRepositoryImpl implements UserBlockedInterface {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserBlocked> findByUserList(Long UserId) {

        return null;
    }
}
