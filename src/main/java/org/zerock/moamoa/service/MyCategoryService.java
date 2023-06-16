package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.Category;
import org.zerock.moamoa.domain.entity.MyCategory;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.MyCategoryRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyCategoryService {
    private final MyCategoryRepository myCategoryRepository;

    public MyCategoryService(MyCategoryRepository myCategoryRepository) {
        this.myCategoryRepository = myCategoryRepository;
    }

    @Transactional
    public void saveMyCategory(List<Category> categories, User user) {
        // 이전에 저장된 MyCategory 엔티티 삭제
        myCategoryRepository.deleteAll();

        List<MyCategory> myCategories = new ArrayList<>();
        for (Category category : categories) {
            MyCategory myCategory = new MyCategory();
            myCategory.setUsers(user);
            myCategory.setCategories(category);
            myCategories.add(myCategory);
        }
        myCategoryRepository.saveAll(myCategories);

        // List<Category> categories = categoryRepository.findAll();
        // User user = userRepository.findById(1L);
        // myCategoryService.saveAll(categories, user);
        //  CategoryRepository와 UserRepository를 사용하여 Category 목록과 User를 가져온 후,
        //  MyCategoryService의 saveAll 메서드를 호출하여 MyCategory를 저장하고 있습니다.
    }

    @Transactional
    public List<Category> findAll(User user) {
        List<MyCategory> myCategories = myCategoryRepository.findByUsers(user);
        List<Category> categories = new ArrayList<>();
        for (MyCategory myCategory : myCategories) {
            categories.add(myCategory.getCategories());
        }
        return categories;
    }

    public void removeAll() {
        myCategoryRepository.deleteAll();
    }
}
