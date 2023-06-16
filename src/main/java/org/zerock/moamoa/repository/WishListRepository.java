package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository  extends JpaRepository<WishList,Long> {
    List<WishList> findByUserId(User user);
}
