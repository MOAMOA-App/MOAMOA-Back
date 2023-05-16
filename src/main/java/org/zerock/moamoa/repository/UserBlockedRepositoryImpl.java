package org.zerock.moamoa.repository;

import org.zerock.moamoa.domain.entity.UserBlocked;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class UserBlockedRepositoryImpl implements UserBlockedInterface{
    @PersistenceContext
    private EntityManager em;
    @Override
    public List<UserBlocked> findByUserList(Long UserId) {

        return null;
    }
}
