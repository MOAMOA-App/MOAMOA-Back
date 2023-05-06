package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.WishList;

public interface WishListRepository  extends JpaRepository<WishList,Long> {

}
