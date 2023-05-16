package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.MyCategory;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;

@Repository
public interface MyCategoryRepository extends JpaRepository<MyCategory,Long> {
    List<MyCategory> findByUsers(User user);

    //List<MyCategory> myCategories = myCategoryRepository.findAllByUsers(user);
    //List<Category> categories = new ArrayList<>();
    //for (MyCategory myCategory : myCategories) {
    //    categories.add(myCategory.getCategories());
    //}
    // 위의 코드에서는 MyCategoryRepository의 findAllByUsers 메서드를 호출하여
    // 해당 유저가 저장한 모든 카테고리를 불러온 후,
    // for-each 루프를 이용하여 Category 객체로 변환하여 List<Category> 형태로 반환합니다.
    // 이후에 이 Category 목록을 이용하여 다양한 작업을 수행할 수 있습니다.
}
